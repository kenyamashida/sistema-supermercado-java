
package dao;

import model.Produto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Objeto de Acesso a Dados (DAO) para a entidade {@link Produto}.
 * <p>
 * Esta classe encapsula toda a lógica de interação com a tabela 'produtos'
 * no banco de dados, como inserção, atualização e consulta de produtos.
 *
 * @author kenyamashida
 */
public class ProdutoDAO {

    /**
     * Insere um novo produto no banco de dados.
     *
     * @param produto O objeto {@link Produto} contendo os dados a serem salvos.
     * @throws SQLException se ocorrer um erro durante a operação de SQL.
     */
    public void cadastrarProduto(Produto produto) throws SQLException {
        String sql = "INSERT INTO produtos (codigo_barras, nome, preco_venda, preco_compra, quantidade_estoque, estoque_minimo, categoria, id_fornecedor) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexaoMySQL.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, produto.getCodigoBarras());
            stmt.setString(2, produto.getNome());
            stmt.setDouble(3, produto.getPrecoVenda());
            stmt.setDouble(4, produto.getPrecoCompra());
            stmt.setInt(5, produto.getQuantidadeEstoque());
            stmt.setInt(6, produto.getEstoqueMinimo());
            stmt.setString(7, produto.getCategoria());

            if (produto.getFornecedor() != null) {
                stmt.setInt(8, produto.getFornecedor().getId());
            } else {
                stmt.setNull(8, Types.INTEGER);
            }

            stmt.executeUpdate();
        }
    }

    /**
     * Atualiza a quantidade em estoque de um produto específico.
     *
     * @param codigoBarras O identificador do produto a ser atualizado.
     * @param novaQuantidade A nova quantidade total em estoque.
     * @throws SQLException se ocorrer um erro durante a operação de SQL.
     */
    public void ajustarEstoque(String codigoBarras, int novaQuantidade) throws SQLException {
        String sql = "UPDATE produtos SET quantidade_estoque = ? WHERE codigo_barras = ?";
        try (Connection conn = ConexaoMySQL.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, novaQuantidade);
            stmt.setString(2, codigoBarras);
            stmt.executeUpdate();
        }
    }

    /**
     * Retorna uma lista com todos os produtos cadastrados no banco de dados.
     *
     * @return uma {@link List} de objetos {@link Produto}.
     */
    public List<Produto> listarTodos() {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT * FROM produtos ORDER BY nome";
        try (Connection conn = ConexaoMySQL.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Produto produto = new Produto();
                produto.setCodigoBarras(rs.getString("codigo_barras"));
                produto.setNome(rs.getString("nome"));
                produto.setPrecoVenda(rs.getDouble("preco_venda"));
                produto.setQuantidadeEstoque(rs.getInt("quantidade_estoque"));
                produto.setEstoqueMinimo(rs.getInt("estoque_minimo"));
                produtos.add(produto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return produtos;
    }

    /**
     * Busca um único produto pelo seu código de barras.
     *
     * @param codigoBarras O código de barras do produto a ser buscado.
     * @return um objeto {@link Produto} se encontrado, caso contrário, {@code null}.
     */
    public Produto buscarPorCodigo(String codigoBarras) {
        String sql = "SELECT * FROM produtos WHERE codigo_barras = ?";
        Produto produto = null;
        try (Connection conn = ConexaoMySQL.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, codigoBarras);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    produto = new Produto();
                    produto.setCodigoBarras(rs.getString("codigo_barras"));
                    produto.setNome(rs.getString("nome"));
                    produto.setPrecoVenda(rs.getDouble("preco_venda"));
                    produto.setPrecoCompra(rs.getDouble("preco_compra"));
                    produto.setQuantidadeEstoque(rs.getInt("quantidade_estoque"));
                    produto.setEstoqueMinimo(rs.getInt("estoque_minimo"));
                    produto.setCategoria(rs.getString("categoria"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return produto;
    }
}