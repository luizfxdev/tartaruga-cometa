CREATE TABLE delivery (
    id SERIAL,
    tracking_code VARCHAR(50) NOT NULL,
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

    CONSTRAINT pk_delivery PRIMARY KEY (id),
    CONSTRAINT uq_delivery_tracking_code UNIQUE (tracking_code),
    CONSTRAINT fk_delivery_sender FOREIGN KEY (sender_id)
        REFERENCES client(id) ON DELETE RESTRICT,
    CONSTRAINT fk_delivery_recipient FOREIGN KEY (recipient_id)
        REFERENCES client(id) ON DELETE RESTRICT,
    CONSTRAINT fk_delivery_origin_address FOREIGN KEY (origin_address_id)
        REFERENCES address(id) ON DELETE RESTRICT,
    CONSTRAINT fk_delivery_destination_address FOREIGN KEY (destination_address_id)
        REFERENCES address(id) ON DELETE RESTRICT,
    CONSTRAINT chk_delivery_sender_different_recipient CHECK (sender_id != recipient_id),
    CONSTRAINT chk_delivery_addresses_different CHECK (origin_address_id != destination_address_id),
    CONSTRAINT chk_delivery_total_value_positive CHECK (total_value >= 0),
    CONSTRAINT chk_delivery_freight_value_positive CHECK (freight_value IS NULL OR freight_value > 0),
    CONSTRAINT chk_delivery_chronology CHECK (
        (collection_date IS NULL OR collection_date >= creation_date) AND
        (shipping_date IS NULL OR shipping_date >= collection_date) AND
        (delivery_date IS NULL OR delivery_date >= shipping_date)
    )
);

CREATE INDEX idx_delivery_tracking_code ON delivery(tracking_code);
CREATE INDEX idx_delivery_sender ON delivery(sender_id);
CREATE INDEX idx_delivery_recipient ON delivery(recipient_id);
CREATE INDEX idx_delivery_status ON delivery(status);
CREATE INDEX idx_delivery_status_creation_date ON delivery(status, creation_date);
