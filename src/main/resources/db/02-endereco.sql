CREATE TABLE address (
    id SERIAL,
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
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_address PRIMARY KEY (id),
    CONSTRAINT fk_address_client FOREIGN KEY (client_id)
        REFERENCES client(id) ON DELETE CASCADE,
    CONSTRAINT chk_address_type CHECK (address_type IN ('ORIGIN', 'DESTINATION', 'REGISTRATION')),
    CONSTRAINT chk_address_state_length CHECK (LENGTH(state) = 2),
    CONSTRAINT chk_address_zip_code_format CHECK (zip_code ~ '^\d{5}-?\d{3}$')
);

CREATE INDEX idx_address_client ON address(client_id);
CREATE INDEX idx_address_is_main ON address(client_id, is_main);
