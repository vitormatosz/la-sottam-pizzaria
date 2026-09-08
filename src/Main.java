import java.awt.CardLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import view.*;

public class Main {
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("La Sottam Pizzaria");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.setLocationRelativeTo(null);

            // Gerenciador de navegação por telas
            CardLayout cardLayout = new CardLayout();
            JPanel mainPanel = new JPanel(cardLayout);

            // Instancia o LoginPanel (Tela Inicial) e define a ação para ir para o Registro
            LoginPanel loginPanel = new LoginPanel(e -> {
                cardLayout.show(mainPanel, "TELA_REGISTRO");
            });

            // Instancia a tela Login passando a ação para VOLTAR para a TELA_INICIAL
            Login telaRegistro = new Login(() -> {
                cardLayout.show(mainPanel, "TELA_INICIAL");
            });

            // Adiciona as telas ao container
            mainPanel.add(loginPanel, "TELA_INICIAL");
            mainPanel.add(telaRegistro, "TELA_REGISTRO");

            // Exibe a tela inicial primeiro
            cardLayout.show(mainPanel, "TELA_INICIAL");

            frame.add(mainPanel);
            frame.setVisible(true);
        });
    }
}