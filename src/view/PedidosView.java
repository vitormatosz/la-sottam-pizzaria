package view;

import dao.PedidoDAO;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.net.URL;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import model.ItemPedido;
import model.Pedido;

public class PedidosView extends JPanel {

    private static final String AUDIOWIDE = "Audiowide-Regular.ttf";
    private static final String POPPINS   = "Poppins-Regular.ttf";

    private static final Color BG_LIGHT_BLUE   = new Color(0xC7EAF7);
    private static final Color PURPLE_CARD     = new Color(0x5B2A86);
    private static final Color GREEN_ACCENT    = new Color(0x6FCF52);
    private static final Color WHITE           = Color.WHITE;

    private static final NumberFormat MOEDA = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
    private static final SimpleDateFormat DATA_FMT = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    private final Runnable onVoltarAction;
    private final PedidoDAO pedidoDAO = new PedidoDAO();

    // Componentes de interface
    private JTable tabelaPedidos;
    private DefaultTableModel tableModel;
    private RoundedTextField buscaField;

    private JLabel labelClienteResumo;
    private JLabel labelSaidaResumo;
    private JLabel labelFormaPagResumo;
    private JLabel labelDataResumo;
    private JLabel labelFreteResumo;
    private JLabel labelTotalResumo;
    private DefaultListModel<String> modeloItensResumo = new DefaultListModel<>();
    private JList<String> listaItensResumo;

    public PedidosView() {
        this(null);
    }

    public PedidosView(Runnable onVoltarAction) {
        this.onVoltarAction = onVoltarAction;

        setLayout(new BorderLayout());
        setBackground(BG_LIGHT_BLUE);
        setBorder(new EmptyBorder(20, 30, 20, 30));

        add(buildHeader(), BorderLayout.NORTH);
        add(buildBody(), BorderLayout.CENTER);

        carregarDadosTabela(null);
    }

