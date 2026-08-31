CREATE TABLE entrega_produto (
    id SERIAL,
    id_entrega INTEGER NOT NULL,
    id_produto INTEGER NOT NULL,
    quantidade INTEGER NOT NULL DEFAULT 1,
    peso_unitario_kg DECIMAL(10,3) NOT NULL,
    volume_unitario_m3 DECIMAL(10,4) NOT NULL,
    valor_unitario DECIMAL(12,2) NOT NULL,
    subtotal DECIMAL(12,2) NOT NULL,
    observacoes TEXT,

    CONSTRAINT entrega_produto_pkey PRIMARY KEY (id),
    CONSTRAINT fk_entrega_produto_entrega FOREIGN KEY (id_entrega)
        REFERENCES entrega(id) ON DELETE CASCADE,
    CONSTRAINT fk_entrega_produto_produto FOREIGN KEY (id_produto)
        REFERENCES produto(id) ON DELETE RESTRICT,
    CONSTRAINT uk_entrega_produto UNIQUE (id_entrega, id_produto),
    CONSTRAINT chk_quantidade_positiva CHECK (quantidade > 0),
    CONSTRAINT chk_peso_unitario_positivo CHECK (peso_unitario_kg > 0),
    CONSTRAINT chk_volume_unitario_positivo CHECK (volume_unitario_m3 > 0),
    CONSTRAINT chk_valor_unitario_positivo CHECK (valor_unitario > 0),
    CONSTRAINT chk_subtotal CHECK (subtotal = quantidade * valor_unitario)
);

CREATE INDEX idx_entrega_produto_entrega ON entrega_produto(id_entrega);
CREATE INDEX idx_entrega_produto_produto ON entrega_produto(id_produto);
