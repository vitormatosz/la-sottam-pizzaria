package view;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.net.URL;
import java.util.function.Consumer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class MenuPrincipal extends JPanel {

    private static final String AUDIOWIDE = "Audiowide-Regular.ttf";
    private static final String POPPINS = "Poppins-Regular.ttf";

    private static final Color BG_LIGHT_BLUE = new Color(0xC7EAF7);
    //private static final Color SIDEBAR_COLOR = new Color(0x252525);
    private static final Color SIDEBAR_HOVER = new Color(0x363636);

    //private static final Color YELLOW_BTN = new Color(0xF5C242);
    private static final Color GREEN_BTN = new Color(0x6FCF52);
    private static final Color RED_BTN = new Color(0xB33A3A);

    private static final Color PURPLE_TEXT = new Color(0x5B2A86);
    private static final Color DARK_TEXT = new Color(0x2B2B2B);
    private static final Color WHITE_TEXT = Color.WHITE;

    private final Consumer<String> onNavegar;

    public MenuPrincipal(Consumer<String> onNavegar) {
        this.onNavegar = onNavegar;

        setLayout(new BorderLayout());
        setBackground(BG_LIGHT_BLUE);

        add(buildSidebar(), BorderLayout.WEST);
        add(buildAreaPrincipal(), BorderLayout.CENTER);
    }

    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setBackground(PURPLE_TEXT);
        sidebar.setPreferredSize(new Dimension(270, 0));

        JPanel cabecalho = new JPanel();
        cabecalho.setLayout(new BoxLayout(cabecalho, BoxLayout.Y_AXIS));
        cabecalho.setOpaque(false);
        cabecalho.setBorder(new EmptyBorder(35, 25, 25, 25));

        JLabel nome = new JLabel("LA SOTTAM");
        nome.setAlignmentX(Component.LEFT_ALIGNMENT);
        nome.setFont(FonteUtil.carregarFonte(AUDIOWIDE, 28f).deriveFont(Font.BOLD));
        nome.setForeground(Color.WHITE);

        JLabel subtitulo = new JLabel("PIZZARIA");
        subtitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        subtitulo.setFont(FonteUtil.carregarFonte(AUDIOWIDE, 15f).deriveFont(Font.BOLD));
        subtitulo.setForeground(GREEN_BTN);

        cabecalho.add(nome);
        cabecalho.add(Box.createVerticalStrut(2));
        cabecalho.add(subtitulo);

        sidebar.add(cabecalho, BorderLayout.NORTH);

        JPanel menu = new JPanel();
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        menu.setOpaque(false);
        menu.setBorder(new EmptyBorder(10, 15, 10, 15));

        menu.add(menuButton("INÍCIO", BG_LIGHT_BLUE, PURPLE_TEXT, true));

        menu.add(Box.createVerticalStrut(25));
        menu.add(menuTitulo("GESTÃO"));

        menu.add(menuButton("CARDÁPIO", PURPLE_TEXT, WHITE_TEXT, false));
        menu.add(menuButton("ESTOQUE", PURPLE_TEXT, WHITE_TEXT, false));
        menu.add(menuButton("CLIENTES", PURPLE_TEXT, WHITE_TEXT, false));
        menu.add(menuButton("FUNCIONÁRIOS", PURPLE_TEXT, WHITE_TEXT, false));

        menu.add(Box.createVerticalStrut(20));
        menu.add(menuTitulo("VENDAS"));

        menu.add(menuButton("PEDIDOS", PURPLE_TEXT, WHITE_TEXT, false));
        menu.add(menuButton("NOVO PEDIDO", GREEN_BTN, DARK_TEXT, false));

        menu.add(Box.createVerticalStrut(20));
        menu.add(menuTitulo("SISTEMA"));

        menu.add(menuButton("RELATÓRIOS", PURPLE_TEXT, WHITE_TEXT, false));

        sidebar.add(menu, BorderLayout.CENTER);

        JPanel painelSair = new JPanel(new BorderLayout());
        painelSair.setOpaque(false);
        painelSair.setBorder(new EmptyBorder(10, 15, 25, 15));

        JButton sair = menuButton("SAIR", RED_BTN, Color.WHITE, false);
        painelSair.add(sair, BorderLayout.CENTER);

        sidebar.add(painelSair, BorderLayout.SOUTH);

        return sidebar;
    }

    private JLabel menuTitulo(String texto) {
        JLabel titulo = new JLabel(texto);

        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        titulo.setFont(FonteUtil.carregarFonte(POPPINS, 12f).deriveFont(Font.BOLD));
        titulo.setForeground(new Color(0xAAAAAA));
        titulo.setBorder(new EmptyBorder(0, 15, 7, 0));

        return titulo;
    }

    private JButton menuButton(String texto, Color corFundo, Color corTexto, boolean selecionado) {
        JButton btn = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();

                g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                Color corAtual = corFundo;

                if (getModel().isRollover()
                        && !selecionado
                        && corFundo.equals(PURPLE_TEXT)) {
                    corAtual = SIDEBAR_HOVER;
                }

                g2.setColor(corAtual);
                g2.fill(new RoundRectangle2D.Double(
                        0, 0, getWidth(), getHeight(), 14, 14));

                g2.dispose();

                super.paintComponent(g);
            }
        };

        btn.setFont(FonteUtil.carregarFonte(POPPINS, 15f).deriveFont(Font.BOLD));
        btn.setForeground(corTexto);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setBorder(new EmptyBorder(12, 18, 12, 18));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        Dimension tamanho = new Dimension(240, 48);
        btn.setPreferredSize(tamanho);
        btn.setMinimumSize(tamanho);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);

        btn.addActionListener(e -> onNavegar.accept(texto));

        return btn;
    }

    private JPanel buildAreaPrincipal() {
        JPanel principal = new JPanel(new BorderLayout());

        principal.setOpaque(false);
        principal.setBorder(new EmptyBorder(30, 40, 0, 40));

        principal.add(buildTopo(), BorderLayout.NORTH);
        principal.add(buildConteudo(), BorderLayout.CENTER);

        return principal;
    }

    //TOPO

    private JPanel buildTopo() {
        JPanel topo = new JPanel(new BorderLayout());
        topo.setOpaque(false);

        JPanel textos = new JPanel();
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
        textos.setOpaque(false);

        JLabel titulo = new JLabel("Olá! Bem-vindo à La Sottam Pizzaria.");
        titulo.setFont(FonteUtil.carregarFonte(POPPINS, 28f).deriveFont(Font.BOLD));
        titulo.setForeground(DARK_TEXT);

        JLabel descricao = new JLabel(
                "Gerencie de forma simples e rápida.");
        descricao.setFont(FonteUtil.carregarFonte(POPPINS, 15f));
        descricao.setForeground(new Color(0x666666));

        textos.add(titulo);
        textos.add(Box.createVerticalStrut(5));
        textos.add(descricao);

        topo.add(textos, BorderLayout.WEST);

        URL logoUrl = getClass().getResource("/assets/logo.png");

        if (logoUrl != null) {
            ImageIcon icon = new ImageIcon(logoUrl);
            Image scaled = icon.getImage().getScaledInstance(
                    300, 60, Image.SCALE_SMOOTH);

            JLabel lblLogo = new JLabel(new ImageIcon(scaled));
            topo.add(lblLogo, BorderLayout.EAST);
        }

        return topo;
    }

    //CARDS

    private JPanel buildConteudo() {
        JPanel conteudo = new JPanel(new BorderLayout());
        conteudo.setOpaque(false);
        conteudo.setBorder(new EmptyBorder(25, 0, 0, 0));

        JPanel cards = new JPanel(new GridLayout(1, 3, 20, 0));
        cards.setOpaque(false);

        cards.add(criarCard(
                "PEDIDOS",
                "Gerencie os pedidos realizados.",
                PURPLE_TEXT));

        cards.add(criarCard(
                "ESTOQUE",
                "Confira os produtos disponíveis.",
                PURPLE_TEXT));

        cards.add(criarCard(
                "CLIENTES",
                "Consulte os clientes cadastrados.",
                PURPLE_TEXT));

        conteudo.add(cards, BorderLayout.NORTH);

        JPanel rodape = new JPanel(new BorderLayout());
        rodape.setOpaque(false);

        rodape.add(buildRodape(), BorderLayout.WEST);
        rodape.add(buildImagemAliens(), BorderLayout.EAST);

        conteudo.add(rodape, BorderLayout.SOUTH);

        return conteudo;
    }

    private JPanel criarCard(String titulo, String descricao, Color cor) {
        JPanel card = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();

                g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(Color.WHITE);
                g2.fill(new RoundRectangle2D.Double(
                        0, 0, getWidth(), getHeight(), 20, 20));

                g2.dispose();

                super.paintComponent(g);
            }
        };

        card.setOpaque(false);
        card.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(
                FonteUtil.carregarFonte(POPPINS, 18f).deriveFont(Font.BOLD));
        lblTitulo.setForeground(cor);

        JLabel lblDescricao = new JLabel(
                "<html>" + descricao + "</html>");
        lblDescricao.setFont(FonteUtil.carregarFonte(POPPINS, 12f));
        lblDescricao.setForeground(new Color(0x666666));

        card.add(lblTitulo, BorderLayout.NORTH);
        card.add(lblDescricao, BorderLayout.CENTER);

        return card;
    }

    //IMAGEM ALIENS

    private JPanel buildImagemAliens() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);

        JLabel lbl = new JLabel();

        URL imgUrl = getClass().getResource("/assets/aliens.png");

        if (imgUrl != null) {
            ImageIcon icon = new ImageIcon(imgUrl);
            Image scaled = icon.getImage().getScaledInstance(650,500,Image.SCALE_SMOOTH);
            lbl.setIcon(new ImageIcon(scaled));
        }

        lbl.setHorizontalAlignment(SwingConstants.RIGHT);
        lbl.setVerticalAlignment(SwingConstants.BOTTOM);

        wrapper.add(lbl, BorderLayout.SOUTH);

        return wrapper;
    }

    //RODAPÉ

private JLabel buildRodape() {
    JLabel lbl = new JLabel("Sua pizza de outra galáxia!");

    lbl.setHorizontalAlignment(SwingConstants.LEFT);
    lbl.setVerticalAlignment(SwingConstants.BOTTOM);

    lbl.setFont(FonteUtil.carregarFonte(POPPINS,25f).deriveFont(Font.ITALIC));
    lbl.setForeground(PURPLE_TEXT);
    lbl.setBorder(new EmptyBorder(0,0,15,0));

    return lbl;
}}