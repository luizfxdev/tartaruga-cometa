-- DADOS DE DESENVOLVIMENTO - NUNCA RODAR EM PRODUCAO
-- Este arquivo popula o banco com dados de exemplo apenas para desenvolvimento local

-- Clientes - Pessoa Física
INSERT INTO cliente (tipo_pessoa, documento, nome, email, telefone) VALUES
('PF', '12345678901', 'João Silva', 'joao@example.com', '1133334444'),
('PF', '98765432109', 'Maria Santos', 'maria@example.com', '1144445555');

-- Clientes - Pessoa Jurídica
INSERT INTO cliente (tipo_pessoa, documento, nome, email, telefone) VALUES
('PJ', '12345678000190', 'Transportadora ABC Ltda', 'contato@transportadora.com.br', '1155556666');

-- Endereços
INSERT INTO endereco (id_cliente, tipo_endereco, logradouro, numero, bairro, cidade, estado, cep, pais, is_principal) VALUES
(1, 'CADASTRO', 'Rua A', '100', 'Centro', 'São Paulo', 'SP', '01310100', 'Brasil', true),
(2, 'CADASTRO', 'Rua B', '200', 'Vila Mariana', 'São Paulo', 'SP', '04008020', 'Brasil', true),
(3, 'ORIGEM', 'Avenida C', '300', 'Brooklin', 'São Paulo', 'SP', '04571130', 'Brasil', true);

-- Produtos
INSERT INTO produto (nome, descricao, preco, peso_kg, volume_m3, valor_declarado, categoria, ativo, estoque) VALUES
('Livro', 'Livro técnico de 500 páginas', 89.90, 0.800, 0.0005, 89.90, 'Livros', true, 50),
('Notebook', 'Notebook 15 polegadas', 3500.00, 2.500, 0.0030, 3500.00, 'Eletrônicos', true, 10),
('Mouse', 'Mouse óptico USB', 45.50, 0.100, 0.0001, 45.50, 'Periféricos', true, 100),
('Teclado', 'Teclado mecânico 104 teclas', 350.00, 0.900, 0.0015, 350.00, 'Periféricos', true, 30),
('Monitor', 'Monitor 24 polegadas Full HD', 899.00, 3.500, 0.0050, 899.00, 'Monitores', true, 20);

-- Entregas
INSERT INTO entrega (codigo_rastreio, id_remetente, id_destinatario, id_endereco_origem, id_endereco_destino, status, valor_total, peso_total_kg, volume_total_m3, valor_frete, data_criacao) VALUES
('TRAC001', 1, 2, 1, 2, 'PENDENTE', 450.40, 3.400, 0.0050, 50.00, NOW()),
('TRAC002', 3, 1, 3, 1, 'EM_TRANSITO', 3850.00, 3.500, 0.0050, 150.00, NOW() - INTERVAL '2 days'),
('TRAC003', 2, 3, 2, 3, 'ENTREGUE', 1248.50, 4.400, 0.0065, 100.00, NOW() - INTERVAL '5 days');

-- Produtos na entrega 1
INSERT INTO entrega_produto (id_entrega, id_produto, quantidade, peso_unitario_kg, volume_unitario_m3, valor_unitario, subtotal) VALUES
(1, 1, 1, 0.800, 0.0005, 89.90, 89.90),
(1, 3, 2, 0.100, 0.0001, 45.50, 91.00),
(1, 4, 1, 0.900, 0.0015, 350.00, 350.00);

-- Produtos na entrega 2
INSERT INTO entrega_produto (id_entrega, id_produto, quantidade, peso_unitario_kg, volume_unitario_m3, valor_unitario, subtotal) VALUES
(2, 2, 1, 2.500, 0.0030, 3500.00, 3500.00);

-- Produtos na entrega 3
INSERT INTO entrega_produto (id_entrega, id_produto, quantidade, peso_unitario_kg, volume_unitario_m3, valor_unitario, subtotal) VALUES
(3, 5, 1, 3.500, 0.0050, 899.00, 899.00),
(3, 3, 1, 0.100, 0.0001, 45.50, 45.50),
(3, 1, 1, 0.800, 0.0005, 89.90, 89.90);

-- Histórico de entregas
INSERT INTO historico_entrega (id_entrega, status_anterior, status_novo, localizacao, observacoes) VALUES
(1, NULL, 'PENDENTE', 'São Paulo - SP', 'Entrega criada'),
(2, NULL, 'PENDENTE', 'São Paulo - SP', 'Entrega criada'),
(2, 'PENDENTE', 'EM_TRANSITO', 'São Paulo - SP', 'Saiu para entrega'),
(3, NULL, 'PENDENTE', 'São Paulo - SP', 'Entrega criada'),
(3, 'PENDENTE', 'EM_TRANSITO', 'São Paulo - SP', 'Saiu para entrega'),
(3, 'EM_TRANSITO', 'ENTREGUE', 'São Paulo - SP', 'Entregue com sucesso');
