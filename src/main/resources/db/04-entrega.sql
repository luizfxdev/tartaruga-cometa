CREATE TABLE entrega (
    id SERIAL,
    codigo_rastreio VARCHAR(50) NOT NULL,
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

    CONSTRAINT entrega_pkey PRIMARY KEY (id),
    CONSTRAINT entrega_codigo_rastreio_key UNIQUE (codigo_rastreio),
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
    CONSTRAINT chk_valor_frete_positivo CHECK (valor_frete IS NULL OR valor_frete > 0),
    CONSTRAINT chk_data_coleta CHECK (data_coleta IS NULL OR data_coleta >= data_criacao),
    CONSTRAINT chk_data_envio CHECK (data_envio IS NULL OR data_envio >= data_coleta),
    CONSTRAINT chk_data_entrega CHECK (data_entrega IS NULL OR data_entrega >= data_envio)
);

CREATE INDEX idx_entrega_codigo_rastreio ON entrega(codigo_rastreio);
CREATE INDEX idx_entrega_remetente ON entrega(id_remetente);
CREATE INDEX idx_entrega_destinatario ON entrega(id_destinatario);
CREATE INDEX idx_entrega_status ON entrega(status);
CREATE INDEX idx_entrega_status_data_criacao ON entrega(status, data_criacao);
