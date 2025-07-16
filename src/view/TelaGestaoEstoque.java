package view;

import dao.ProdutoDAO;
import model.Produto;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

/**
 * Tela para gerenciamento do estoque de produtos.
 * <p>
 * Permite visualizar todos os produtos em uma tabela, identificar itens com
 * estoque baixo e realizar ajustes manuais na quantidade de estoque.
 *
 * @author kenyamashida
 */
public class TelaGestaoEstoque extends JFrame {
    private JTable tabelaProdutos;
    private DefaultTableModel model;
    private final ProdutoDAO produtoDAO;

    public TelaGestaoEstoque() {
        setTitle("Gestão de Estoque");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        produtoDAO = new ProdutoDAO();

        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        model.addColumn("Cód. Barras");
        model.addColumn("Nome");
        model.addColumn("Qtd. em Estoque");
        model.addColumn("Estoque Mínimo");
        model.addColumn("Status");

        tabelaProdutos = new JTable(model);
        add(new JScrollPane(tabelaProdutos), BorderLayout.CENTER);

        JPanel panelBotoes = new JPanel();
        JButton btnAjustarEstoque = new JButton("Ajustar Estoque Selecionado");
        JButton btnRecarregar = new JButton("Recarregar Lista");
        panelBotoes.add(btnAjustarEstoque);
        panelBotoes.add(btnRecarregar);
        add(panelBotoes, BorderLayout.SOUTH);

        btnAjustarEstoque.addActionListener(e -> ajustarEstoque());
        btnRecarregar.addActionListener(e -> carregarProdutos());

        carregarProdutos();
    }

    /**
     * Carrega (ou recarrega) a lista de todos os produtos do banco de dados
     * e os exibe na tabela, calculando o status do estoque.
     */
    private void carregarProdutos() {
        model.setRowCount(0);
        List<Produto> produtos = produtoDAO.listarTodos();
        for (Produto p : produtos) {
            String status = p.getQuantidadeEstoque() <= p.getEstoqueMinimo() ? "ESTOQUE BAIXO!" : "OK";
            model.addRow(new Object[]{
                    p.getCodigoBarras(),
                    p.getNome(),
                    p.getQuantidadeEstoque(),
                    p.getEstoqueMinimo(),
                    status
            });
        }
    }

    /**
     * Inicia o fluxo para ajustar o estoque de um produto selecionado na tabela.
     */
    private void ajustarEstoque() {
        int selectedRow = tabelaProdutos.getSelectedRow();
        if (selectedRow >= 0) {
            String codigoBarras = (String) model.getValueAt(selectedRow, 0);
            String nomeProduto = (String) model.getValueAt(selectedRow, 1);

            String novaQtdStr = JOptionPane.showInputDialog(this, "Digite a nova quantidade em estoque para:\n" + nomeProduto, "Ajustar Estoque", JOptionPane.PLAIN_MESSAGE);

            if (novaQtdStr != null && !novaQtdStr.trim().isEmpty()) {
                try {
                    int novaQuantidade = Integer.parseInt(novaQtdStr);
                    produtoDAO.ajustarEstoque(codigoBarras, novaQuantidade);
                    carregarProdutos();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Por favor, insira um número válido.", "Erro", JOptionPane.ERROR_MESSAGE);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Erro ao ajustar o estoque: " + ex.getMessage(), "Erro de Banco de Dados", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um produto na tabela para ajustar.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }
}