    // ---------- Cabeçalho (igual ClientesView) ----------
    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(0, 0, 15, 0));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        left.setOpaque(false);

        JLabel pedidosLabel = new JLabel("PEDIDOS");
        pedidosLabel.setFont(FonteUtil.carregarFonte(AUDIOWIDE, 64f));
        pedidosLabel.setForeground(PURPLE_CARD);
        pedidosLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, PURPLE_CARD));

        JButton backBtn = makeCircleButton("<");
        backBtn.addActionListener(e -> {
            if (onVoltarAction != null) {
                onVoltarAction.run();
            }
        });

        left.add(pedidosLabel);
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

                g2.setColor(GREEN_ACCENT);
                g2.fillOval(0, 0, getWidth(), getHeight());

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
        btn.setFont(FonteUtil.carregarFonte(AUDIOWIDE, 20f));
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // ---------- Corpo Principal (igual estrutura da ClientesView) ----------
    private JPanel buildBody() {
        JPanel body = new JPanel(new GridBagLayout());
        body.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 10, 5, 10);

        // Lado Esquerdo: Tabela de pedidos
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        body.add(buildTablePanel(), gbc);

        // Lado Direito: Resumo do pedido selecionado + busca
        gbc.gridx = 1;
        gbc.weightx = 0.0;
        gbc.weighty = 1.0;

        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setOpaque(false);

        GridBagConstraints gbcRight = new GridBagConstraints();
        gbcRight.gridx = 0;
        gbcRight.fill = GridBagConstraints.HORIZONTAL;
        gbcRight.anchor = GridBagConstraints.NORTH;
        gbcRight.insets = new Insets(0, 0, 15, 0);

        gbcRight.gridy = 0;
        gbcRight.weighty = 0.0;
        rightPanel.add(buildResumoCard(), gbcRight);

        gbcRight.gridy = 1;
        gbcRight.weighty = 1.0;
        gbcRight.insets = new Insets(0, 0, 0, 0);
        rightPanel.add(buildSearchCard(), gbcRight);

        body.add(rightPanel, gbc);

        return body;
    }

    // ---------- Tabela de pedidos ----------
    private JPanel buildTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        String[] colunas = {"ID", "Cliente", "Saída", "Data", "Total"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelaPedidos = new JTable(tableModel);
        tabelaPedidos.setFont(FonteUtil.carregarFonte(POPPINS, 14f));
        tabelaPedidos.setRowHeight(32);
        tabelaPedidos.getTableHeader().setFont(FonteUtil.carregarFonte(POPPINS, 15f));
        tabelaPedidos.getTableHeader().setBackground(WHITE);
        tabelaPedidos.getTableHeader().setForeground(PURPLE_CARD);
        tabelaPedidos.setShowGrid(true);
        tabelaPedidos.setGridColor(Color.BLACK);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < tabelaPedidos.getColumnCount(); i++) {
            tabelaPedidos.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Clicar num pedido carrega o resumo completo (igual clicar em um cliente)
        tabelaPedidos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tabelaPedidos.getSelectedRow();
                if (row != -1) {
                    int id = (Integer) tableModel.getValueAt(row, 0);
                    carregarResumoPedido(id);
                }
            }
        });

        JScrollPane scroll = new JScrollPane(tabelaPedidos);
        scroll.getViewport().setBackground(WHITE);
        scroll.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    // ---------- Card de Resumo (ocupa o lugar do card de cadastro/edição) ----------
    private RoundedPanel buildResumoCard() {
        RoundedPanel card = new RoundedPanel(25, PURPLE_CARD);

        Dimension formSize = new Dimension(360, 400);
        card.setPreferredSize(formSize);
        card.setMaximumSize(formSize);

        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(15, 20, 15, 20));

        card.add(sectionTitle("RESUMO DO PEDIDO"));
        card.add(Box.createVerticalStrut(10));

        listaItensResumo = new JList<>(modeloItensResumo);
        listaItensResumo.setFont(FonteUtil.carregarFonte(POPPINS, 13f));
        listaItensResumo.setBackground(WHITE);
        JScrollPane scrollItens = new JScrollPane(listaItensResumo);
        scrollItens.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollItens.setMaximumSize(new Dimension(Integer.MAX_VALUE, 140));
        card.add(scrollItens);
        card.add(Box.createVerticalStrut(10));

        labelClienteResumo = fieldLabel("CLIENTE: -");
        card.add(labelClienteResumo);
        card.add(Box.createVerticalStrut(4));

        labelSaidaResumo = fieldLabel("SAÍDA: -");
        card.add(labelSaidaResumo);
        card.add(Box.createVerticalStrut(4));

        labelFormaPagResumo = fieldLabel("PAGAMENTO: -");
        card.add(labelFormaPagResumo);
        card.add(Box.createVerticalStrut(4));

        labelDataResumo = fieldLabel("DATA: -");
        card.add(labelDataResumo);
        card.add(Box.createVerticalStrut(8));

        labelFreteResumo = fieldLabel("FRETE: " + MOEDA.format(0));
        card.add(labelFreteResumo);
        card.add(Box.createVerticalStrut(4));

        labelTotalResumo = new JLabel("TOTAL: " + MOEDA.format(0));
        labelTotalResumo.setFont(FonteUtil.carregarFonte(AUDIOWIDE, 18f));
        labelTotalResumo.setForeground(GREEN_ACCENT);
        labelTotalResumo.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(labelTotalResumo);

        return card;
    }

    // ---------- Card de Busca (igual ClientesView) ----------
    private RoundedPanel buildSearchCard() {
        RoundedPanel card = new RoundedPanel(25, PURPLE_CARD);

        Dimension searchSize = new Dimension(360, 110);
        card.setPreferredSize(searchSize);
        card.setMaximumSize(searchSize);

        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(12, 20, 12, 20));

        JLabel title = sectionTitle("PESQUISAR PEDIDOS");
        card.add(title);
        card.add(Box.createVerticalStrut(8));

        buscaField = new RoundedTextField(20);
        buscaField.setToolTipText("Digite o nome do cliente");

        buscaField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                carregarDadosTabela(buscaField.getText().trim());
            }
        });

        card.add(buscaField);
        return card;
    }

    // ---------- Operações com Banco de Dados / DAO ----------

    private void carregarDadosTabela(String filtro) {
        tableModel.setRowCount(0);
        List<Pedido> lista = pedidoDAO.listarTodos();

        for (Pedido p : lista) {
            String nomeCliente = p.getCliente() != null ? p.getCliente().getNome() : "-";

            if (filtro != null && !filtro.isEmpty()) {
                if (!nomeCliente.toLowerCase().contains(filtro.toLowerCase())) {
                    continue;
                }
            }

            double total = calcularTotal(p);
            tableModel.addRow(new Object[]{
                p.getId(),
                nomeCliente,
                p.getTipoDeSaida(),
                DATA_FMT.format(p.getDataPedido()),
                MOEDA.format(total)
            });
        }
    }

    private void carregarResumoPedido(int id) {
        Pedido pedido = pedidoDAO.buscarPorId(id);
        if (pedido == null) {
            return;
        }

        modeloItensResumo.clear();
        for (ItemPedido item : pedido.getItens()) {
            String nomeProduto = item.isMeioAMeio()
                    ? item.getProduto().getNome() + " / " + item.getSegundoSabor().getNome()
                    : item.getProduto().getNome();
            modeloItensResumo.addElement(nomeProduto + " (" + item.getTamanho() + ") x" + item.getQuantidade()
                    + " — " + MOEDA.format(item.calcularSubtotal()));
        }

        labelClienteResumo.setText("CLIENTE: " + (pedido.getCliente() != null ? pedido.getCliente().getNome() : "-"));
        labelSaidaResumo.setText("SAÍDA: " + pedido.getTipoDeSaida());
        labelFormaPagResumo.setText("PAGAMENTO: " + pedido.getFormaPag());
        labelDataResumo.setText("DATA: " + DATA_FMT.format(pedido.getDataPedido()));
        labelFreteResumo.setText("FRETE: " + MOEDA.format(pedido.getFrete()));
        labelTotalResumo.setText("TOTAL: " + MOEDA.format(calcularTotal(pedido)));
    }

    private double calcularTotal(Pedido pedido) {
        double subtotal = 0;
        for (ItemPedido item : pedido.getItens()) {
            subtotal += item.calcularSubtotal();
        }
        return subtotal + pedido.getFrete();
    }

    private JLabel sectionTitle(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FonteUtil.carregarFonte(AUDIOWIDE, 20f));
        label.setForeground(GREEN_ACCENT);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JLabel fieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FonteUtil.carregarFonte(POPPINS, 15f));
        label.setForeground(WHITE);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    // ---------- Componentes Gráficos Customizados (iguais ClientesView) ----------

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
}