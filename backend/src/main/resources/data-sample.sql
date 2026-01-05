TRUNCATE TABLE historico_entrega CASCADE;
TRUNCATE TABLE entrega_produto CASCADE;
TRUNCATE TABLE entrega CASCADE;
TRUNCATE TABLE produto CASCADE;
TRUNCATE TABLE endereco CASCADE;
TRUNCATE TABLE cliente CASCADE;

ALTER SEQUENCE cliente_id_seq RESTART WITH 1;
ALTER SEQUENCE endereco_id_seq RESTART WITH 1;
ALTER SEQUENCE produto_id_seq RESTART WITH 1;
ALTER SEQUENCE entrega_id_seq RESTART WITH 1;
ALTER SEQUENCE entrega_produto_id_seq RESTART WITH 1;
ALTER SEQUENCE historico_entrega_id_seq RESTART WITH 1;

INSERT INTO cliente (tipo_pessoa, documento, nome, email, telefone) VALUES
('PF', '12345678901', 'João Silva Santos', 'joao.silva@email.com', '(81) 98765-4321'),
('PF', '98765432109', 'Maria Oliveira Costa', 'maria.oliveira@email.com', '(81) 99876-5432'),
('PF', '11122233344', 'Pedro Henrique Lima', 'pedro.lima@email.com', '(81) 97654-3210'),
('PF', '55566677788', 'Ana Carolina Souza', 'ana.souza@email.com', '(81) 96543-2109');

INSERT INTO cliente (tipo_pessoa, documento, nome, email, telefone) VALUES
('PJ', '12345678000190', 'TechStore Comércio de Eletrônicos LTDA', 'contato@techstore.com.br', '(81) 3456-7890'),
('PJ', '98765432000111', 'MegaBooks Distribuidora de Livros SA', 'vendas@megabooks.com.br', '(81) 3567-8901'),
('PJ', '11223344000155', 'FastFood Restaurantes LTDA', 'pedidos@fastfood.com.br', '(81) 3678-9012'),
('PJ', '55667788000199', 'ModaChic Confecções EIRELI', 'comercial@modachic.com.br', '(81) 3789-0123');

INSERT INTO endereco (id_cliente, tipo_endereco, logradouro, numero, complemento, bairro, cidade, estado, cep, is_principal) VALUES
(1, 'CADASTRAL', 'Rua das Flores', '123', 'Apt 201', 'Boa Viagem', 'Recife', 'PE', '51021-010', true),
(1, 'DESTINO', 'Avenida Domingos Ferreira', '456', NULL, 'Boa Viagem', 'Recife', 'PE', '51020-030', false),
(2, 'CADASTRAL', 'Rua do Príncipe', '789', 'Casa', 'Boa Vista', 'Recife', 'PE', '50050-410', true),
(3, 'CADASTRAL', 'Avenida Agamenon Magalhães', '321', 'Bloco B', 'Espinheiro', 'Recife', 'PE', '52020-000', true),
(4, 'CADASTRAL', 'Rua da Aurora', '654', 'Sala 302', 'Santo Amaro', 'Recife', 'PE', '50040-090', true);

INSERT INTO endereco (id_cliente, tipo_endereco, logradouro, numero, complemento, bairro, cidade, estado, cep, is_principal) VALUES
(5, 'ORIGEM', 'Avenida Bernardo Vieira de Melo', '1500', 'Galpão 5', 'Piedade', 'Jaboatão dos Guararapes', 'PE', '54410-000', true),
(6, 'ORIGEM', 'Rua da Concórdia', '234', 'Armazém A', 'São José', 'Recife', 'PE', '50020-100', true),
(7, 'ORIGEM', 'Avenida Caxangá', '5678', 'Loja 10', 'Iputinga', 'Recife', 'PE', '50670-000', true),
(8, 'ORIGEM', 'Rua do Hospício', '890', NULL, 'Boa Vista', 'Recife', 'PE', '50050-080', true);

