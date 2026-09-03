import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // JFrame frame = new JFrame("La Sottam Pizzaria");
            // frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            // frame.setSize(1024, 768);
            // frame.setLocationRelativeTo(null);

            // // Adiciona o painel de Login
            // LoginPanel loginPanel = new LoginPanel(e -> {
            //     System.out.println("Navegar para a próxima tela!");
            // });

            // frame.add(loginPanel);
            // frame.setVisible(true);
            LaSottamGUI tela = new LaSottamGUI();
            tela.setVisible(true);
        });
    }
}

// public class Main {
//     public static void main(String[] args) {
//         dao.ClienteDAO dao = new dao.ClienteDAO();

//         model.Cliente novo = new model.Cliente("Maria José", "11999998888", "Rua das Pizzas, 123");
//         dao.inserir(novo);

//         for (model.Cliente c : dao.listarTodos()) {
//             System.out.println(c.getId() + " - " + c.getNome() + " - " + c.getNumeroTel());
//         }
//     }
// }