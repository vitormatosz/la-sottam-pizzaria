package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;
import java.net.URL;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Inicial extends JPanel {
    private final JButton btnEntrar;

    public Inicial(ActionListener onEntrarClick) {
        setLayout(new BorderLayout());
        setBackground(Color.decode("#CDEEFF"));

        // --- PAINEL SUPERIOR (HEADER / LOGO) ---
        JPanel headerPanel = new JPanel(new GridLayout(1, 1));
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(40, 0, 0, 0));

        // Carregamento Logo
        URL logoUrl = getClass().getResource("/assets/logo.png");
        if (logoUrl != null) {
            ImageIcon logoIcon = new ImageIcon(logoUrl);
            Image logoScaled = logoIcon.getImage().getScaledInstance(964, 212, Image.SCALE_SMOOTH);
            JLabel lblLogo = new JLabel(new ImageIcon(logoScaled));
            headerPanel.add(lblLogo);
        }

        add(headerPanel, BorderLayout.NORTH);

        // --- PAINEL CENTRAL (AÇÕES) ---
        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);

        // Botão customizado com cantos arredondados via Graphics2D
        btnEntrar = new JButton("ENTRAR >") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Desenha o fundo arredondado
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);

                // Desenha o texto centralizado
                g2.setColor(getForeground());
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                g2.drawString(getText(), x, y);

                g2.dispose();
            }
        };

        btnEntrar.setContentAreaFilled(false);
        btnEntrar.setFocusPainted(false);
        btnEntrar.setBorderPainted(false);

        btnEntrar.setBackground(Color.decode("#39761F"));
        btnEntrar.setForeground(Color.decode("#7ED956"));
        Font poppins = FonteUtil.carregarFonte("Poppins-Bold.ttf", 36f);
        btnEntrar.setFont(poppins);
        btnEntrar.setPreferredSize(new Dimension(230, 80));
        btnEntrar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnEntrar.addActionListener(onEntrarClick);
        centerPanel.add(btnEntrar);
        add(centerPanel, BorderLayout.CENTER);

        // --- PAINEL INFERIOR / ALIENS ---
        JPanel footerPanel = new JPanel();
        footerPanel.setOpaque(false);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        // Carregamento Aliens
        URL aliensUrl = getClass().getResource("/assets/aliens.png");
        if (aliensUrl != null) {
            ImageIcon aliensIcon = new ImageIcon(aliensUrl);
            Image aliensScaled = aliensIcon.getImage().getScaledInstance(736, 550, Image.SCALE_SMOOTH);
            JLabel lblAliens = new JLabel(new ImageIcon(aliensScaled));
            footerPanel.add(lblAliens);
        }
        add(footerPanel, BorderLayout.EAST);
    }

    public JButton getBtnEntrar() {
        return btnEntrar;
    }}
