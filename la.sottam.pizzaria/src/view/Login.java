package view;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.net.URL;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Login extends JPanel {

    private static final String AUDIOWIDE = "Audiowide-Regular.ttf";
    private static final String POPPINS   = "Poppins-Regular.ttf";

    private static final Color BG_LIGHT_BLUE   = new Color(0xC7EAF7);
    private static final Color PURPLE_CARD     = new Color(0x5B2A86);
    private static final Color GREEN_ACCENT    = new Color(0x6FCF52);
    private static final Color GREEN_BTN       = new Color(0x3F7D3A);
    private static final Color GREEN_BTN_HOVER = new Color(0x356B31);
    private static final Color WHITE           = Color.WHITE;

    private Runnable onVoltarAction;

    public Login() {
        this(null);
    }

    public Login(Runnable onVoltarAction) {
        this.onVoltarAction = onVoltarAction;

        setLayout(new BorderLayout());
        setBackground(BG_LIGHT_BLUE);
        setBorder(new EmptyBorder(30, 40, 30, 40));

        add(buildHeader(), BorderLayout.NORTH);
        add(buildBody(), BorderLayout.CENTER);
    }

    // ---------- Cabeçalho ----------
    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(10, 0, 30, 0));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        left.setOpaque(false);

        JLabel registroLabel = new JLabel("REGISTRO");
        registroLabel.setFont(FonteUtil.carregarFonte(AUDIOWIDE, 64f));
        registroLabel.setForeground(PURPLE_CARD);
        registroLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, PURPLE_CARD));

        JButton backBtn = makeCircleButton("<");
        backBtn.addActionListener(e -> {
            if (onVoltarAction != null) {
                onVoltarAction.run();
            }
        });

        left.add(registroLabel);
        left.add(backBtn);

        JPanel right = new JPanel();
        right.setOpaque(false);
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));

        URL logoUrl = getClass().getResource("/assets/logo.png");
        if (logoUrl != null) {
            ImageIcon logoIcon = new ImageIcon(logoUrl);
            Image logoScaled = logoIcon.getImage().getScaledInstance(350, 70, Image.SCALE_SMOOTH);
            JLabel lblLogo = new JLabel(new ImageIcon(logoScaled));
            right.add(lblLogo);
        }

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
                
                // Desenha o fundo circular
                g2.setColor(GREEN_ACCENT);
                g2.fillOval(0, 0, getWidth(), getHeight());
                
                // Desenha o texto centralizado
                g2.setColor(WHITE);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                g2.drawString(getText(), x, y);
                
                g2.dispose();
            }
        };
        btn.setPreferredSize(new Dimension(36, 36));
        btn.setFont(FonteUtil.carregarFonte(AUDIOWIDE, 30f));
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // ---------- Corpo (Dois Cards Lado a Lado) ----------
    private JPanel buildBody() {
        JPanel body = new JPanel(new GridBagLayout());
        body.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.insets = new Insets(10, 20, 10, 20);

        // Card 1: ENTRAR
        gbc.gridx = 0;
        body.add(buildLoginCard(), gbc);

        // Card 2: CADASTRO
        gbc.gridx = 1;
        body.add(buildRegisterCard(), gbc);

        return body;
    }

    // --- Card Entrar ---
    private RoundedPanel buildLoginCard() {
        RoundedPanel card = new RoundedPanel(30, PURPLE_CARD);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(30, 30, 30, 30));
        card.setPreferredSize(new Dimension(380, 500));

        card.add(sectionTitle("ENTRAR"));
        card.add(Box.createVerticalStrut(25));

        card.add(fieldLabel("ID DO USUÁRIO*"));
        card.add(Box.createVerticalStrut(6));
        RoundedTextField idField = new RoundedTextField(20);
        card.add(idField);
        card.add(Box.createVerticalStrut(20));

        card.add(fieldLabel("SENHA*"));
        card.add(Box.createVerticalStrut(6));
        RoundedPasswordField senhaField = new RoundedPasswordField(20);
        card.add(senhaField);
        card.add(Box.createVerticalStrut(30));

        RoundedButton entrarBtn = new RoundedButton("ENTRAR");
        entrarBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        entrarBtn.addActionListener(e ->
                JOptionPane.showMessageDialog(this, "Login: " + idField.getText()));
        card.add(entrarBtn);

        return card;
    }

    // --- Card Cadastro ---
    private RoundedPanel buildRegisterCard() {
        RoundedPanel card = new RoundedPanel(30, PURPLE_CARD);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(30, 30, 30, 30));
        card.setPreferredSize(new Dimension(420, 500));

        card.add(sectionTitle("CADASTRO"));
        card.add(Box.createVerticalStrut(20));

        card.add(fieldLabel("NOME DE USUÁRIO*"));
        card.add(Box.createVerticalStrut(4));
        RoundedTextField userField = new RoundedTextField(20);
        card.add(userField);
        card.add(Box.createVerticalStrut(12));

        card.add(fieldLabel("SENHA*"));
        card.add(Box.createVerticalStrut(4));
        RoundedPasswordField senhaField = new RoundedPasswordField(20);
        card.add(senhaField);
        card.add(Box.createVerticalStrut(12));

        card.add(fieldLabel("CONFIRMAÇÃO DE SENHA*"));
        card.add(Box.createVerticalStrut(4));
        RoundedPasswordField confirmSenhaField = new RoundedPasswordField(20);
        card.add(confirmSenhaField);
        card.add(Box.createVerticalStrut(12));

        card.add(fieldLabel("SENHA ADMIN*"));
        card.add(Box.createVerticalStrut(4));
        RoundedPasswordField adminField = new RoundedPasswordField(20);
        card.add(adminField);
        card.add(Box.createVerticalStrut(20));

        RoundedButton finalizarBtn = new RoundedButton("FINALIZAR");
        finalizarBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        finalizarBtn.addActionListener(e ->
                JOptionPane.showMessageDialog(this, "Cadastro solicitado para: " + userField.getText()));
        card.add(finalizarBtn);

        return card;
    }

    private JLabel sectionTitle(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FonteUtil.carregarFonte(AUDIOWIDE, 22f));
        label.setForeground(GREEN_ACCENT);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JLabel fieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FonteUtil.carregarFonte(POPPINS, 20f));
        label.setForeground(WHITE);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    // ---------- Componentes customizados ----------

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

    static class RoundedTextField extends JTextField {
        RoundedTextField(int columns) {
            super(columns);
            setOpaque(false);
            setBorder(new EmptyBorder(6, 12, 6, 12));
            setFont(FonteUtil.carregarFonte(POPPINS, 13f));
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
            setAlignmentX(Component.LEFT_ALIGNMENT);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(WHITE);
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 16, 16));
            g2.dispose();
            super.paintComponent(g);
        }
    }

    static class RoundedPasswordField extends JPasswordField {
        RoundedPasswordField(int columns) {
            super(columns);
            setOpaque(false);
            setBorder(new EmptyBorder(6, 12, 6, 12));
            setFont(FonteUtil.carregarFonte(POPPINS, 13f));
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
            setAlignmentX(Component.LEFT_ALIGNMENT);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(WHITE);
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 16, 16));
            g2.dispose();
            super.paintComponent(g);
        }
    }

    static class RoundedButton extends JButton {
        private Color currentBg;

        RoundedButton(String text) {
            super(text);
            setFont(FonteUtil.carregarFonte(POPPINS, 13f));
            setForeground(WHITE);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorder(new EmptyBorder(8, 20, 8, 20));
            setMaximumSize(new Dimension(180, 40));
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            currentBg = GREEN_BTN;

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
        }

        private void setBackgroundColor(Color c) {
            currentBg = c;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(currentBg);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            g2.dispose();
            super.paintComponent(g);
        }
    }
}