DROP VIEW IF EXISTS vw_complete_deliveries CASCADE;
DROP VIEW IF EXISTS vw_delivery_statistics CASCADE;

CREATE VIEW vw_complete_deliveries AS
SELECT
    d.id,
    d.tracking_code,
    d.status,
    d.creation_date,
    d.delivery_date,
    cr.name AS sender_name,
    cr.document AS sender_document,
    cd.name AS recipient_name,
    cd.document AS recipient_document,
    ao.city AS origin_city,
    ao.state AS origin_state,
    ad.city AS destination_city,
    ad.state AS destination_state,
    d.total_value,
    d.freight_value,
    d.total_weight_kg,
    d.total_volume_m3
FROM delivery d
INNER JOIN client cr ON d.sender_id = cr.id
INNER JOIN client cd ON d.recipient_id = cd.id
INNER JOIN address ao ON d.origin_address_id = ao.id
INNER JOIN address ad ON d.destination_address_id = ad.id;

CREATE VIEW vw_delivery_statistics AS
SELECT
    status,
    COUNT(*) AS quantity,
    SUM(total_value) AS total_value,
    SUM(total_weight_kg) AS total_weight,
    AVG(freight_value) AS average_freight_value
FROM delivery
GROUP BY status;
