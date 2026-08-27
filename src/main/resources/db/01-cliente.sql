CREATE TABLE client (
    id SERIAL,
    person_type person_type_enum NOT NULL,
    document VARCHAR(20) NOT NULL,
    name VARCHAR(200) NOT NULL,
    email VARCHAR(100),
    phone VARCHAR(20),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_client PRIMARY KEY (id),
    CONSTRAINT uq_client_document UNIQUE (document),
    CONSTRAINT chk_client_document_length CHECK (
        (person_type = 'INDIVIDUAL' AND LENGTH(REPLACE(document, '.', '')) = 11) OR
        (person_type = 'LEGAL_ENTITY' AND LENGTH(REPLACE(document, '.', '')) = 14)
    ),
    CONSTRAINT chk_client_email_format CHECK (email ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$' OR email IS NULL)
);

CREATE INDEX idx_client_document ON client(document);
CREATE INDEX idx_client_person_type ON client(person_type);
