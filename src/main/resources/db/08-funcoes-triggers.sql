CREATE OR REPLACE FUNCTION update_timestamp_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER trg_update_client_timestamp BEFORE UPDATE ON client
    FOR EACH ROW EXECUTE FUNCTION update_timestamp_column();

CREATE TRIGGER trg_update_product_timestamp BEFORE UPDATE ON product
    FOR EACH ROW EXECUTE FUNCTION update_timestamp_column();

CREATE TRIGGER trg_update_delivery_timestamp BEFORE UPDATE ON delivery
    FOR EACH ROW EXECUTE FUNCTION update_timestamp_column();

CREATE TRIGGER trg_update_address_timestamp BEFORE UPDATE ON address
    FOR EACH ROW EXECUTE FUNCTION update_timestamp_column();

CREATE OR REPLACE FUNCTION prevent_delivery_history_update()
RETURNS TRIGGER AS $$
BEGIN
    RAISE EXCEPTION 'delivery_history is append-only and cannot be modified';
END;
$$ language 'plpgsql';

CREATE TRIGGER trg_prevent_delivery_history_update BEFORE UPDATE ON delivery_history
    FOR EACH ROW EXECUTE FUNCTION prevent_delivery_history_update();
