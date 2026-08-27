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

CREATE OR REPLACE FUNCTION log_delivery_status_change()
RETURNS TRIGGER AS $$
BEGIN
    IF (TG_OP = 'UPDATE' AND OLD.status IS DISTINCT FROM NEW.status) THEN
        INSERT INTO delivery_history (delivery_id, previous_status, new_status, "user")
        VALUES (NEW.id, OLD.status, NEW.status, CURRENT_USER);
    END IF;
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER trg_log_delivery_status_change AFTER UPDATE ON delivery
    FOR EACH ROW EXECUTE FUNCTION log_delivery_status_change();

CREATE OR REPLACE FUNCTION prevent_delivery_history_modification()
RETURNS TRIGGER AS $$
BEGIN
    RAISE EXCEPTION 'delivery_history is append-only and cannot be modified';
END;
$$ language 'plpgsql';

CREATE TRIGGER trg_prevent_delivery_history_update BEFORE UPDATE ON delivery_history
    FOR EACH ROW EXECUTE FUNCTION prevent_delivery_history_modification();

CREATE TRIGGER trg_prevent_delivery_history_delete BEFORE DELETE ON delivery_history
    FOR EACH ROW EXECUTE FUNCTION prevent_delivery_history_modification();
