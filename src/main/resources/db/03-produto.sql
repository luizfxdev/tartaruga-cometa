CREATE TABLE product (
    id SERIAL,
    name VARCHAR(200) NOT NULL,
    description TEXT,
    price DECIMAL(12,2) NOT NULL,
    weight_kg DECIMAL(10,3) NOT NULL,
    volume_m3 DECIMAL(10,4) NOT NULL,
    declared_value DECIMAL(12,2) NOT NULL,
    category VARCHAR(50),
    is_active BOOLEAN DEFAULT TRUE,
    stock_quantity INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_product PRIMARY KEY (id),
    CONSTRAINT chk_product_weight_positive CHECK (weight_kg > 0),
    CONSTRAINT chk_product_volume_positive CHECK (volume_m3 > 0),
    CONSTRAINT chk_product_declared_value_positive CHECK (declared_value > 0),
    CONSTRAINT chk_product_price_positive CHECK (price > 0),
    CONSTRAINT chk_product_stock_quantity_positive CHECK (stock_quantity >= 0)
);

CREATE INDEX idx_product_is_active ON product(is_active);
CREATE INDEX idx_product_category ON product(category);
