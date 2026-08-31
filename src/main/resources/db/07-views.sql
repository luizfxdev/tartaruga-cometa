DROP VIEW IF EXISTS vw_entregas_completas CASCADE;
DROP VIEW IF EXISTS vw_estatisticas_entregas CASCADE;

CREATE VIEW vw_entregas_completas AS
SELECT
    e.id,
    e.codigo_rastreio,
    e.status,
    e.data_criacao,
    e.data_entrega,
    cr.nome AS nome_remetente,
    cr.documento AS documento_remetente,
    cd.nome AS nome_destinatario,
    cd.documento AS documento_destinatario,
    ao.cidade AS cidade_origem,
    ao.estado AS estado_origem,
    ad.cidade AS cidade_destino,
    ad.estado AS estado_destino,
    e.valor_total,
    e.valor_frete,
    e.peso_total_kg,
    e.volume_total_m3
FROM entrega e
INNER JOIN cliente cr ON e.id_remetente = cr.id
INNER JOIN cliente cd ON e.id_destinatario = cd.id
INNER JOIN endereco ao ON e.id_endereco_origem = ao.id
INNER JOIN endereco ad ON e.id_endereco_destino = ad.id;

CREATE VIEW vw_estatisticas_entregas AS
SELECT
    status,
    COUNT(*) AS quantidade,
    SUM(valor_total) AS valor_total,
    SUM(peso_total_kg) AS peso_total,
    AVG(valor_frete) AS valor_frete_medio
FROM entrega
GROUP BY status;
