package view;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.net.URL;
import java.util.function.Consumer;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class MenuPrincipal extends JPanel {

    private static final String POPPINS = "Poppins-Regular.ttf";

    private static final Color BG_LIGHT_BLUE = new Color(0xC7EAF7);
    private static final Color YELLOW_BTN = new Color(0xF5C242);
    private static final Color GREEN_BTN = new Color(0x6FCF52);
    private static final Color RED_BTN = new Color(0xB33A3A);
    private static final Color PURPLE_TEXT = new Color(0x5B2A86);
    private static final Color DARK_TEXT = new Color(0x2B2B2B);

    // callback: avisa a tela "de fora" (Main) qual botão foi clicado
    private final Consumer<String> onNavegar;

    public MenuPrincipal(Consumer<String> onNavegar) {
        this.onNavegar = onNavegar;

        setLayout(new BorderLayout());
        setBackground(BG_LIGHT_BLUE);
        setBorder(new EmptyBorder(30, 40, 30, 40));

        add(buildTopo(), BorderLayout.NORTH);
        add(buildCorpo(), BorderLayout.CENTER);
    }

    // ---------- Logo no topo direito ----------
    private JPanel buildTopo() {
        JPanel topo = new JPanel(new BorderLayout());
        topo.setOpaque(false);

        URL logoUrl = getClass().getResource("/assets/logo.png");
        if (logoUrl != null) {
            ImageIcon icon = new ImageIcon(logoUrl);
            Image scaled = icon.getImage().getScaledInstance(500, 100, Image.SCALE_SMOOTH);
            JLabel lblLogo = new JLabel(new ImageIcon(scaled));
            topo.add(lblLogo, BorderLayout.EAST);
        }
        return topo;
    }

    // ---------- Corpo: lado esquerdo (botões + frase) + aliens ocupando o resto ----------
    private JPanel buildCorpo() {
        JPanel corpo = new JPanel(new BorderLayout());
        corpo.setOpaque(false);

        JPanel ladoEsquerdo = new JPanel(new BorderLayout());
        ladoEsquerdo.setOpaque(false);
        ladoEsquerdo.add(buildColunas(), BorderLayout.NORTH); // botões colados no topo
        ladoEsquerdo.add(buildRodape(), BorderLayout.SOUTH); // frase logo abaixo dos botões

        corpo.add(ladoEsquerdo, BorderLayout.CENTER);
        corpo.add(buildImagemAliens(), BorderLayout.EAST); // ocupa todo o resto, até o fim da janela

        return corpo;
    }

    // ---------- As duas colunas de botões, alinhadas no topo ----------
    private JPanel buildColunas() {
        JPanel colunas = new JPanel(new GridBagLayout());
        colunas.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.NORTHWEST; // trava as duas colunas no topo, não no centro
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 0, 20);

        JPanel colunaEsquerda = new JPanel();
        colunaEsquerda.setOpaque(false);
        colunaEsquerda.setLayout(new BoxLayout(colunaEsquerda, BoxLayout.Y_AXIS));
        colunaEsquerda.add(menuButton("CARDÁPIO", YELLOW_BTN, DARK_TEXT));
        colunaEsquerda.add(Box.createVerticalStrut(12));
        colunaEsquerda.add(menuButton("ESTOQUE", YELLOW_BTN, DARK_TEXT));
        colunaEsquerda.add(Box.createVerticalStrut(12));
        colunaEsquerda.add(menuButton("CLIENTES", YELLOW_BTN, DARK_TEXT));
        colunaEsquerda.add(Box.createVerticalStrut(12));
        colunaEsquerda.add(menuButton("PEDIDOS", YELLOW_BTN, DARK_TEXT));
        colunaEsquerda.add(Box.createVerticalStrut(12));
        colunaEsquerda.add(menuButton("RELATÓRIOS", YELLOW_BTN, DARK_TEXT));
        colunaEsquerda.add(Box.createVerticalStrut(12));
        colunaEsquerda.add(menuButton("SAIR", RED_BTN, Color.WHITE));

        JPanel colunaDireita = new JPanel();
        colunaDireita.setOpaque(false);
        colunaDireita.setLayout(new BoxLayout(colunaDireita, BoxLayout.Y_AXIS));
        colunaDireita.add(menuButton("FUNCIONÁRIOS", YELLOW_BTN, DARK_TEXT));
        colunaDireita.add(Box.createVerticalStrut(12));
        colunaDireita.add(menuButton("NOVO PEDIDO", GREEN_BTN, DARK_TEXT));

        gbc.gridx = 0;
        colunas.add(colunaEsquerda, gbc);
        gbc.gridx = 1;
        colunas.add(colunaDireita, gbc);

        return colunas;
    }

    // ---------- Imagem dos aliens, ancorada no fim da área disponível ----------
    private JPanel buildImagemAliens() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);

        JLabel lbl = new JLabel();
        URL imgUrl = getClass().getResource("/assets/aliens.png"); // 577x433px
        if (imgUrl != null) {
            ImageIcon icon = new ImageIcon(imgUrl);
            Image scaled = icon.getImage().getScaledInstance(850, 690, Image.SCALE_SMOOTH);
            lbl.setIcon(new ImageIcon(scaled));
        }

        wrapper.add(lbl, BorderLayout.SOUTH); // ancora embaixo do espaço disponível
        return wrapper;
    }

    // ---------- Frase, agora dentro da coluna esquerda ----------
    private JLabel buildRodape() {
        JLabel lbl = new JLabel("Sua pizza de outra galáxia!");
        lbl.setFont(FonteUtil.carregarFonte(POPPINS, 40f).deriveFont(Font.ITALIC));
        lbl.setForeground(PURPLE_TEXT);
        lbl.setBorder(new EmptyBorder(30, 0, 0, 0)); // respiro entre os botões e a frase
        return lbl;
    }

    // ---------- Botão do menu (reaproveitável, tamanho fixo) ----------
    private JButton menuButton(String texto, Color corFundo, Color corTexto) {
        JButton btn = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(corFundo);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(FonteUtil.carregarFonte(POPPINS, 25f).deriveFont(Font.BOLD));
        btn.setForeground(corTexto);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setBorder(new EmptyBorder(12, 30, 12, 30));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // tamanho fixo igual pra todos, independente do texto
        Dimension tamanhoFixo = new Dimension(300, 65);
        btn.setPreferredSize(tamanhoFixo);
        btn.setMaximumSize(tamanhoFixo);
        btn.setMinimumSize(tamanhoFixo);

        // avisa quem criou essa tela qual botão foi clicado
        btn.addActionListener(e -> onNavegar.accept(texto));

        return btn;
    }
}