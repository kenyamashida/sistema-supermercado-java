package view;

import dao.RelatorioDAO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Tela para visualização de relatórios gerenciais.
 * <p>
 * Apresenta botões para cada tipo de relatório e exibe os resultados
 * retornados pelo {@link RelatorioDAO} em uma tabela.
 *
 * @author kenyamashida
 */
public class TelaRelatorios extends JFrame {

    private JTable tabelaResultados;
    private DefaultTableModel tableModel;
    private final RelatorioDAO relatorioDAO;
    private JLabel lblTituloRelatorio;

    public TelaRelatorios() {
        setTitle("Central de Relatórios Gerenciais");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        relatorioDAO = new RelatorioDAO();

        JPanel panelBotoes = new JPanel();
        panelBotoes.setLayout(new BoxLayout(panelBotoes, BoxLayout.Y_AXIS));
        panelBotoes.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton btnVendasDiarias = new JButton("Vendas do Dia");
        JButton btnMaisVendidos = new JButton("Produtos Mais Vendidos");
        JButton btnEstoqueBaixo = new JButton("Estoque Baixo");
        JButton btnMargemLucro = new JButton("Margem de Lucro");
        JButton btnFluxoCaixa = new JButton("Fluxo de Caixa (Entradas)");
        JButton btnVoltar = new JButton("<< Voltar ao PDV");

        Dimension buttonSize = new Dimension(200, 40);
        Component[] botoes = {btnVendasDiarias, btnMaisVendidos, btnEstoqueBaixo, btnMargemLucro, btnFluxoCaixa};
        for (Component btn : botoes) {
            ((JButton) btn).setMaximumSize(buttonSize);
            ((JButton) btn).setAlignmentX(Component.CENTER_ALIGNMENT);
        }
        btnVoltar.setMaximumSize(new Dimension(200, 30));
        btnVoltar.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelBotoes.add(btnVendasDiarias);
        panelBotoes.add(Box.createRigidArea(new Dimension(0, 10)));
        panelBotoes.add(btnMaisVendidos);
        panelBotoes.add(Box.createRigidArea(new Dimension(0, 10)));
        panelBotoes.add(btnEstoqueBaixo);
        panelBotoes.add(Box.createRigidArea(new Dimension(0, 10)));
        panelBotoes.add(btnMargemLucro);
        panelBotoes.add(Box.createRigidArea(new Dimension(0, 10)));
        panelBotoes.add(btnFluxoCaixa);
        panelBotoes.add(Box.createVerticalGlue());
        panelBotoes.add(btnVoltar);

        JPanel panelResultados = new JPanel(new BorderLayout());
        panelResultados.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        lblTituloRelatorio = new JLabel("Selecione um relatório para começar", SwingConstants.CENTER);
        lblTituloRelatorio.setFont(new Font("Arial", Font.BOLD, 18));

        tableModel = new DefaultTableModel();
        tabelaResultados = new JTable(tableModel);
        tabelaResultados.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(tabelaResultados);

        panelResultados.add(lblTituloRelatorio, BorderLayout.NORTH);
        panelResultados.add(scrollPane, BorderLayout.CENTER);

        add(panelBotoes, BorderLayout.WEST);
        add(panelResultados, BorderLayout.CENTER);

        // --- Listeners de Eventos ---
        btnVoltar.addActionListener(e -> dispose());
        btnEstoqueBaixo.addActionListener(e -> gerarRelatorioEstoqueBaixo());
        btnMaisVendidos.addActionListener(e -> gerarRelatorioMaisVendidos());
        btnMargemLucro.addActionListener(e -> gerarRelatorioMargemLucro());
        btnFluxoCaixa.addActionListener(e -> gerarRelatorioFluxoCaixa());
        btnVendasDiarias.addActionListener(e -> gerarRelatorioVendasDiarias());
    }

    private void gerarRelatorioEstoqueBaixo() {
        lblTituloRelatorio.setText("Relatório de Estoque Baixo");
        RelatorioDAO.ResultadoRelatorio resultado = relatorioDAO.gerarRelatorioEstoqueBaixo();
        atualizarTabela(resultado);
    }

    private void gerarRelatorioMaisVendidos() {
        lblTituloRelatorio.setText("Relatório de Produtos Mais Vendidos (por Quantidade)");
        RelatorioDAO.ResultadoRelatorio resultado = relatorioDAO.gerarRelatorioProdutosMaisVendidosPorQtd();
        atualizarTabela(resultado);
    }

    private void gerarRelatorioMargemLucro() {
        lblTituloRelatorio.setText("Relatório de Margem de Lucro por Produto");
        RelatorioDAO.ResultadoRelatorio resultado = relatorioDAO.gerarRelatorioMargemDeLucro();
        atualizarTabela(resultado);
    }

    private void gerarRelatorioFluxoCaixa() {
        lblTituloRelatorio.setText("Relatório de Fluxo de Caixa (Entradas por Venda)");
        RelatorioDAO.ResultadoRelatorio resultado = relatorioDAO.gerarRelatorioFluxoDeCaixa();
        atualizarTabela(resultado);
    }

    private void gerarRelatorioVendasDiarias() {
        String dataStr = JOptionPane.showInputDialog(this, "Digite a data (formato: AAAA-MM-DD):", LocalDate.now().toString());
        if (dataStr != null && !dataStr.trim().isEmpty()) {
            try {
                LocalDate data = LocalDate.parse(dataStr);
                lblTituloRelatorio.setText("Relatório de Vendas do Dia: " + data);
                RelatorioDAO.ResultadoRelatorio resultado = relatorioDAO.gerarRelatorioVendasDiarias(data);
                atualizarTabela(resultado);
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "Formato de data inválido. Use AAAA-MM-DD.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Limpa a tabela e a preenche com os novos dados do relatório.
     * @param resultado O objeto contendo os nomes das colunas e as linhas de dados.
     */
    private void atualizarTabela(RelatorioDAO.ResultadoRelatorio resultado) {
        tableModel.setColumnIdentifiers(resultado.getColunas());
        tableModel.setRowCount(0);
        for (Object[] linha : resultado.getDados()) {
            tableModel.addRow(linha);
        }
        if (resultado.getDados().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhum dado encontrado para este relatório.", "Sem Resultados", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
