import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.net.URL;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class LoginPanel extends JPanel {
    private final JButton btnEntrar;

    public LoginPanel(ActionListener onEntrarClick) {
        setLayout(new BorderLayout());
        setBackground(Color.decode("#CDEEFF"));

        // --- PAINEL SUPERIOR (HEADER / LOGO) ---
        JPanel headerPanel = new JPanel(new GridLayout(1, 1));
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(40, 0, 0, 0));

        // Carregamento Logo
        URL logoUrl = getClass().getResource("/assets/logo.png");
        ImageIcon logoIcon = new ImageIcon(logoUrl);
        Image logoScaled = logoIcon.getImage().getScaledInstance(964, 212, Image.SCALE_SMOOTH);
        JLabel lblLogo = new JLabel(new ImageIcon(logoScaled));
        headerPanel.add(lblLogo);

        add(headerPanel, BorderLayout.NORTH);

        // --- PAINEL CENTRAL (AÇÕES) ---
        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);

        btnEntrar = new JButton("ENTRAR >");
        btnEntrar.setBackground(Color.decode("#39761F"));
        btnEntrar.setForeground(Color.decode("#7ED956"));
        Font Poppins = FonteUtil.carregarFonte("Poppins-Light.ttf", 36f);
        btnEntrar.setFont(Poppins);
        btnEntrar.setPreferredSize(new Dimension(230, 80));
        btnEntrar.setFocusPainted(false);

        btnEntrar.addActionListener(onEntrarClick);
        centerPanel.add(btnEntrar);
        add(centerPanel, BorderLayout.CENTER);

        // --- PAINEL INFERIOR / ALIENS ---
        JPanel footerPanel = new JPanel();
        footerPanel.setOpaque(false);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        // Carregamento Aliens
        URL aliensUrl = getClass().getResource("/assets/aliens.png");
        ImageIcon aliensIcon = new ImageIcon(aliensUrl);
        Image aliensScaled = aliensIcon.getImage().getScaledInstance(736, 550, Image.SCALE_SMOOTH);

        JLabel lblAliens = new JLabel(new ImageIcon(aliensScaled));
        footerPanel.add(lblAliens);
        add(footerPanel, BorderLayout.EAST);
    }

    public JButton getBtnEntrar() {
        return btnEntrar;
    }

    // =====================================================
    // JFrame
    // =====================================================

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            JFrame frame = new JFrame("Login");
            frame.setResizable(false);

            // Fecha a aplicação ao fechar a janela
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // Tamanho da janela
            frame.setSize(1097, 820);

            // Coloca a janela no centro da tela
            frame.setLocationRelativeTo(null);

            // Cria o LoginPanel
            LoginPanel loginPanel = new LoginPanel(e -> {
                System.out.println("Botão ENTRAR clicado!");
            });

            // Coloca o LoginPanel dentro do JFrame
            frame.setLayout(new BorderLayout());
            frame.add(loginPanel, BorderLayout.CENTER);

            // Mostra a janela
            frame.setVisible(true);
        });
    }
}
