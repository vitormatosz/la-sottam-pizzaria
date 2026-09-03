import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.net.URL;

/**
 * Tela de Login / Cadastro - La Sottam Pizzaria
 *
 * Para compilar e executar:
 *   javac LaSottamGUI.java
 *   java LaSottamGUI
 */
public class LaSottamGUI extends JFrame {

    // Paleta de cores baseada no design
    private static final Color BG_LIGHT_BLUE   = new Color(0xC7EAF7);
    private static final Color PURPLE_CARD     = new Color(0x5B2A86);
    private static final Color GREEN_ACCENT    = new Color(0x6FCF52);
    private static final Color GREEN_BTN       = new Color(0x3F7D3A);
    private static final Color GREEN_BTN_HOVER = new Color(0x356B31);
    private static final Color WHITE           = Color.WHITE;

    public LaSottamGUI() {
        setTitle("Registro - La Sottam Pizzaria");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG_LIGHT_BLUE);
        root.setBorder(new EmptyBorder(30, 40, 30, 40));
        setContentPane(root);

        root.add(buildHeader(), BorderLayout.NORTH);
        root.add(buildBody(), BorderLayout.CENTER);
    }

    // ---------- Cabeçalho ----------
    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(10, 0, 30, 0));

        // Lado esquerdo: "REGISTRO" com seta
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        left.setOpaque(false);

        JLabel registroLabel = new JLabel("REGISTRO");
        registroLabel.setFont(new Font("SansSerif", Font.BOLD, 34));
        registroLabel.setForeground(PURPLE_CARD);
        // Efeito de "sublinhado duplo" simples
        registroLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, PURPLE_CARD));

        JButton backBtn = makeCircleButton("<");
        left.add(registroLabel);
        left.add(backBtn);

        // Lado direito: logo LA SOTTAM PIZZARIA
        JPanel right = new JPanel();
        right.setOpaque(false);
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));

        URL logoUrl = getClass().getResource("/assets/logo.png");
        ImageIcon logoIcon = new ImageIcon(logoUrl);
        Image logoScaled = logoIcon.getImage().getScaledInstance(500, 100, Image.SCALE_SMOOTH);
        JLabel lblLogo = new JLabel(new ImageIcon(logoScaled));
        right.add(lblLogo);

        header.add(left, BorderLayout.WEST);
        header.add(right, BorderLayout.EAST);
        return header;
    }

    private JButton makeCircleButton(String text) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(GREEN_ACCENT);
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setPreferredSize(new Dimension(36, 36));
        btn.setForeground(WHITE);
        btn.setFont(new Font("SansSerif", Font.BOLD, 16));
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        return btn;
    }

    // ---------- Corpo (dois cards) ----------
    private JPanel buildBody() {
        JPanel body = new JPanel(new GridBagLayout());
        body.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.insets = new Insets(20, 20, 20, 20);

        gbc.gridx = 0;
        gbc.weightx = 1;
        body.add(buildLoginCard(), gbc);

        return body;
    }

    private RoundedPanel buildLoginCard() {
        RoundedPanel card = new RoundedPanel(30, PURPLE_CARD);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(35, 35, 35, 35));
        card.setPreferredSize(new Dimension(700, 380));

        card.add(sectionTitle("ENTRAR"));
        card.add(Box.createVerticalStrut(30));

        card.add(fieldLabel("ID DO USUÁRIO*"));
        card.add(Box.createVerticalStrut(6));
        
        RoundedTextField idField = new RoundedTextField(20);
        card.add(idField);
        card.add(Box.createVerticalStrut(30));

        card.add(fieldLabel("SENHA*"));
        card.add(Box.createVerticalStrut(6));
        RoundedPasswordField senhaField = new RoundedPasswordField(20);
        card.add(senhaField);
        card.add(Box.createVerticalStrut(35));

        RoundedButton entrarBtn = new RoundedButton("ENTRAR  \u2713");
        entrarBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        entrarBtn.addActionListener(e ->
                JOptionPane.showMessageDialog(this, "Login: " + idField.getText()));
        card.add(entrarBtn);

        return card;
    }

    private JLabel sectionTitle(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.BOLD, 22));
        label.setForeground(GREEN_ACCENT);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JLabel fieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.BOLD, 12));
        label.setForeground(WHITE);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    // ---------- Componentes customizados ----------

    /** Painel com cantos arredondados. */
    static class RoundedPanel extends JPanel {
        private final int radius;
        private final Color bg;

        RoundedPanel(int radius, Color bg) {
            this.radius = radius;
            this.bg = bg;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bg);
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), radius, radius));
            g2.dispose();
            super.paintComponent(g);
        }
    }

    /** Campo de texto com cantos arredondados. */
    static class RoundedTextField extends JTextField {
        RoundedTextField(int columns) {
            super(columns);
            setOpaque(false);
            setBorder(new EmptyBorder(8, 14, 8, 14));
            setFont(new Font("SansSerif", Font.PLAIN, 14));
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
            setAlignmentX(Component.LEFT_ALIGNMENT);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(WHITE);
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 18, 18));
            g2.dispose();
            super.paintComponent(g);
        }
    }

    /** Campo de senha com cantos arredondados. */
    static class RoundedPasswordField extends JPasswordField {
        RoundedPasswordField(int columns) {
            super(columns);
            setOpaque(false);
            setBorder(new EmptyBorder(8, 14, 8, 14));
            setFont(new Font("SansSerif", Font.PLAIN, 14));
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
            setAlignmentX(Component.LEFT_ALIGNMENT);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(WHITE);
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 18, 18));
            g2.dispose();
            super.paintComponent(g);
        }
    }

    /** Botão verde com cantos bem arredondados (estilo "pill"). */
/** Botão verde com cantos retos (retangular). */
    static class RoundedButton extends JButton {
        RoundedButton(String text) {
            super(text);
            setFont(new Font("SansSerif", Font.BOLD, 14));
            setForeground(WHITE);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorder(new EmptyBorder(10, 24, 10, 24));
            setMaximumSize(new Dimension(200, 60));
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    setBackgroundColor(GREEN_BTN_HOVER);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    setBackgroundColor(GREEN_BTN);
                }
            });
            currentBg = GREEN_BTN;
        }

        private Color currentBg;

        private void setBackgroundColor(Color c) {
            currentBg = c;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(currentBg);
            
            // Desenha um retângulo simples com cantos retos
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            
            g2.dispose();
            super.paintComponent(g);
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LaSottamGUI().setVisible(true));
    }
}