-- DEVELOPMENT SEED DATA - NEVER RUN IN PRODUCTION
-- This file populates the database with sample data for local development only

-- Clientes - Pessoa Física
INSERT INTO client (person_type, document, name, email, phone) VALUES
('INDIVIDUAL', '12345678901', 'João Silva', 'joao@example.com', '1133334444'),
('INDIVIDUAL', '98765432109', 'Maria Santos', 'maria@example.com', '1144445555');

-- Clientes - Pessoa Jurídica
INSERT INTO client (person_type, document, name, email, phone) VALUES
('LEGAL_ENTITY', '12345678000190', 'Transportadora ABC Ltda', 'contato@transportadora.com.br', '1155556666');

-- Endereços
INSERT INTO address (client_id, address_type, street, number, neighborhood, city, state, zip_code, country, is_main) VALUES
(1, 'REGISTRATION', 'Rua A', '100', 'Centro', 'São Paulo', 'SP', '01310100', 'Brasil', true),
(2, 'REGISTRATION', 'Rua B', '200', 'Vila Mariana', 'São Paulo', 'SP', '04008020', 'Brasil', true),
(3, 'ORIGIN', 'Avenida C', '300', 'Brooklin', 'São Paulo', 'SP', '04571130', 'Brasil', true);

-- Produtos
INSERT INTO product (name, description, price, weight_kg, volume_m3, declared_value, category, is_active, stock_quantity) VALUES
('Livro', 'Livro técnico de 500 páginas', 89.90, 0.800, 0.0005, 89.90, 'Livros', true, 50),
('Notebook', 'Notebook 15 polegadas', 3500.00, 2.500, 0.0030, 3500.00, 'Eletrônicos', true, 10),
('Mouse', 'Mouse óptico USB', 45.50, 0.100, 0.0001, 45.50, 'Periféricos', true, 100),
('Teclado', 'Teclado mecânico 104 teclas', 350.00, 0.900, 0.0015, 350.00, 'Periféricos', true, 30),
('Monitor', 'Monitor 24 polegadas Full HD', 899.00, 3.500, 0.0050, 899.00, 'Monitores', true, 20);

-- Entregas
INSERT INTO delivery (tracking_code, sender_id, recipient_id, origin_address_id, destination_address_id, status, total_value, total_weight_kg, total_volume_m3, freight_value, creation_date) VALUES
('TRAC001', 1, 2, 1, 2, 'PENDING', 450.40, 3.400, 0.0050, 50.00, NOW()),
('TRAC002', 3, 1, 3, 1, 'IN_TRANSIT', 3850.00, 3.500, 0.0050, 150.00, NOW() - INTERVAL '2 days'),
('TRAC003', 2, 3, 2, 3, 'DELIVERED', 1248.50, 4.400, 0.0065, 100.00, NOW() - INTERVAL '5 days');

-- Produtos na entrega 1
INSERT INTO delivery_product (delivery_id, product_id, quantity, unit_weight_kg, unit_volume_m3, unit_value, subtotal) VALUES
(1, 1, 1, 0.800, 0.0005, 89.90, 89.90),
(1, 3, 2, 0.100, 0.0001, 45.50, 91.00),
(1, 4, 1, 0.900, 0.0015, 350.00, 350.00);

-- Produtos na entrega 2
INSERT INTO delivery_product (delivery_id, product_id, quantity, unit_weight_kg, unit_volume_m3, unit_value, subtotal) VALUES
(2, 2, 1, 2.500, 0.0030, 3500.00, 3500.00);

-- Produtos na entrega 3
INSERT INTO delivery_product (delivery_id, product_id, quantity, unit_weight_kg, unit_volume_m3, unit_value, subtotal) VALUES
(3, 5, 1, 3.500, 0.0050, 899.00, 899.00),
(3, 3, 1, 0.100, 0.0001, 45.50, 45.50),
(3, 1, 1, 0.800, 0.0005, 89.90, 89.90);

-- Histórico de entregas
INSERT INTO delivery_history (delivery_id, previous_status, new_status, location, observations) VALUES
(1, NULL, 'PENDING', 'São Paulo - SP', 'Entrega criada'),
(2, NULL, 'PENDING', 'São Paulo - SP', 'Entrega criada'),
(2, 'PENDING', 'IN_TRANSIT', 'São Paulo - SP', 'Saiu para entrega'),
(3, NULL, 'PENDING', 'São Paulo - SP', 'Entrega criada'),
(3, 'PENDING', 'IN_TRANSIT', 'São Paulo - SP', 'Saiu para entrega'),
(3, 'IN_TRANSIT', 'DELIVERED', 'São Paulo - SP', 'Entregue com sucesso');
