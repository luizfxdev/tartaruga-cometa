DROP TABLE IF EXISTS historico_entrega CASCADE;
DROP TABLE IF EXISTS entrega_produto CASCADE;
DROP TABLE IF EXISTS entrega CASCADE;
DROP TABLE IF EXISTS produto CASCADE;
DROP TABLE IF EXISTS endereco CASCADE;
DROP TABLE IF EXISTS cliente CASCADE;

DROP TYPE IF EXISTS tipo_pessoa CASCADE;
DROP TYPE IF EXISTS status_entrega CASCADE;

CREATE TYPE tipo_pessoa AS ENUM ('PF', 'PJ');

CREATE TYPE status_entrega AS ENUM (
    'PENDENTE',
    'EM_TRANSITO',
    'ENTREGUE',
    'CANCELADA',
    'NAO_REALIZADA'
);

CREATE TABLE cliente (
    id SERIAL PRIMARY KEY,
    tipo_pessoa tipo_pessoa NOT NULL,
    documento VARCHAR(20) NOT NULL UNIQUE,
    nome VARCHAR(200) NOT NULL,
    email VARCHAR(100),
    telefone VARCHAR(20),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT chk_documento_length CHECK (
        (tipo_pessoa = 'PF' AND LENGTH(REPLACE(documento, '.', '')) = 11) OR
        (tipo_pessoa = 'PJ' AND LENGTH(REPLACE(documento, '.', '')) = 14)
    ),
    CONSTRAINT chk_email_format CHECK (email ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$' OR email IS NULL)
);

CREATE INDEX idx_cliente_documento ON cliente(documento);
CREATE INDEX idx_cliente_nome ON cliente(nome);
CREATE INDEX idx_cliente_tipo ON cliente(tipo_pessoa);

CREATE TABLE endereco (
    id SERIAL PRIMARY KEY,
    id_cliente INTEGER NOT NULL,
    tipo_endereco VARCHAR(20) NOT NULL,
    logradouro VARCHAR(200) NOT NULL,
    numero VARCHAR(10) NOT NULL,
    complemento VARCHAR(100),
    bairro VARCHAR(100) NOT NULL,
    cidade VARCHAR(100) NOT NULL,
    estado CHAR(2) NOT NULL,
    cep VARCHAR(10) NOT NULL,
    referencia TEXT,
    is_principal BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_endereco_cliente FOREIGN KEY (id_cliente)
        REFERENCES cliente(id) ON DELETE CASCADE,
    CONSTRAINT chk_tipo_endereco CHECK (tipo_endereco IN ('ORIGEM', 'DESTINO', 'CADASTRAL')),
    CONSTRAINT chk_estado CHECK (LENGTH(estado) = 2),
    CONSTRAINT chk_cep CHECK (cep ~ '^\d{5}-?\d{3}$')
);

CREATE INDEX idx_endereco_cliente ON endereco(id_cliente);
CREATE INDEX idx_endereco_cep ON endereco(cep);
CREATE INDEX idx_endereco_principal ON endereco(id_cliente, is_principal);

CREATE TABLE produto (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(200) NOT NULL,
    descricao TEXT,
    peso_kg DECIMAL(10,3) NOT NULL,
    volume_m3 DECIMAL(10,4) NOT NULL,
    valor_declarado DECIMAL(12,2) NOT NULL,
    categoria VARCHAR(50),
    ativo BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT chk_peso_positivo CHECK (peso_kg > 0),
    CONSTRAINT chk_volume_positivo CHECK (volume_m3 > 0),
    CONSTRAINT chk_valor_positivo CHECK (valor_declarado > 0)
);

CREATE INDEX idx_produto_nome ON produto(nome);
CREATE INDEX idx_produto_ativo ON produto(ativo);
CREATE INDEX idx_produto_categoria ON produto(categoria);

CREATE TABLE entrega (
    id SERIAL PRIMARY KEY,
    codigo_rastreio VARCHAR(50) UNIQUE NOT NULL,
    id_remetente INTEGER NOT NULL,
    id_destinatario INTEGER NOT NULL,
    id_endereco_origem INTEGER NOT NULL,
    id_endereco_destino INTEGER NOT NULL,
    status status_entrega NOT NULL DEFAULT 'PENDENTE',
    valor_total DECIMAL(12,2) NOT NULL DEFAULT 0.00,
    peso_total_kg DECIMAL(10,3) NOT NULL DEFAULT 0.000,
    volume_total_m3 DECIMAL(10,4) NOT NULL DEFAULT 0.0000,
    valor_frete DECIMAL(10,2),
    observacoes TEXT,
    data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_coleta TIMESTAMP,
    data_envio TIMESTAMP,
    data_entrega TIMESTAMP,
    data_cancelamento TIMESTAMP,
    motivo_cancelamento TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_entrega_remetente FOREIGN KEY (id_remetente)
        REFERENCES cliente(id) ON DELETE RESTRICT,
    CONSTRAINT fk_entrega_destinatario FOREIGN KEY (id_destinatario)
        REFERENCES cliente(id) ON DELETE RESTRICT,
    CONSTRAINT fk_entrega_endereco_origem FOREIGN KEY (id_endereco_origem)
        REFERENCES endereco(id) ON DELETE RESTRICT,
    CONSTRAINT fk_entrega_endereco_destino FOREIGN KEY (id_endereco_destino)
        REFERENCES endereco(id) ON DELETE RESTRICT,
    CONSTRAINT chk_remetente_diferente_destinatario CHECK (id_remetente != id_destinatario),
    CONSTRAINT chk_endereco_origem_diferente_destino CHECK (id_endereco_origem != id_endereco_destino),
    CONSTRAINT chk_valor_total_positivo CHECK (valor_total >= 0),
    CONSTRAINT chk_valor_frete_positivo CHECK (valor_frete IS NULL OR valor_frete >= 0),
    CONSTRAINT chk_data_coleta CHECK (data_coleta IS NULL OR data_coleta >= data_criacao),
    CONSTRAINT chk_data_envio CHECK (data_envio IS NULL OR data_envio >= data_coleta),
    CONSTRAINT chk_data_entrega CHECK (data_entrega IS NULL OR data_entrega >= data_envio)
);

CREATE UNIQUE INDEX idx_entrega_codigo ON entrega(codigo_rastreio);
CREATE INDEX idx_entrega_remetente ON entrega(id_remetente);
CREATE INDEX idx_entrega_destinatario ON entrega(id_destinatario);
CREATE INDEX idx_entrega_status ON entrega(status);
CREATE INDEX idx_entrega_data_criacao ON entrega(data_criacao);
CREATE INDEX idx_entrega_status_data ON entrega(status, data_criacao);

CREATE TABLE entrega_produto (
    id SERIAL PRIMARY KEY,
    id_entrega INTEGER NOT NULL,
    id_produto INTEGER NOT NULL,
    quantidade INTEGER NOT NULL DEFAULT 1,
    peso_unitario_kg DECIMAL(10,3) NOT NULL,
    volume_unitario_m3 DECIMAL(10,4) NOT NULL,
    valor_unitario DECIMAL(12,2) NOT NULL,
    subtotal DECIMAL(12,2) NOT NULL,
    observacoes TEXT,

    CONSTRAINT fk_entrega_produto_entrega FOREIGN KEY (id_entrega)
        REFERENCES entrega(id) ON DELETE CASCADE,
    CONSTRAINT fk_entrega_produto_produto FOREIGN KEY (id_produto)
        REFERENCES produto(id) ON DELETE RESTRICT,
    CONSTRAINT chk_quantidade_positiva CHECK (quantidade > 0),
    CONSTRAINT chk_peso_unitario_positivo CHECK (peso_unitario_kg > 0),
    CONSTRAINT chk_volume_unitario_positivo CHECK (volume_unitario_m3 > 0),
    CONSTRAINT chk_valor_unitario_positivo CHECK (valor_unitario > 0),
    CONSTRAINT chk_subtotal CHECK (subtotal = quantidade * valor_unitario),
    CONSTRAINT uk_entrega_produto UNIQUE (id_entrega, id_produto)
);

CREATE INDEX idx_ep_entrega ON entrega_produto(id_entrega);
CREATE INDEX idx_ep_produto ON entrega_produto(id_produto);

CREATE TABLE historico_entrega (
    id SERIAL PRIMARY KEY,
    id_entrega INTEGER NOT NULL,
    status_anterior status_entrega,
    status_novo status_entrega NOT NULL,
    data_mudanca TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    usuario VARCHAR(100),
    observacoes TEXT,
    localizacao VARCHAR(200),

    CONSTRAINT fk_historico_entrega FOREIGN KEY (id_entrega)
        REFERENCES entrega(id) ON DELETE CASCADE
);

CREATE INDEX idx_historico_entrega ON historico_entrega(id_entrega);
CREATE INDEX idx_historico_data ON historico_entrega(data_mudanca);
CREATE INDEX idx_historico_status ON historico_entrega(status_novo);

CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_cliente_updated_at BEFORE UPDATE ON cliente
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_produto_updated_at BEFORE UPDATE ON produto
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_entrega_updated_at BEFORE UPDATE ON entrega
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE OR REPLACE FUNCTION registrar_mudanca_status()
RETURNS TRIGGER AS $$
BEGIN
    IF (TG_OP = 'UPDATE' AND OLD.status IS DISTINCT FROM NEW.status) THEN
        INSERT INTO historico_entrega (id_entrega, status_anterior, status_novo, usuario)
        VALUES (NEW.id, OLD.status, NEW.status, CURRENT_USER);
    END IF;
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER trigger_mudanca_status AFTER UPDATE ON entrega
    FOR EACH ROW EXECUTE FUNCTION registrar_mudanca_status();

CREATE OR REPLACE VIEW vw_entregas_completas AS
SELECT
    e.id,
    e.codigo_rastreio,
    e.status,
    e.data_criacao,
    e.data_entrega,
    cr.nome AS remetente_nome,
    cr.documento AS remetente_documento,
    cd.nome AS destinatario_nome,
    cd.documento AS destinatario_documento,
    eo.cidade AS origem_cidade,
    eo.estado AS origem_estado,
    ed.cidade AS destino_cidade,
    ed.estado AS destino_estado,
    e.valor_total,
    e.valor_frete,
    e.peso_total_kg,
    e.volume_total_m3
FROM entrega e
INNER JOIN cliente cr ON e.id_remetente = cr.id
INNER JOIN cliente cd ON e.id_destinatario = cd.id
INNER JOIN endereco eo ON e.id_endereco_origem = eo.id
INNER JOIN endereco ed ON e.id_endereco_destino = ed.id;

CREATE OR REPLACE VIEW vw_estatisticas_entregas AS
SELECT
    status,
    COUNT(*) AS quantidade,
    SUM(valor_total) AS valor_total,
    SUM(peso_total_kg) AS peso_total,
    AVG(valor_frete) AS valor_frete_medio
FROM entrega
GROUP BY status;
