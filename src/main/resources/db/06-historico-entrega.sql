CREATE TABLE historico_entrega (
    id SERIAL,
    id_entrega INTEGER NOT NULL,
    status_anterior status_entrega,
    status_novo status_entrega NOT NULL,
    data_mudanca TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    usuario VARCHAR(100),
    observacoes TEXT,
    localizacao VARCHAR(200),

    CONSTRAINT historico_entrega_pkey PRIMARY KEY (id),
    CONSTRAINT fk_historico_entrega FOREIGN KEY (id_entrega)
        REFERENCES entrega(id) ON DELETE CASCADE
);

CREATE INDEX idx_historico_entrega_entrega ON historico_entrega(id_entrega);
CREATE INDEX idx_historico_entrega_data_mudanca ON historico_entrega(data_mudanca);
CREATE INDEX idx_historico_entrega_status_novo ON historico_entrega(status_novo);
