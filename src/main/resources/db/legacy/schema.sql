-- Drop existing tables and types to ensure a clean slate
DROP TABLE IF EXISTS delivery_history CASCADE;
DROP TABLE IF EXISTS delivery_product CASCADE;
DROP TABLE IF EXISTS delivery CASCADE;
DROP TABLE IF EXISTS product CASCADE;
DROP TABLE IF EXISTS address CASCADE;
DROP TABLE IF EXISTS client CASCADE;

DROP TYPE IF EXISTS person_type_enum CASCADE;
DROP TYPE IF EXISTS delivery_status_enum CASCADE;

-- Create ENUM types
CREATE TYPE person_type_enum AS ENUM ('INDIVIDUAL', 'LEGAL_ENTITY');

CREATE TYPE delivery_status_enum AS ENUM (
    'PENDING',
    'IN_TRANSIT',
    'DELIVERED',
    'CANCELED',
    'NOT_PERFORMED'
);

-- Create client table
CREATE TABLE client (
    id SERIAL PRIMARY KEY,
    person_type person_type_enum NOT NULL,
    document VARCHAR(20) NOT NULL UNIQUE,
    name VARCHAR(200) NOT NULL,
    email VARCHAR(100),
    phone VARCHAR(20),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT chk_document_length CHECK (
        (person_type = 'INDIVIDUAL' AND LENGTH(REPLACE(document, '.', '')) = 11) OR
        (person_type = 'LEGAL_ENTITY' AND LENGTH(REPLACE(document, '.', '')) = 14)
    ),
    CONSTRAINT chk_email_format CHECK (email ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$' OR email IS NULL)
);

-- Create indexes for client table
CREATE INDEX idx_client_document ON client(document);
CREATE INDEX idx_client_name ON client(name);
CREATE INDEX idx_client_person_type ON client(person_type);

-- Create address table
CREATE TABLE address (
    id SERIAL PRIMARY KEY,
    client_id INTEGER NOT NULL,
    address_type VARCHAR(20) NOT NULL,
    street VARCHAR(200) NOT NULL,
    number VARCHAR(10) NOT NULL,
    complement VARCHAR(100),
    neighborhood VARCHAR(100) NOT NULL,
    city VARCHAR(100) NOT NULL,
    state CHAR(2) NOT NULL,
    zip_code VARCHAR(10) NOT NULL,
    country VARCHAR(100) NOT NULL DEFAULT 'Brasil',
    reference TEXT,
    is_main BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    -- Adicionada a coluna 'updated_at'
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_address_client FOREIGN KEY (client_id)
        REFERENCES client(id) ON DELETE CASCADE,
    CONSTRAINT chk_address_type CHECK (address_type IN ('ORIGIN', 'DESTINATION', 'REGISTRATION')),
    CONSTRAINT chk_state_length CHECK (LENGTH(state) = 2),
    CONSTRAINT chk_zip_code_format CHECK (zip_code ~ '^\d{5}-?\d{3}$')
);

-- Create indexes for address table
CREATE INDEX idx_address_client ON address(client_id);
CREATE INDEX idx_address_zip_code ON address(zip_code);
CREATE INDEX idx_address_is_main ON address(client_id, is_main);

-- Create product table
CREATE TABLE product (
    id SERIAL PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    description TEXT,
    price DECIMAL(12,2) NOT NULL,
    weight_kg DECIMAL(10,3) NOT NULL,
    volume_m3 DECIMAL(10,4) NOT NULL,
    declared_value DECIMAL(12,2) NOT NULL,
    category VARCHAR(50),
    is_active BOOLEAN DEFAULT TRUE,
    -- Adicionada a coluna 'stock_quantity'
    stock_quantity INTEGER NOT NULL DEFAULT 0, -- Adicionado com valor padrão
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT chk_weight_positive CHECK (weight_kg > 0),
    CONSTRAINT chk_volume_positive CHECK (volume_m3 > 0),
    CONSTRAINT chk_declared_value_positive CHECK (declared_value > 0),
    CONSTRAINT chk_price_positive CHECK (price > 0),
    CONSTRAINT chk_stock_quantity_positive CHECK (stock_quantity >= 0) -- Adicionada restrição para stock_quantity
);

-- Create indexes for product table
CREATE INDEX idx_product_name ON product(name);
CREATE INDEX idx_product_is_active ON product(is_active);
CREATE INDEX idx_product_category ON product(category);

-- Create delivery table
CREATE TABLE delivery (
    id SERIAL PRIMARY KEY,
    tracking_code VARCHAR(50) UNIQUE NOT NULL,
    sender_id INTEGER NOT NULL,
    recipient_id INTEGER NOT NULL,
    origin_address_id INTEGER NOT NULL,
    destination_address_id INTEGER NOT NULL,
    status delivery_status_enum NOT NULL DEFAULT 'PENDING',
    total_value DECIMAL(12,2) NOT NULL DEFAULT 0.00,
    total_weight_kg DECIMAL(10,3) NOT NULL DEFAULT 0.000,
    total_volume_m3 DECIMAL(10,4) NOT NULL DEFAULT 0.0000,
    freight_value DECIMAL(10,2),
    observations TEXT,
    reason_not_delivered TEXT,
    creation_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    collection_date TIMESTAMP,
    shipping_date TIMESTAMP,
    delivery_date TIMESTAMP,
    cancellation_date TIMESTAMP,
    cancellation_reason TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_delivery_sender FOREIGN KEY (sender_id)
        REFERENCES client(id) ON DELETE RESTRICT,
    CONSTRAINT fk_delivery_recipient FOREIGN KEY (recipient_id)
        REFERENCES client(id) ON DELETE RESTRICT,
    CONSTRAINT fk_delivery_origin_address FOREIGN KEY (origin_address_id)
        REFERENCES address(id) ON DELETE RESTRICT,
    CONSTRAINT fk_delivery_destination_address FOREIGN KEY (destination_address_id)
        REFERENCES address(id) ON DELETE RESTRICT,
    CONSTRAINT chk_sender_different_recipient CHECK (sender_id != recipient_id),
    CONSTRAINT chk_origin_address_different_destination_address CHECK (origin_address_id != destination_address_id),
    CONSTRAINT chk_total_value_positive CHECK (total_value >= 0),
    CONSTRAINT chk_freight_value_positive CHECK (freight_value IS NULL OR freight_value >= 0),
    CONSTRAINT chk_collection_date CHECK (collection_date IS NULL OR collection_date >= creation_date),
    CONSTRAINT chk_shipping_date CHECK (shipping_date IS NULL OR shipping_date >= collection_date),
    CONSTRAINT chk_delivery_date CHECK (delivery_date IS NULL OR delivery_date >= shipping_date)
);

-- Create indexes for delivery table
CREATE UNIQUE INDEX idx_delivery_tracking_code ON delivery(tracking_code);
CREATE INDEX idx_delivery_sender ON delivery(sender_id);
CREATE INDEX idx_delivery_recipient ON delivery(recipient_id);
CREATE INDEX idx_delivery_status ON delivery(status);
CREATE INDEX idx_delivery_creation_date ON delivery(creation_date);
CREATE INDEX idx_delivery_status_date ON delivery(status, creation_date);

-- Create delivery_product table (junction table for many-to-many relationship)
CREATE TABLE delivery_product (
    id SERIAL PRIMARY KEY,
    delivery_id INTEGER NOT NULL,
    product_id INTEGER NOT NULL,
    quantity INTEGER NOT NULL DEFAULT 1,
    unit_weight_kg DECIMAL(10,3) NOT NULL,
    unit_volume_m3 DECIMAL(10,4) NOT NULL,
    unit_value DECIMAL(12,2) NOT NULL,
    subtotal DECIMAL(12,2) NOT NULL,
    observations TEXT,

    CONSTRAINT fk_delivery_product_delivery FOREIGN KEY (delivery_id)
        REFERENCES delivery(id) ON DELETE CASCADE,
    CONSTRAINT fk_delivery_product_product FOREIGN KEY (product_id)
        REFERENCES product(id) ON DELETE RESTRICT,
    CONSTRAINT chk_quantity_positive CHECK (quantity > 0),
    CONSTRAINT chk_unit_weight_positive CHECK (unit_weight_kg > 0),
    CONSTRAINT chk_unit_volume_positive CHECK (unit_volume_m3 > 0),
    CONSTRAINT chk_unit_value_positive CHECK (unit_value > 0),
    CONSTRAINT chk_subtotal_calculation CHECK (subtotal = quantity * unit_value),
    CONSTRAINT uk_delivery_product UNIQUE (delivery_id, product_id)
);

-- Create indexes for delivery_product table
CREATE INDEX idx_dp_delivery ON delivery_product(delivery_id);
CREATE INDEX idx_dp_product ON delivery_product(product_id);

-- Create delivery_history table
CREATE TABLE delivery_history (
    id SERIAL PRIMARY KEY,
    delivery_id INTEGER NOT NULL,
    previous_status delivery_status_enum,
    new_status delivery_status_enum NOT NULL,
    change_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "user" VARCHAR(100), -- "user" is a reserved keyword, so it's quoted
    observations TEXT,
    location VARCHAR(200),

    CONSTRAINT fk_delivery_history_delivery FOREIGN KEY (delivery_id)
        REFERENCES delivery(id) ON DELETE CASCADE
);

-- Create indexes for delivery_history table
CREATE INDEX idx_delivery_history_delivery ON delivery_history(delivery_id);
CREATE INDEX idx_delivery_history_date ON delivery_history(change_date);
CREATE INDEX idx_delivery_history_status ON delivery_history(new_status);

-- Function to update the 'updated_at' column automatically
CREATE OR REPLACE FUNCTION update_timestamp_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Triggers for 'updated_at' column
CREATE TRIGGER update_client_timestamp BEFORE UPDATE ON client
    FOR EACH ROW EXECUTE FUNCTION update_timestamp_column();

CREATE TRIGGER update_product_timestamp BEFORE UPDATE ON product
    FOR EACH ROW EXECUTE FUNCTION update_timestamp_column();

CREATE TRIGGER update_delivery_timestamp BEFORE UPDATE ON delivery
    FOR EACH ROW EXECUTE FUNCTION update_timestamp_column();
-- Adicionado trigger para a tabela address
CREATE TRIGGER update_address_timestamp BEFORE UPDATE ON address
    FOR EACH ROW EXECUTE FUNCTION update_timestamp_column();


-- Function to log status changes in delivery_history
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

-- Trigger to call the status change logging function
CREATE TRIGGER trigger_log_delivery_status_change AFTER UPDATE ON delivery
    FOR EACH ROW EXECUTE FUNCTION log_delivery_status_change();

-- View for complete delivery details
CREATE OR REPLACE VIEW vw_complete_deliveries AS
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

-- View for delivery statistics
CREATE OR REPLACE VIEW vw_delivery_statistics AS
SELECT
    status,
    COUNT(*) AS quantity,
    SUM(total_value) AS total_value,
    SUM(total_weight_kg) AS total_weight,
    AVG(freight_value) AS average_freight_value
FROM delivery
GROUP BY status;
