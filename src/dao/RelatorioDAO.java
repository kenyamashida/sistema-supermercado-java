
package dao;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Objeto de Acesso a Dados (DAO) especializado em gerar relatórios gerenciais.
 * <p>
 * Esta classe contém os métodos para executar as consultas SQL complexas
 * necessárias para os relatórios, retornando os dados de forma estruturada.
 *
 * @author kenyamashida
 */
public class RelatorioDAO {

    /**
     * Classe interna estática para encapsular o resultado de uma consulta de relatório.
     * <p>
     * Funciona como um "pacote" de dados, contendo os nomes das colunas e uma
     * lista de linhas (arrays de objetos), facilitando a exibição em uma JTable.
     */
    public static class ResultadoRelatorio {
        private final String[] colunas;
        private final List<Object[]> dados;

        public ResultadoRelatorio(String[] colunas, List<Object[]> dados) {
            this.colunas = colunas;
            this.dados = dados;
        }

        public String[] getColunas() { return colunas; }
        public List<Object[]> getDados() { return dados; }
    }

    /**
     * Método genérico e reutilizável para executar consultas SQL e formatar os resultados.
     * <p>
     * Abstrai a complexidade do JDBC (ResultSet, MetaData) e retorna os dados
     * em um formato simples e pronto para uso pela camada de visão.
     *
     * @param sql A query SQL a ser executada.
     * @param params Parâmetros variáveis para a PreparedStatement (evita SQL Injection).
     * @return Um objeto {@link ResultadoRelatorio} com os dados da consulta.
     */
    private ResultadoRelatorio executarConsulta(String sql, Object... params) {
        List<Object[]> dados = new ArrayList<>();
        String[] colunas = new String[0];

        try (Connection conn = ConexaoMySQL.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();

                colunas = new String[columnCount];
                for (int i = 0; i < columnCount; i++) {
                    colunas[i] = metaData.getColumnLabel(i + 1).replace("_", " ").toUpperCase();
                }

                while (rs.next()) {
                    Object[] linha = new Object[columnCount];
                    for (int i = 0; i < columnCount; i++) {
                        linha[i] = rs.getObject(i + 1);
                    }
                    dados.add(linha);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ResultadoRelatorio(colunas, dados);
    }

    // --- Métodos Públicos para cada Relatório ---

    public ResultadoRelatorio gerarRelatorioVendasDiarias(LocalDate data) {
        String sql = "SELECT DATE(data_venda) AS dia, COUNT(id_venda) AS qtd_vendas, SUM(valor_total) AS faturamento, GROUP_CONCAT(DISTINCT forma_pagamento SEPARATOR ', ') AS pagamentos FROM vendas WHERE DATE(data_venda) = ? GROUP BY dia";
        return executarConsulta(sql, data);
    }

    public ResultadoRelatorio gerarRelatorioProdutosMaisVendidosPorQtd() {
        String sql = "SELECT p.nome, p.codigo_barras, SUM(iv.quantidade) AS total_unidades_vendidas FROM itens_venda AS iv JOIN produtos AS p ON iv.codigo_barras_produto = p.codigo_barras GROUP BY p.nome, p.codigo_barras ORDER BY total_unidades_vendidas DESC LIMIT 20";
        return executarConsulta(sql);
    }

    public ResultadoRelatorio gerarRelatorioEstoqueBaixo() {
        String sql = "SELECT nome, codigo_barras, quantidade_estoque, estoque_minimo, (estoque_minimo - quantidade_estoque) AS necessidade_reposicao FROM produtos WHERE quantidade_estoque <= estoque_minimo ORDER BY necessidade_reposicao DESC";
        return executarConsulta(sql);
    }

    public ResultadoRelatorio gerarRelatorioMargemDeLucro() {
        String sql = "SELECT p.nome, p.preco_venda, p.preco_compra, (p.preco_venda - p.preco_compra) AS margem_unidade, SUM(iv.quantidade) AS total_vendido, SUM(iv.quantidade * (p.preco_venda - p.preco_compra)) AS lucro_total FROM itens_venda AS iv JOIN produtos AS p ON iv.codigo_barras_produto = p.codigo_barras GROUP BY p.nome, p.preco_venda, p.preco_compra ORDER BY lucro_total DESC";
        return executarConsulta(sql);
    }

    public ResultadoRelatorio gerarRelatorioFluxoDeCaixa() {
        String sql = "SELECT DATE(data_venda) AS dia, forma_pagamento, SUM(valor_total) AS total_recebido FROM vendas GROUP BY dia, forma_pagamento ORDER BY dia DESC, forma_pagamento";
        return executarConsulta(sql);
    }
}