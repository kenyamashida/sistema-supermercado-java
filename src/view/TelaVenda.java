package view;

import dao.ProdutoDAO;
import dao.VendaDAO;
import model.*; // Importa todas as classes do pacote model
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * A tela principal do sistema, que funciona como Ponto de Venda (PDV).
 * <p>
 * Esta classe é responsável pela interface gráfica onde o operador de caixa
 * registra os produtos de uma venda e a finaliza. Ela também serve como
 * ponto de acesso para outras funcionalidades administrativas através de uma
 * barra de menus.
 *
 * @author kenyamashida
 */
public class TelaVenda extends JFrame {

    // --- Atributos da Interface Gráfica (Componentes Swing) ---
    private JTextField txtBuscaProduto;
    private JTable tabelaItens;
    private DefaultTableModel tableModel;
    private JLabel lblTotal;
    private JButton btnAdicionar, btnFinalizar;
    private JMenuBar menuBar;

    // --- Atributos de Controle e Negócio ---
    private Venda vendaAtual;
    private final ProdutoDAO produtoDAO;
    private final VendaDAO vendaDAO;
    private final NumberFormat currencyFormat;

    /**
     * Construtor da TelaVenda.
     * Inicializa todos os componentes da interface, configura os listeners de eventos
     * e prepara a tela para ser exibida.
     */
    public TelaVenda() {
        setTitle("PDV - Ponto de Venda");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Inicializa os objetos de negócio e de acesso a dados
        vendaAtual = new Venda();
        produtoDAO = new ProdutoDAO();
        vendaDAO = new VendaDAO();
        currencyFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

        // Cria e adiciona a barra de menus à janela
        criarMenuBar();
        setJMenuBar(menuBar);

        // --- Montagem dos Painéis da Interface ---
        JPanel panelBusca = new JPanel(new BorderLayout(10, 10));
        panelBusca.setBorder(BorderFactory.createTitledBorder("Buscar Produto (Código de Barras)"));
        txtBuscaProduto = new JTextField();
        btnAdicionar = new JButton("Adicionar Item");
        panelBusca.add(txtBuscaProduto, BorderLayout.CENTER);
        panelBusca.add(btnAdicionar, BorderLayout.EAST);

        tableModel = new DefaultTableModel(new Object[]{"Cód.", "Produto", "Qtd.", "Preço Unit.", "Subtotal"}, 0);
        tabelaItens = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tabelaItens);

        JPanel panelSul = new JPanel(new BorderLayout(10, 10));
        panelSul.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        lblTotal = new JLabel("Total: R$ 0,00");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 24));

        btnFinalizar = new JButton("Finalizar Venda");
        btnFinalizar.setFont(new Font("Arial", Font.BOLD, 16));
        btnFinalizar.setBackground(new Color(0, 153, 51));
        btnFinalizar.setForeground(Color.WHITE);

        panelSul.add(lblTotal, BorderLayout.WEST);
        panelSul.add(btnFinalizar, BorderLayout.EAST);

        // Adiciona os painéis principais ao Frame
        add(panelBusca, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(panelSul, BorderLayout.SOUTH);

        // --- Configuração dos Listeners de Eventos ---
        btnAdicionar.addActionListener(e -> adicionarItem());
        txtBuscaProduto.addActionListener(e -> adicionarItem());
        btnFinalizar.addActionListener(e -> finalizarVenda());
    }

    /**
     * Cria e configura a barra de menus superior da janela.
     * Agrupa as funcionalidades de Cadastros, Estoque e Relatórios de forma organizada.
     */
    private void criarMenuBar() {
        menuBar = new JMenuBar();

        // Menu Cadastros
        JMenu menuCadastros = new JMenu("Cadastros");
        JMenuItem itemCadastrarProduto = new JMenuItem("Cadastrar Novo Produto...");
        itemCadastrarProduto.addActionListener(e -> new TelaCadastroProduto().setVisible(true));
        menuCadastros.add(itemCadastrarProduto);

        // Menu Estoque
        JMenu menuEstoque = new JMenu("Estoque");
        JMenuItem itemGerirEstoque = new JMenuItem("Gerenciar Estoque...");
        itemGerirEstoque.addActionListener(e -> new TelaGestaoEstoque().setVisible(true));
        menuEstoque.add(itemGerirEstoque);

        // Menu Relatórios
        JMenu menuRelatorios = new JMenu("Relatórios");
        JMenuItem itemAbrirRelatorios = new JMenuItem("Abrir Central de Relatórios...");
        itemAbrirRelatorios.addActionListener(e -> new TelaRelatorios().setVisible(true));
        menuRelatorios.add(itemAbrirRelatorios);

        menuBar.add(menuCadastros);
        menuBar.add(menuEstoque);
        menuBar.add(menuRelatorios);
    }

    /**
     * Ação executada ao adicionar um item à venda.
     * Busca o produto no banco, valida o estoque e o adiciona na tabela e na venda atual.
     */
    private void adicionarItem() {
        String codigo = txtBuscaProduto.getText().trim();
        if (codigo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, insira o código do produto.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Produto produto = produtoDAO.buscarPorCodigo(codigo);

        if (produto == null) {
            JOptionPane.showMessageDialog(this, "Produto não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (produto.getQuantidadeEstoque() <= 0) {
            JOptionPane.showMessageDialog(this, "Produto sem estoque.", "Estoque Insuficiente", JOptionPane.WARNING_MESSAGE);
            return;
        }

        ItemVenda novoItem = new ItemVenda(produto, 1);
        vendaAtual.adicionarItem(novoItem);

        tableModel.addRow(new Object[]{
                produto.getCodigoBarras(),
                produto.getNome(),
                1,
                currencyFormat.format(produto.getPrecoVenda()),
                currencyFormat.format(novoItem.calcularSubtotal())
        });

        atualizarTotal();
        txtBuscaProduto.setText("");
        txtBuscaProduto.requestFocus();
    }

    /**
     * Atualiza o rótulo do valor total na tela com base na venda atual.
     */
    private void atualizarTotal() {
        vendaAtual.calcularTotal();
        lblTotal.setText("Total: " + currencyFormat.format(vendaAtual.getTotalVenda()));
    }

    /**
     * Inicia o processo de finalização da venda, solicitando a forma de pagamento
     * e utilizando o VendaDAO para persistir os dados no banco.
     */
    private void finalizarVenda() {
        if (vendaAtual.getItensVenda().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Adicione itens para realizar a venda.", "Venda vazia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String[] opcoesPagamento = {"Dinheiro", "Cartão de Crédito", "Cartão de Débito", "PIX"};
        String formaPagamento = (String) JOptionPane.showInputDialog(this, "Selecione a forma de pagamento:",
                "Finalizar Venda", JOptionPane.QUESTION_MESSAGE, null, opcoesPagamento, opcoesPagamento[0]);

        if (formaPagamento != null) {
            vendaAtual.setFormaPagamento(formaPagamento);
            boolean sucesso = vendaDAO.registrarVenda(vendaAtual);

            if (sucesso) {
                JOptionPane.showMessageDialog(this, "Venda registrada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                reiniciarVenda();
            } else {
                JOptionPane.showMessageDialog(this, "Ocorreu um erro ao registrar a venda.", "Erro de Banco de Dados", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Limpa a tela e os dados da venda atual para iniciar uma nova transação.
     */
    private void reiniciarVenda() {
        vendaAtual = new Venda();
        tableModel.setRowCount(0);
        atualizarTotal();
        txtBuscaProduto.requestFocus();
    }
}
