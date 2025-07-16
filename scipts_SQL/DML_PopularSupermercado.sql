-- Seleciona o banco de dados para garantir que os dados sejam inseridos no lugar certo.
USE supermercado_db;

-- --- SEÇÃO DE LIMPEZA (CUIDADO: APAGA OS DADOS ANTIGOS) ---
-- Desativa a verificação de chave estrangeira para permitir a limpeza sem erros de ordem.
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE fornecedores;
TRUNCATE TABLE produtos;
TRUNCATE TABLE clientes;
TRUNCATE TABLE vendas;
TRUNCATE TABLE itens_venda;
TRUNCATE TABLE pedidos_compra;
TRUNCATE TABLE itens_pedido_compra;
TRUNCATE TABLE ajustes_estoque;
-- Reativa a verificação de chave estrangeira.
SET FOREIGN_KEY_CHECKS = 1;


-- =====================================================================
-- --- SEÇÃO DE INSERÇÃO DE DADOS ---
-- =====================================================================

-- 1. FORNECEDORES
INSERT INTO fornecedores (id, nome, cnpj, contato) VALUES
(1, 'Super Distribuidora Brasil', '11.222.333/0001-44', 'Carlos Pereira'),
(2, 'Laticínios Sabor da Manhã', '44.555.666/0001-77', 'Ana Julia'),
(3, 'Hortifruti Vida Fresca', '77.888.999/0001-00', 'Mariana Lima'),
(4, 'Bebidas & Cia', '12.345.678/0001-99', 'Ricardo Alves');

-- 2. PRODUTOS
INSERT INTO produtos (codigo_barras, nome, preco_venda, preco_compra, quantidade_estoque, estoque_minimo, categoria, id_fornecedor) VALUES
('7890001112223', 'Arroz Branco Tipo 1 (5kg)', 25.50, 18.00, 150, 20, 'Mercearia', 1),
('7890004445556', 'Feijão Carioca (1kg)', 8.99, 6.50, 200, 30, 'Mercearia', 1),
('7890007778889', 'Leite Integral UHT (1L)', 4.79, 3.20, 300, 50, 'Laticínios', 2),
('7890001234567', 'Queijo Minas Frescal (500g)', 18.90, 12.50, 80, 15, 'Laticínios', 2),
('7890009876543', 'Maçã Fuji (kg)', 9.98, 7.00, 45, 10, 'Hortifruti', 3),
('1234567890123', 'Refrigerante Cola (2L)', 7.50, 5.00, 250, 40, 'Bebidas', 4),
('1112223334445', 'Café em Pó Tradicional (500g)', 15.80, 11.00, 120, 25, 'Mercearia', 1),
('5556667778889', 'Iogurte Natural (170g)', 3.29, 2.10, 15, 20, 'Laticínios', 2); -- Produto com estoque baixo

-- 3. CLIENTES
INSERT INTO clientes (id, nome, cpf, email) VALUES
(1, 'João da Silva', '111.222.333-44', 'joao.silva@email.com'),
(2, 'Maria Oliveira', '555.666.777-88', 'maria.oliveira@email.com');

-- 4. VENDAS (Distribuídas ao longo do tempo para relatórios)
-- Vendas da semana passada
INSERT INTO vendas (id_venda, data_venda, valor_total, forma_pagamento, id_cliente) VALUES
(1, '2025-06-02 10:30:00', 34.49, 'Cartão de Débito', 1),
(2, '2025-06-03 15:00:00', 12.29, 'PIX', NULL),
-- Vendas desta semana
(3, '2025-06-06 18:45:00', 50.30, 'Cartão de Crédito', 2),
(4, '2025-06-07 11:10:00', 8.99, 'Dinheiro', NULL),
-- Vendas do mês anterior
(5, '2025-05-15 09:00:00', 84.78, 'Cartão de Crédito', 1);

-- 5. ITENS DAS VENDAS
-- Venda 1
INSERT INTO itens_venda (id_venda, codigo_barras_produto, quantidade, preco_unitario) VALUES
(1, '7890001112223', 1, 25.50), -- Arroz
(1, '7890004445556', 1, 8.99);  -- Feijão
-- Venda 2
INSERT INTO itens_venda (id_venda, codigo_barras_produto, quantidade, preco_unitario) VALUES
(2, '7890007778889', 2, 4.79),  -- Leite
(2, '5556667778889', 1, 3.29);   -- Iogurte
-- Venda 3
INSERT INTO itens_venda (id_venda, codigo_barras_produto, quantidade, preco_unitario) VALUES
(3, '7890001234567', 1, 18.90), -- Queijo
(3, '1234567890123', 2, 7.50),  -- Refrigerante
(3, '1112223334445', 1, 15.80); -- Café
-- Venda 4
INSERT INTO itens_venda (id_venda, codigo_barras_produto, quantidade, preco_unitario) VALUES
(4, '7890004445556', 1, 8.99);  -- Feijão
-- Venda 5
INSERT INTO itens_venda (id_venda, codigo_barras_produto, quantidade, preco_unitario) VALUES
(5, '7890001112223', 2, 25.50), -- Arroz
(5, '1234567890123', 3, 7.50),  -- Refrigerante
(5, '7890007778889', 1, 4.79),   -- Leite
(5, '7890004445556', 1, 8.99);  -- Feijão

-- 6. PEDIDOS DE COMPRA
INSERT INTO pedidos_compra (id_pedido, id_fornecedor, data_pedido, status) VALUES
(1, 1, '2025-05-20', 'Recebido'),
(2, 2, '2025-06-05', 'Pendente');

-- 7. ITENS DOS PEDIDOS DE COMPRA
-- Pedido 1
INSERT INTO itens_pedido_compra (id_pedido, codigo_barras_produto, quantidade_pedida, preco_compra_unitario) VALUES
(1, '7890001112223', 50, 18.00), -- Arroz
(1, '1112223334445', 100, 11.00); -- Café
-- Pedido 2
INSERT INTO itens_pedido_compra (id_pedido, codigo_barras_produto, quantidade_pedida, preco_compra_unitario) VALUES
(2, '5556667778889', 120, 2.10); -- Iogurte

-- 8. AJUSTES DE ESTOQUE
INSERT INTO ajustes_estoque (codigo_barras_produto, quantidade_ajustada, motivo) VALUES
('7890009876543', -5, 'Perda/Avaria de mercadoria'), -- 5kg de maçã perdidos
('7890004445556', 10, 'Encontrado no depósito (contagem de balanço)'); -- 10kg de feijão encontrados