INSERT INTO produto (nome, descricao, peso_kg, volume_m3, valor_declarado, categoria, ativo) VALUES
('Notebook Dell Inspiron 15', 'Notebook i5, 8GB RAM, 256GB SSD', 2.100, 0.0150, 3500.00, 'Eletrônicos', true),
('Smartphone Samsung Galaxy S23', 'Smartphone 5G, 128GB', 0.180, 0.0008, 2800.00, 'Eletrônicos', true),
('Smart TV LG 50 Polegadas', 'TV 4K Ultra HD', 12.500, 0.1200, 2200.00, 'Eletrônicos', true),
('Kit 10 Livros de Romance', 'Coleção de romances best-sellers', 3.500, 0.0250, 350.00, 'Livros', true),
('Kit 5 Livros Técnicos Java', 'Livros de programação Java avançado', 4.200, 0.0180, 480.00, 'Livros', true),
('Caixa de Pizza Congelada (10 unidades)', 'Pizza margherita congelada', 5.000, 0.0350, 180.00, 'Alimentos', true),
('Kit Ingredientes para Hambúrguer', 'Carne, pão, queijo, vegetais', 8.500, 0.0450, 120.00, 'Alimentos', true),
('Vestido Festa Longo', 'Vestido de festa em tecido nobre', 0.600, 0.0120, 450.00, 'Roupas', true),
('Kit 3 Camisetas Básicas', 'Camisetas 100% algodão', 0.800, 0.0080, 89.90, 'Roupas', true),
('Cadeira de Escritório Ergonômica', 'Cadeira com apoio lombar ajustável', 15.000, 0.2500, 850.00, 'Móveis', true);

INSERT INTO entrega (
    codigo_rastreio, id_remetente, id_destinatario,
    id_endereco_origem, id_endereco_destino,
    status, valor_total, peso_total_kg, volume_total_m3, valor_frete,
    data_criacao, data_coleta, data_envio, data_entrega
) VALUES (
    'TC2024110001', 5, 1,
    5, 1,
    'ENTREGUE', 3500.00, 2.100, 0.0150, 45.00,
    '2024-11-20 10:30:00', '2024-11-20 14:00:00', '2024-11-20 15:30:00', '2024-11-21 11:20:00'
);

INSERT INTO entrega (
    codigo_rastreio, id_remetente, id_destinatario,
    id_endereco_origem, id_endereco_destino,
    status, valor_total, peso_total_kg, volume_total_m3, valor_frete,
    data_criacao, data_coleta, data_envio
) VALUES (
    'TC2024110002', 6, 2,
    6, 3,
    'EM_TRANSITO', 830.00, 7.700, 0.0430, 35.00,
    '2024-11-22 09:15:00', '2024-11-22 13:45:00', '2024-11-22 16:00:00'
);

INSERT INTO entrega (
    codigo_rastreio, id_remetente, id_destinatario,
    id_endereco_origem, id_endereco_destino,
    status, valor_total, peso_total_kg, volume_total_m3, valor_frete,
    data_criacao
) VALUES (
    'TC2024110003', 7, 3,
    7, 4,
    'PENDENTE', 300.00, 13.500, 0.0800, 50.00,
    '2024-11-26 08:00:00'
);

INSERT INTO entrega (
    codigo_rastreio, id_remetente, id_destinatario,
    id_endereco_origem, id_endereco_destino,
    status, valor_total, peso_total_kg, volume_total_m3, valor_frete,
    data_criacao, data_coleta, data_envio, data_entrega
) VALUES (
    'TC2024110004', 8, 4,
    8, 5,
    'ENTREGUE', 539.90, 1.400, 0.0200, 25.00,
    '2024-11-18 11:20:00', '2024-11-18 15:00:00', '2024-11-18 16:30:00', '2024-11-19 10:45:00'
);

INSERT INTO entrega (
    codigo_rastreio, id_remetente, id_destinatario,
    id_endereco_origem, id_endereco_destino,
    status, valor_total, peso_total_kg, volume_total_m3, valor_frete,
    data_criacao, data_cancelamento, motivo_cancelamento
) VALUES (
    'TC2024110005', 5, 2,
    5, 3,
    'CANCELADA', 5000.00, 14.600, 0.1350, 75.00,
    '2024-11-15 14:30:00', '2024-11-16 09:00:00', 'Cliente solicitou cancelamento - comprou em loja física'
);

