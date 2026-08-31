CREATE TABLE produto (
    id SERIAL,
    nome VARCHAR(200) NOT NULL,
    descricao TEXT,
    preco DECIMAL(12,2),
    peso_kg DECIMAL(10,3) NOT NULL,
    volume_m3 DECIMAL(10,4) NOT NULL,
    valor_declarado DECIMAL(12,2) NOT NULL,
    categoria VARCHAR(50),
    ativo BOOLEAN DEFAULT TRUE,
    estoque INTEGER DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT produto_pkey PRIMARY KEY (id),
    CONSTRAINT chk_peso_positivo CHECK (peso_kg > 0),
    CONSTRAINT chk_volume_positivo CHECK (volume_m3 > 0),
    CONSTRAINT chk_valor_positivo CHECK (valor_declarado > 0),
    CONSTRAINT chk_preco_positivo CHECK (preco IS NULL OR preco > 0),
    CONSTRAINT chk_estoque_positivo CHECK (estoque IS NULL OR estoque >= 0)
);

CREATE INDEX idx_produto_ativo ON produto(ativo);
CREATE INDEX idx_produto_categoria ON produto(categoria);
