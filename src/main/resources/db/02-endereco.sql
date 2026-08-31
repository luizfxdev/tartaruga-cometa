CREATE TABLE endereco (
    id SERIAL,
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
    pais VARCHAR(100) NOT NULL DEFAULT 'Brasil',
    is_principal BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT endereco_pkey PRIMARY KEY (id),
    CONSTRAINT fk_endereco_cliente FOREIGN KEY (id_cliente)
        REFERENCES cliente(id) ON DELETE CASCADE,
    CONSTRAINT chk_tipo_endereco CHECK (tipo_endereco IN ('ORIGEM', 'DESTINO', 'CADASTRO')),
    CONSTRAINT chk_estado CHECK (LENGTH(estado) = 2),
    CONSTRAINT chk_cep CHECK (cep ~ '^\d{5}-?\d{3}$')
);

CREATE INDEX idx_endereco_cliente ON endereco(id_cliente);
CREATE INDEX idx_endereco_is_principal ON endereco(id_cliente, is_principal);
