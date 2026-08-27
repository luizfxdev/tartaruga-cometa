-- Índice para busca por código de rastreamento (busca crítica)
CREATE INDEX idx_delivery_tracking_code_search ON delivery(tracking_code);

-- Índice para filtros por status (listagem crítica)
CREATE INDEX idx_delivery_status_filter ON delivery(status);

-- Índice composto para filtro por status e ordenação por data (listagem com ordenação)
CREATE INDEX idx_delivery_status_date ON delivery(status, creation_date);
