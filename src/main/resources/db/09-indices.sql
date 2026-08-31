-- Índice para busca por código de rastreamento (busca crítica)
CREATE INDEX idx_entrega_codigo_rastreio_busca ON entrega(codigo_rastreio);

-- Índice para filtros por status (listagem crítica)
CREATE INDEX idx_entrega_status_filtro ON entrega(status);

-- Índice composto para filtro por status e ordenação por data (listagem com ordenação)
CREATE INDEX idx_entrega_status_data ON entrega(status, data_criacao);
