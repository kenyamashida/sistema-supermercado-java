-- =====================================================================
-- SCRIPT COMPLETO DO BANCO DE DADOS PARA O SISTEMA DE SUPERMERCADO
-- Versão: 2.0
-- Funcionalidades suportadas: Todas as do case.
-- =====================================================================

-- Cria o banco de dados caso ele ainda não exista.
CREATE DATABASE IF NOT EXISTS supermercado_db;

-- Seleciona o banco de dados para usar nos comandos seguintes.
USE supermercado_db;

-- --- SEÇÃO DE LIMPEZA (CUIDADO: APAGA AS TABELAS EXISTENTES) ---
-- Remove as tabelas na ordem inversa de criação para evitar erros de chave estrangeira.
DROP TABLE IF EXISTS itens_pedido_compra;
DROP TABLE IF EXISTS pedidos_compra;
DROP TABLE IF EXISTS ajustes_estoque;
DROP TABLE IF EXISTS itens_venda;
DROP TABLE IF EXISTS vendas;
DROP TABLE IF EXISTS produtos;
DROP TABLE IF EXISTS fornecedores;
DROP TABLE IF EXISTS clientes;

-- =====================================================================
-- --- SEÇÃO DE CRIAÇÃO DAS TABELAS ---
-- =====================================================================

-- Tabela de Fornecedores
-- Armazena informações sobre as empresas que fornecem os produtos.
CREATE TABLE fornecedores (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    cnpj VARCHAR(20) UNIQUE,
    endereco VARCHAR(255),
    contato VARCHAR(100)
);

-- Tabela de Clientes
-- Armazena informações dos clientes para vincular a vendas e gerar relatórios.
CREATE TABLE clientes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    cpf VARCHAR(14) UNIQUE, -- CPF pode ser usado para identificar clientes em programas de fidelidade.
    email VARCHAR(100),
    telefone VARCHAR(20)
);

-- Tabela de Produtos
-- O coração do sistema, armazena todos os itens à venda.
CREATE TABLE produtos (
    codigo_barras VARCHAR(100) PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    preco_venda DECIMAL(10, 2) NOT NULL,
    preco_compra DECIMAL(10, 2) NOT NULL,
    quantidade_estoque INT NOT NULL,
    estoque_minimo INT NOT NULL,
    categoria VARCHAR(100),
    id_fornecedor INT,
    FOREIGN KEY (id_fornecedor) REFERENCES fornecedores(id)
);

-- Tabela de Vendas
-- Registra cada transação. Agora com a possibilidade de vincular um cliente.
CREATE TABLE vendas (
    id_venda INT AUTO_INCREMENT PRIMARY KEY,
    data_venda TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    valor_total DECIMAL(10, 2) NOT NULL,
    forma_pagamento VARCHAR(50) NOT NULL,
    id_cliente INT, -- Chave estrangeira para o cliente (pode ser NULA, pois nem toda venda é identificada).
    FOREIGN KEY (id_cliente) REFERENCES clientes(id)
);

-- Tabela de Itens da Venda
-- Detalha quais produtos e em que quantidade foram vendidos em cada transação.
CREATE TABLE itens_venda (
    id_item_venda INT AUTO_INCREMENT PRIMARY KEY,
    id_venda INT NOT NULL,
    codigo_barras_produto VARCHAR(100) NOT NULL,
    quantidade INT NOT NULL,
    preco_unitario DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (id_venda) REFERENCES vendas(id_venda) ON DELETE CASCADE, -- Se uma venda é deletada, seus itens também são.
    FOREIGN KEY (codigo_barras_produto) REFERENCES produtos(codigo_barras)
);

-- Tabela de Pedidos de Compra
-- (Funcionalidade 4: Gestão de Fornecedores)
-- Registra os pedidos feitos aos fornecedores para reposição de estoque.
CREATE TABLE pedidos_compra (
    id_pedido INT AUTO_INCREMENT PRIMARY KEY,
    id_fornecedor INT NOT NULL,
    data_pedido DATE NOT NULL,
    data_recebimento DATE, -- Preenchido quando o pedido chega.
    valor_total_pedido DECIMAL(10, 2),
    status VARCHAR(50) DEFAULT 'Pendente', -- Ex: Pendente, Recebido, Cancelado
    FOREIGN KEY (id_fornecedor) REFERENCES fornecedores(id)
);

-- Tabela de Itens do Pedido de Compra
-- Detalha os produtos e quantidades de cada pedido de compra.
CREATE TABLE itens_pedido_compra (
    id_item_pedido INT AUTO_INCREMENT PRIMARY KEY,
    id_pedido INT NOT NULL,
    codigo_barras_produto VARCHAR(100) NOT NULL,
    quantidade_pedida INT NOT NULL,
    preco_compra_unitario DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (id_pedido) REFERENCES pedidos_compra(id_pedido) ON DELETE CASCADE,
    FOREIGN KEY (codigo_barras_produto) REFERENCES produtos(codigo_barras)
);

-- Tabela de Ajustes de Estoque
-- (Funcionalidade 1: Controle de Estoque)
-- Essencial para auditoria. Registra ajustes manuais no estoque.
CREATE TABLE ajustes_estoque (
    id_ajuste INT AUTO_INCREMENT PRIMARY KEY,
    codigo_barras_produto VARCHAR(100) NOT NULL,
    data_ajuste TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    quantidade_ajustada INT NOT NULL, -- Positivo para entrada, negativo para saída.
    motivo VARCHAR(255) NOT NULL, -- Ex: 'Contagem de balanço', 'Perda/Avaria', 'Devolução de cliente'.
    FOREIGN KEY (codigo_barras_produto) REFERENCES produtos(codigo_barras)
);

