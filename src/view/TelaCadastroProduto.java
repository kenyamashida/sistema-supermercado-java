package view;

import dao.ProdutoDAO;
import model.Produto;
import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

/**
 * Tela de formulário para cadastro de novos produtos no sistema.
 *
 * @author kenyamashida
 */
public class TelaCadastroProduto extends JFrame {
    private JTextField txtCodigoBarras, txtNome, txtPrecoVenda, txtPrecoCompra, txtQuantidade, txtEstoqueMinimo, txtCategoria;
    private final ProdutoDAO produtoDAO;

    public TelaCadastroProduto() {
        setTitle("Cadastro de Novo Produto");
        setSize(450, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        produtoDAO = new ProdutoDAO();

        JPanel panelForm = new JPanel(new GridLayout(8, 2, 10, 10));
        panelForm.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        panelForm.add(new JLabel("Código de Barras:"));
        txtCodigoBarras = new JTextField();
        panelForm.add(txtCodigoBarras);

        panelForm.add(new JLabel("Nome do Produto:"));
        txtNome = new JTextField();
        panelForm.add(txtNome);

        panelForm.add(new JLabel("Preço de Venda (R$):"));
        txtPrecoVenda = new JTextField();
        panelForm.add(txtPrecoVenda);

        panelForm.add(new JLabel("Preço de Compra (R$):"));
        txtPrecoCompra = new JTextField();
        panelForm.add(txtPrecoCompra);

        panelForm.add(new JLabel("Quantidade Inicial:"));
        txtQuantidade = new JTextField();
        panelForm.add(txtQuantidade);

        panelForm.add(new JLabel("Estoque Mínimo:"));
        txtEstoqueMinimo = new JTextField();
        panelForm.add(txtEstoqueMinimo);

        panelForm.add(new JLabel("Categoria:"));
        txtCategoria = new JTextField();
        panelForm.add(txtCategoria);

        JButton btnSalvar = new JButton("Salvar Produto");
        panelForm.add(new JLabel()); // Espaço em branco
        panelForm.add(btnSalvar);

        add(panelForm);

        btnSalvar.addActionListener(e -> salvarProduto());
    }

    /**
     * Coleta os dados do formulário, cria um objeto Produto e o persiste
     * no banco de dados através do ProdutoDAO.
     */
    private void salvarProduto() {
        try {
            Produto produto = new Produto();
            produto.setCodigoBarras(txtCodigoBarras.getText());
            produto.setNome(txtNome.getText());
            produto.setPrecoVenda(Double.parseDouble(txtPrecoVenda.getText()));
            produto.setPrecoCompra(Double.parseDouble(txtPrecoCompra.getText()));
            produto.setQuantidadeEstoque(Integer.parseInt(txtQuantidade.getText()));
            produto.setEstoqueMinimo(Integer.parseInt(txtEstoqueMinimo.getText()));
            produto.setCategoria(txtCategoria.getText());

            produtoDAO.cadastrarProduto(produto);

            JOptionPane.showMessageDialog(this, "Produto cadastrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            dispose();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor, preencha os campos numéricos corretamente.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar no banco de dados: " + ex.getMessage(), "Erro de Banco de Dados", JOptionPane.ERROR_MESSAGE);
        }
    }
}
