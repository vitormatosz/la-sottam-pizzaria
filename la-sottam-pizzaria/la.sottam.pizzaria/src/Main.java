import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("La Sottam Pizzaria");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1024, 768);
            frame.setLocationRelativeTo(null);

            // Adiciona o painel de Login
            LoginPanel loginPanel = new LoginPanel(e -> {
                System.out.println("Navegar para a próxima tela!");
            });

            frame.add(loginPanel);
            frame.setVisible(true);
        });
    }
}