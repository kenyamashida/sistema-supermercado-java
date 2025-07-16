package dao;

import model.ItemVenda;
import model.Venda;
import java.sql.*;

/**
 * Objeto de Acesso a Dados (DAO) para a entidade {@link Venda}.
 * <p>
 * Responsável pela operação crítica de registrar uma venda no banco de dados,
 * utilizando uma transação para garantir a consistência dos dados (ACID).
 * A transação assegura que a venda, seus itens e a atualização do estoque
 * ocorram como uma única operação atômica.
 *
 * @author kenyamashida
 */
public class VendaDAO {

    /**
     * Salva uma venda completa no banco de dados de forma transacional.
     * <p>
     * 1. Insere o registro principal na tabela 'vendas'.<br>
     * 2. Obtém o ID gerado para a nova venda.<br>
     * 3. Itera sobre cada {@link ItemVenda}, inserindo-o na tabela 'itens_venda'
     * e atualizando o estoque do produto correspondente na tabela 'produtos'.<br>
     * 4. Se todas as operações forem bem-sucedidas, efetiva a transação (commit).
     * Caso contrário, desfaz todas as alterações (rollback).
     *
     * @param venda O objeto {@link Venda} a ser persistido, contendo todos os seus itens.
     * @return {@code true} se a transação foi concluída com sucesso, {@code false} caso contrário.
     */
    public boolean registrarVenda(Venda venda) {
        String sqlVenda = "INSERT INTO vendas (valor_total, forma_pagamento) VALUES (?, ?)";
        String sqlItem = "INSERT INTO itens_venda (id_venda, codigo_barras_produto, quantidade, preco_unitario) VALUES (?, ?, ?, ?)";
        String sqlUpdateEstoque = "UPDATE produtos SET quantidade_estoque = quantidade_estoque - ? WHERE codigo_barras = ?";

        Connection conn = null;
        try {
            conn = ConexaoMySQL.getConexao();
            conn.setAutoCommit(false); // Inicia a transação

            try (PreparedStatement stmtVenda = conn.prepareStatement(sqlVenda, Statement.RETURN_GENERATED_KEYS)) {
                stmtVenda.setDouble(1, venda.getTotalVenda());
                stmtVenda.setString(2, venda.getFormaPagamento());
                stmtVenda.executeUpdate();

                try (ResultSet generatedKeys = stmtVenda.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        venda.setIdVenda(generatedKeys.getInt(1));
                    } else {
                        throw new SQLException("Falha ao obter o ID da venda.");
                    }
                }
            }

            try (PreparedStatement stmtItem = conn.prepareStatement(sqlItem);
                 PreparedStatement stmtEstoque = conn.prepareStatement(sqlUpdateEstoque)) {

                for (ItemVenda item : venda.getItensVenda()) {
                    stmtItem.setInt(1, venda.getIdVenda());
                    stmtItem.setString(2, item.getProduto().getCodigoBarras());
                    stmtItem.setInt(3, item.getQuantidade());
                    stmtItem.setDouble(4, item.getPrecoUnitario());
                    stmtItem.addBatch();

                    stmtEstoque.setInt(1, item.getQuantidade());
                    stmtEstoque.setString(2, item.getProduto().getCodigoBarras());
                    stmtEstoque.addBatch();
                }
                stmtItem.executeBatch();
                stmtEstoque.executeBatch();
            }

            conn.commit(); // Efetiva a transação
            return true;

        } catch (SQLException e) {
            System.err.println("Erro de transação. Realizando rollback.");
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback(); // Desfaz as operações em caso de erro
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
