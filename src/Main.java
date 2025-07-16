import view.TelaVenda;
import javax.swing.SwingUtilities;

/**
 * Classe principal que inicia a aplicação do sistema de supermercado.
 * <p>
 * A única responsabilidade desta classe é criar e exibir a tela principal (PDV),
 * garantindo que a interface gráfica seja executada na thread correta do Swing.
 *
 * @author kenyamashida
 */
public class Main {
    /**
     * O ponto de entrada da aplicação.
     * @param args Argumentos de linha de comando (não utilizados).
     */
    public static void main(String[] args) {
        // SwingUtilities.invokeLater é usado para enfileirar a criação da GUI
        // na Event Dispatch Thread (EDT), que é a thread responsável por
        // todas as atualizações da interface gráfica em Swing.
        // Isso evita problemas de concorrência e garante que a UI seja responsiva.
        SwingUtilities.invokeLater(() -> new TelaVenda().setVisible(true));
    }
}
