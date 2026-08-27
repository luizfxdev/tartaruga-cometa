CREATE TABLE delivery_product (
    id SERIAL,
    delivery_id INTEGER NOT NULL,
    product_id INTEGER NOT NULL,
    quantity INTEGER NOT NULL DEFAULT 1,
    unit_weight_kg DECIMAL(10,3) NOT NULL,
    unit_volume_m3 DECIMAL(10,4) NOT NULL,
    unit_value DECIMAL(12,2) NOT NULL,
    subtotal DECIMAL(12,2) NOT NULL,
    observations TEXT,

    CONSTRAINT pk_delivery_product PRIMARY KEY (id),
    CONSTRAINT fk_delivery_product_delivery FOREIGN KEY (delivery_id)
        REFERENCES delivery(id) ON DELETE CASCADE,
    CONSTRAINT fk_delivery_product_product FOREIGN KEY (product_id)
        REFERENCES product(id) ON DELETE RESTRICT,
    CONSTRAINT uq_delivery_product UNIQUE (delivery_id, product_id),
    CONSTRAINT chk_delivery_product_quantity_positive CHECK (quantity > 0),
    CONSTRAINT chk_delivery_product_unit_weight_positive CHECK (unit_weight_kg > 0),
    CONSTRAINT chk_delivery_product_unit_volume_positive CHECK (unit_volume_m3 > 0),
    CONSTRAINT chk_delivery_product_unit_value_positive CHECK (unit_value > 0),
    CONSTRAINT chk_delivery_product_subtotal_calculation CHECK (subtotal = quantity * unit_value)
);

CREATE INDEX idx_delivery_product_delivery ON delivery_product(delivery_id);
CREATE INDEX idx_delivery_product_product ON delivery_product(product_id);