INSERT INTO entrega_produto (id_entrega, id_produto, quantidade, peso_unitario_kg, volume_unitario_m3, valor_unitario, subtotal) VALUES
(1, 1, 1, 2.100, 0.0150, 3500.00, 3500.00);

INSERT INTO entrega_produto (id_entrega, id_produto, quantidade, peso_unitario_kg, volume_unitario_m3, valor_unitario, subtotal) VALUES
(2, 4, 1, 3.500, 0.0250, 350.00, 350.00),
(2, 5, 1, 4.200, 0.0180, 480.00, 480.00);

INSERT INTO entrega_produto (id_entrega, id_produto, quantidade, peso_unitario_kg, volume_unitario_m3, valor_unitario, subtotal) VALUES
(3, 6, 1, 5.000, 0.0350, 180.00, 180.00),
(3, 7, 1, 8.500, 0.0450, 120.00, 120.00);

INSERT INTO entrega_produto (id_entrega, id_produto, quantidade, peso_unitario_kg, volume_unitario_m3, valor_unitario, subtotal) VALUES
(4, 8, 1, 0.600, 0.0120, 450.00, 450.00),
(4, 9, 1, 0.800, 0.0080, 89.90, 89.90);

INSERT INTO entrega_produto (id_entrega, id_produto, quantidade, peso_unitario_kg, volume_unitario_m3, valor_unitario, subtotal) VALUES
(5, 1, 1, 2.100, 0.0150, 3500.00, 3500.00),
(5, 3, 1, 12.500, 0.1200, 2200.00, 2200.00);

INSERT INTO historico_entrega (id_entrega, status_anterior, status_novo, data_mudanca, usuario, observacoes, localizacao) VALUES
(1, NULL, 'PENDENTE', '2024-11-20 10:30:00', 'system', 'Entrega criada no sistema', 'Jaboatão dos Guararapes - PE'),
(1, 'PENDENTE', 'EM_TRANSITO', '2024-11-20 15:30:00', 'motorista_001', 'Saiu para entrega', 'Centro de Distribuição Piedade'),
(1, 'EM_TRANSITO', 'ENTREGUE', '2024-11-21 11:20:00', 'motorista_001', 'Entregue ao destinatário', 'Boa Viagem - Recife');

INSERT INTO historico_entrega (id_entrega, status_anterior, status_novo, data_mudanca, usuario, observacoes, localizacao) VALUES
(2, NULL, 'PENDENTE', '2024-11-22 09:15:00', 'system', 'Entrega criada no sistema', 'São José - Recife'),
(2, 'PENDENTE', 'EM_TRANSITO', '2024-11-22 16:00:00', 'motorista_002', 'Em rota para entrega', 'BR-101 Sul');

INSERT INTO historico_entrega (id_entrega, status_anterior, status_novo, data_mudanca, usuario, observacoes, localizacao) VALUES
(3, NULL, 'PENDENTE', '2024-11-26 08:00:00', 'system', 'Entrega criada no sistema - aguardando coleta', 'Iputinga - Recife');

INSERT INTO historico_entrega (id_entrega, status_anterior, status_novo, data_mudanca, usuario, observacoes, localizacao) VALUES
(4, NULL, 'PENDENTE', '2024-11-18 11:20:00', 'system', 'Entrega criada no sistema', 'Boa Vista - Recife'),
(4, 'PENDENTE', 'EM_TRANSITO', '2024-11-18 16:30:00', 'motorista_003', 'Saiu para entrega', 'Centro - Recife'),
(4, 'EM_TRANSITO', 'ENTREGUE', '2024-11-19 10:45:00', 'motorista_003', 'Entregue com sucesso', 'Santo Amaro - Recife');

INSERT INTO historico_entrega (id_entrega, status_anterior, status_novo, data_mudanca, usuario, observacoes, localizacao) VALUES
(5, NULL, 'PENDENTE', '2024-11-15 14:30:00', 'system', 'Entrega criada no sistema', 'Piedade - Jaboatão'),
(5, 'PENDENTE', 'CANCELADA', '2024-11-16 09:00:00', 'atendente_maria', 'Cancelado a pedido do cliente', 'Aguardando coleta');
