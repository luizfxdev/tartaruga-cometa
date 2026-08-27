CREATE TABLE delivery_history (
    id SERIAL,
    delivery_id INTEGER NOT NULL,
    previous_status delivery_status_enum,
    new_status delivery_status_enum NOT NULL,
    change_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "user" VARCHAR(100),
    observations TEXT,
    location VARCHAR(200),

    CONSTRAINT pk_delivery_history PRIMARY KEY (id),
    CONSTRAINT fk_delivery_history_delivery FOREIGN KEY (delivery_id)
        REFERENCES delivery(id) ON DELETE CASCADE
);

CREATE INDEX idx_delivery_history_delivery ON delivery_history(delivery_id);
CREATE INDEX idx_delivery_history_change_date ON delivery_history(change_date);
CREATE INDEX idx_delivery_history_new_status ON delivery_history(new_status);
