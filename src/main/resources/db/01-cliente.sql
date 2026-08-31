CREATE TABLE cliente (
    id SERIAL,
    tipo_pessoa tipo_pessoa NOT NULL,
    documento VARCHAR(20) NOT NULL,
    nome VARCHAR(200) NOT NULL,
    email VARCHAR(100),
    telefone VARCHAR(20),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT cliente_pkey PRIMARY KEY (id),
    CONSTRAINT cliente_documento_key UNIQUE (documento),
    CONSTRAINT chk_documento_length CHECK (
        (tipo_pessoa = 'PF' AND LENGTH(REPLACE(documento, '.', '')) = 11) OR
        (tipo_pessoa = 'PJ' AND LENGTH(REPLACE(documento, '.', '')) = 14)
    ),
    CONSTRAINT chk_email_format CHECK (email ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$' OR email IS NULL)
);

CREATE INDEX idx_cliente_documento ON cliente(documento);
CREATE INDEX idx_cliente_tipo_pessoa ON cliente(tipo_pessoa);
