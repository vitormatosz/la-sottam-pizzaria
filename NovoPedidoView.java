package view;

import dao.ClienteDAO;
import dao.PedidoDAO;
import dao.ProdutoDAO;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import model.Cliente;
import model.ItemPedido;
import model.Pedido;
import model.Produto;
import model.Tamanho;
import model.TipoSaida;

public class NovoPedidoView extends JPanel {

    private static final String AUDIOWIDE = "Audiowide-Regular.ttf";
    private static final String POPPINS   = "Poppins-Regular.ttf";

    private static final Color BG_LIGHT_BLUE   = new Color(0xC7EAF7);
    private static final Color PURPLE_CARD     = new Color(0x5B2A86);
    private static final Color GREEN_ACCENT    = new Color(0x6FCF52);
    private static final Color GREEN_BTN       = new Color(0x3F7D3A);
    private static final Color GREEN_BTN_HOVER = new Color(0x356B31);
    private static final Color ORANGE_BTN      = new Color(0xC97A1B);
    private static final Color ORANGE_BTN_HOVER= new Color(0xA6640F);
    private static final Color RED_BTN         = new Color(0xA02323);
    private static final Color RED_BTN_HOVER   = new Color(0x821C1C);
    private static final Color WHITE           = Color.WHITE;

    private static final NumberFormat MOEDA = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

    private final Runnable onVoltarAction;

    // ---------- Navegação interna (Montar pedido -> Resumo -> Conclusão) ----------
    private final CardLayout stepLayout = new CardLayout();
    private final JPanel stepContainer = new JPanel(stepLayout);
    private JLabel tituloHeader;

    // ---------- DAOs ----------
    private final ProdutoDAO produtoDAO = new ProdutoDAO();
    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final PedidoDAO pedidoDAO = new PedidoDAO();

    // ---------- Estado do pedido em construção ----------
    private final List<ItemPedido> carrinho = new ArrayList<>();
    private Cliente clienteSelecionado;
    private Produto produtoSelecionado;
    private Produto segundoSaborSelecionado;
    private Tamanho tamanhoSelecionado = Tamanho.MEDIA;
    private boolean meioAMeioAtivo = false;
    private TipoSaida tipoSaidaSelecionado = TipoSaida.ENTREGA;

    // ---------- Componentes: catálogo de produtos ----------
    private JTable tabelaProdutos;
    private DefaultTableModel modeloProdutos;

    // ---------- Componentes: formulário do item ----------
    private JButton meioAMeioBtn;
    private JLabel labelSabor1;
    private RoundedTextField campoValorProduto1;
    private JPanel blocoSabor2;
    private JLabel labelSabor2;
    private RoundedTextField campoValorProduto2;
    private RoundedTextField campoQuantidade;
    private RoundedTextField campoObservacoes;
    private RoundedButton btnPequena, btnMedia, btnGrande;
    private RoundedTextField campoBuscaCliente;
    private JLabel labelClienteSelecionado;
    private DefaultListModel<String> modeloResultadoClientes = new DefaultListModel<>();
    private JList<String> listaResultadoClientes;
    private List<Cliente> resultadosClientes = new ArrayList<>();

    // ---------- Componentes: carrinho ----------
    private DefaultListModel<String> modeloCarrinho = new DefaultListModel<>();
    private JList<String> listaCarrinho;
    private JLabel labelSubtotalCarrinho;
    private JLabel labelClienteCarrinho;

    // ---------- Componentes: resumo ----------
    private DefaultListModel<String> modeloResumo = new DefaultListModel<>();
    private JList<String> listaResumo;
    private JLabel labelClienteResumo;
    private JLabel labelFreteResumo;
    private JLabel labelTotalResumo;
    private RoundedButton btnEntrega, btnRetirada;
    private JComboBox<String> comboFormaPag;
    private RoundedTextField campoObservacaoGeral;

    public NovoPedidoView() {
        this(null);
    }

    public NovoPedidoView(Runnable onVoltarAction) {
        this.onVoltarAction = onVoltarAction;

        setLayout(new BorderLayout());
        setBackground(BG_LIGHT_BLUE);
        setBorder(new EmptyBorder(20, 30, 20, 30));

        add(buildHeader(), BorderLayout.NORTH);

        stepContainer.setOpaque(false);
        stepContainer.add(buildMontarPedidoStep(), "montar");
        stepContainer.add(buildResumoStep(), "resumo");
        stepContainer.add(buildConclusaoStep(), "conclusao");
        add(stepContainer, BorderLayout.CENTER);

        carregarTabelaProdutos();
        irParaEtapa("montar");
    }

    private void irParaEtapa(String nome) {
        stepLayout.show(stepContainer, nome);
        if ("montar".equals(nome)) {
            tituloHeader.setText("NOVO PEDIDO");
        } else {
            tituloHeader.setText("RESUMO");
        }
    }

    // ---------- Cabeçalho ----------
    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(0, 0, 15, 0));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        left.setOpaque(false);

        tituloHeader = new JLabel("NOVO PEDIDO");
        tituloHeader.setFont(FonteUtil.carregarFonte(AUDIOWIDE, 56f));
        tituloHeader.setForeground(PURPLE_CARD);
        tituloHeader.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, PURPLE_CARD));

        JButton backBtn = makeCircleButton("<");
        backBtn.addActionListener(e -> {
            if (onVoltarAction != null) {
                onVoltarAction.run();
            }
        });

        left.add(tituloHeader);
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

    // =====================================================================
    // ETAPA 1 — MONTAR PEDIDO
    // =====================================================================
    private JPanel buildMontarPedidoStep() {
        JPanel body = new JPanel(new GridBagLayout());
        body.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.weighty = 1.0;

        // Coluna 1: catálogo de produtos (clicável, igual clientes)
        gbc.gridx = 0;
        gbc.weightx = 1.0;
        body.add(buildCatalogoPanel(), gbc);

        // Coluna 2: formulário do item
        gbc.gridx = 1;
        gbc.weightx = 0.0;
        body.add(buildFormItemCard(), gbc);

        // Coluna 3: carrinho / produtos do pedido
        gbc.gridx = 2;
        gbc.weightx = 0.6;
        body.add(buildCarrinhoPanel(), gbc);

        return body;
    }

    // ---------- Catálogo (igual padrão da tabela de clientes) ----------
    private JPanel buildCatalogoPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        JLabel titulo = sectionTitleDark("PRODUTOS DISPONÍVEIS");
        panel.add(titulo, BorderLayout.NORTH);

        String[] colunas = {"ID", "Nome", "Categoria", "Preço"};
        modeloProdutos = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelaProdutos = new JTable(modeloProdutos);
        tabelaProdutos.setFont(FonteUtil.carregarFonte(POPPINS, 14f));
        tabelaProdutos.setRowHeight(32);
        tabelaProdutos.getTableHeader().setFont(FonteUtil.carregarFonte(POPPINS, 15f));
        tabelaProdutos.getTableHeader().setBackground(WHITE);
        tabelaProdutos.getTableHeader().setForeground(PURPLE_CARD);
        tabelaProdutos.setShowGrid(true);
        tabelaProdutos.setGridColor(Color.BLACK);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < tabelaProdutos.getColumnCount(); i++) {
            tabelaProdutos.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Clicar em um produto da lista seleciona ele para o item (igual clicar em um cliente)
        tabelaProdutos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tabelaProdutos.getSelectedRow();
                if (row != -1) {
                    int id = (Integer) modeloProdutos.getValueAt(row, 0);
                    Produto produto = produtoDAO.buscarPorId(id);
                    selecionarProdutoClicado(produto);
                }
            }
        });

        JScrollPane scroll = new JScrollPane(tabelaProdutos);
        scroll.getViewport().setBackground(WHITE);
        scroll.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private void carregarTabelaProdutos() {
        modeloProdutos.setRowCount(0);
        List<Produto> produtos = produtoDAO.listarTodos();
        for (Produto p : produtos) {
            if (!p.isDisponivel()) {
                continue;
            }
            modeloProdutos.addRow(new Object[]{p.getId(), p.getNome(), p.getCategoria(), MOEDA.format(p.getPreco())});
        }
    }

    // Preenche o "sabor 1" e, se o meio a meio estiver ativo, depois o "sabor 2"
    private void selecionarProdutoClicado(Produto produto) {
        if (produto == null) {
            return;
        }
        if (produtoSelecionado == null) {
            produtoSelecionado = produto;
            labelSabor1.setText(produto.getId() + " - " + produto.getNome());
            campoValorProduto1.setText(MOEDA.format(produto.getPreco()));
        } else if (meioAMeioAtivo && segundoSaborSelecionado == null) {
            segundoSaborSelecionado = produto;
            labelSabor2.setText(produto.getId() + " - " + produto.getNome());
            campoValorProduto2.setText(MOEDA.format(produto.getPreco()));
        } else {
            // já tem os sabores necessários preenchidos: reinicia a partir do clicado
            produtoSelecionado = produto;
            segundoSaborSelecionado = null;
            labelSabor1.setText(produto.getId() + " - " + produto.getNome());
            campoValorProduto1.setText(MOEDA.format(produto.getPreco()));
            labelSabor2.setText("-");
            campoValorProduto2.setText("");
        }
    }

    // ---------- Formulário do item (card roxo) ----------
    private RoundedPanel buildFormItemCard() {
        RoundedPanel card = new RoundedPanel(25, PURPLE_CARD);
        Dimension formSize = new Dimension(360, 560);
        card.setPreferredSize(formSize);
        card.setMaximumSize(formSize);

        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(15, 20, 15, 20));

        JPanel tituloLinha = new JPanel(new BorderLayout());
        tituloLinha.setOpaque(false);
        tituloLinha.setAlignmentX(Component.LEFT_ALIGNMENT);
        tituloLinha.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        tituloLinha.add(sectionTitle("PEDIDO"), BorderLayout.WEST);

        meioAMeioBtn = makeCircleButton("½");
        meioAMeioBtn.setToolTipText("Marcar como pizza meio a meio");
        meioAMeioBtn.addActionListener(e -> alternarMeioAMeio());
        JPanel infoWrap = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        infoWrap.setOpaque(false);
        infoWrap.add(meioAMeioBtn);
        tituloLinha.add(infoWrap, BorderLayout.EAST);

        card.add(tituloLinha);
        card.add(Box.createVerticalStrut(10));

        card.add(fieldLabel("SABOR 1"));
        card.add(Box.createVerticalStrut(2));
        labelSabor1 = valorInfo("Clique em um produto ao lado");
        card.add(labelSabor1);
        card.add(Box.createVerticalStrut(4));
        campoValorProduto1 = new RoundedTextField(20);
        campoValorProduto1.setEditable(false);
        campoValorProduto1.putClientProperty("placeholder", "VALOR DO PRODUTO");
        card.add(fieldLabel("VALOR DO PRODUTO"));
        card.add(Box.createVerticalStrut(2));
        card.add(campoValorProduto1);
        card.add(Box.createVerticalStrut(8));

        blocoSabor2 = new JPanel();
        blocoSabor2.setLayout(new BoxLayout(blocoSabor2, BoxLayout.Y_AXIS));
        blocoSabor2.setOpaque(false);
        blocoSabor2.setAlignmentX(Component.LEFT_ALIGNMENT);
        blocoSabor2.add(fieldLabel("SABOR 2 (meio a meio)"));
        blocoSabor2.add(Box.createVerticalStrut(2));
        labelSabor2 = valorInfo("Clique em outro produto ao lado");
        blocoSabor2.add(labelSabor2);
        blocoSabor2.add(Box.createVerticalStrut(4));
        campoValorProduto2 = new RoundedTextField(20);
        campoValorProduto2.setEditable(false);
        blocoSabor2.add(campoValorProduto2);
        blocoSabor2.add(Box.createVerticalStrut(8));
        blocoSabor2.setVisible(false);
        card.add(blocoSabor2);

        card.add(fieldLabel("QUANTIDADE"));
        card.add(Box.createVerticalStrut(2));
        campoQuantidade = new RoundedTextField(20);
        campoQuantidade.setText("1");
        card.add(campoQuantidade);
        card.add(Box.createVerticalStrut(8));

        card.add(fieldLabel("OBSERVAÇÕES"));
        card.add(Box.createVerticalStrut(2));
        campoObservacoes = new RoundedTextField(20);
        card.add(campoObservacoes);
        card.add(Box.createVerticalStrut(8));

        card.add(fieldLabel("TAMANHO"));
        card.add(Box.createVerticalStrut(4));
        JPanel tamanhoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        tamanhoPanel.setOpaque(false);
        tamanhoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnPequena = new RoundedButton("P");
        btnMedia = new RoundedButton("M");
        btnGrande = new RoundedButton("G");
        btnPequena.addActionListener(e -> selecionarTamanho(Tamanho.PEQUENA));
        btnMedia.addActionListener(e -> selecionarTamanho(Tamanho.MEDIA));
        btnGrande.addActionListener(e -> selecionarTamanho(Tamanho.GRANDE));
        tamanhoPanel.add(btnPequena);
        tamanhoPanel.add(btnMedia);
        tamanhoPanel.add(btnGrande);
        card.add(tamanhoPanel);
        selecionarTamanho(Tamanho.MEDIA);
        card.add(Box.createVerticalStrut(10));

        card.add(fieldLabel("CLIENTE"));
        card.add(Box.createVerticalStrut(2));
        campoBuscaCliente = new RoundedTextField(20);
        campoBuscaCliente.setToolTipText("Digite o nome do cliente");
        campoBuscaCliente.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                buscarClientes(campoBuscaCliente.getText().trim());
            }
        });
        card.add(campoBuscaCliente);
        card.add(Box.createVerticalStrut(4));

        listaResultadoClientes = new JList<>(modeloResultadoClientes);
        listaResultadoClientes.setFont(FonteUtil.carregarFonte(POPPINS, 13f));
        listaResultadoClientes.setVisibleRowCount(3);
        listaResultadoClientes.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int idx = listaResultadoClientes.getSelectedIndex();
                if (idx != -1 && idx < resultadosClientes.size()) {
                    clienteSelecionado = resultadosClientes.get(idx);
                    labelClienteSelecionado.setText("Selecionado: " + clienteSelecionado.getNome());
                    modeloResultadoClientes.clear();
                    campoBuscaCliente.setText(clienteSelecionado.getNome());
                }
            }
        });
        JScrollPane scrollClientes = new JScrollPane(listaResultadoClientes);
        scrollClientes.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        scrollClientes.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(scrollClientes);
        card.add(Box.createVerticalStrut(4));

        labelClienteSelecionado = fieldLabel("Nenhum cliente selecionado");
        labelClienteSelecionado.setFont(FonteUtil.carregarFonte(POPPINS, 12f));
        card.add(labelClienteSelecionado);
        card.add(Box.createVerticalStrut(10));

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        btnPanel.setOpaque(false);
        btnPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        RoundedButton limparBtn = new RoundedButton("LIMPAR");
        limparBtn.addActionListener(e -> limparFormularioItem());

        RoundedButton avancarBtn = new RoundedButton("AVANÇAR >");
        avancarBtn.addActionListener(e -> adicionarItemAoCarrinho());

        btnPanel.add(limparBtn);
        btnPanel.add(avancarBtn);
        card.add(btnPanel);

        return card;
    }

    private JLabel valorInfo(String textoPadrao) {
        JLabel label = new JLabel(textoPadrao);
        label.setFont(FonteUtil.carregarFonte(POPPINS, 13f));
        label.setForeground(GREEN_ACCENT);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private void alternarMeioAMeio() {
        meioAMeioAtivo = !meioAMeioAtivo;
        blocoSabor2.setVisible(meioAMeioAtivo);
        segundoSaborSelecionado = null;
        labelSabor2.setText("Clique em outro produto ao lado");
        campoValorProduto2.setText("");
        meioAMeioBtn.setToolTipText(meioAMeioAtivo ? "Meio a meio ativado (clique para desativar)"
                : "Marcar como pizza meio a meio");
        revalidate();
        repaint();
    }

    private void selecionarTamanho(Tamanho tamanho) {
        tamanhoSelecionado = tamanho;
        btnPequena.setSelecionado(tamanho == Tamanho.PEQUENA);
        btnMedia.setSelecionado(tamanho == Tamanho.MEDIA);
        btnGrande.setSelecionado(tamanho == Tamanho.GRANDE);
    }

    private void buscarClientes(String filtro) {
        modeloResultadoClientes.clear();
        resultadosClientes.clear();
        if (filtro.isEmpty()) {
            return;
        }
        List<Cliente> todos = clienteDAO.listarTodos();
        String f = filtro.toLowerCase();
        for (Cliente c : todos) {
            if (c.getNome() != null && c.getNome().toLowerCase().contains(f)) {
                resultadosClientes.add(c);
                modeloResultadoClientes.addElement(c.getNome() + " - " + c.getNumeroTel());
            }
        }
    }

    private void limparFormularioItem() {
        produtoSelecionado = null;
        segundoSaborSelecionado = null;
        meioAMeioAtivo = false;
        blocoSabor2.setVisible(false);
        labelSabor1.setText("Clique em um produto ao lado");
        labelSabor2.setText("Clique em outro produto ao lado");
        campoValorProduto1.setText("");
        campoValorProduto2.setText("");
        campoQuantidade.setText("1");
        campoObservacoes.setText("");
        selecionarTamanho(Tamanho.MEDIA);
        tabelaProdutos.clearSelection();
        revalidate();
        repaint();
    }

    private void adicionarItemAoCarrinho() {
        if (produtoSelecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um produto na lista ao lado.");
            return;
        }
        if (meioAMeioAtivo && segundoSaborSelecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione o segundo sabor para o meio a meio.");
            return;
        }
        if (clienteSelecionado == null) {
            JOptionPane.showMessageDialog(this, "Busque e selecione o cliente do pedido.");
            return;
        }

        int quantidade;
        try {
            quantidade = Integer.parseInt(campoQuantidade.getText().trim());
            if (quantidade <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Informe uma quantidade válida.");
            return;
        }

        ItemPedido item = meioAMeioAtivo
                ? new ItemPedido(produtoSelecionado, segundoSaborSelecionado, tamanhoSelecionado, quantidade)
                : new ItemPedido(produtoSelecionado, tamanhoSelecionado, quantidade);

        carrinho.add(item);
        atualizarListaCarrinho();
        limparFormularioItem();
    }

    // ---------- Carrinho / Produtos do Pedido ----------
    private RoundedPanel buildCarrinhoPanel() {
        RoundedPanel card = new RoundedPanel(25, PURPLE_CARD);
        card.setLayout(new BorderLayout(0, 10));
        card.setBorder(new EmptyBorder(15, 20, 15, 20));

        JPanel topo = new JPanel();
        topo.setLayout(new BoxLayout(topo, BoxLayout.Y_AXIS));
        topo.setOpaque(false);
        topo.add(sectionTitle("PRODUTOS PEDIDO"));
        labelClienteCarrinho = fieldLabel("Cliente: -");
        labelClienteCarrinho.setFont(FonteUtil.carregarFonte(POPPINS, 13f));
        topo.add(Box.createVerticalStrut(4));
        topo.add(labelClienteCarrinho);
        card.add(topo, BorderLayout.NORTH);

        listaCarrinho = new JList<>(modeloCarrinho);
        listaCarrinho.setFont(FonteUtil.carregarFonte(POPPINS, 13f));
        JScrollPane scroll = new JScrollPane(listaCarrinho);
        scroll.getViewport().setBackground(WHITE);
        card.add(scroll, BorderLayout.CENTER);

        JPanel rodape = new JPanel();
        rodape.setLayout(new BoxLayout(rodape, BoxLayout.Y_AXIS));
        rodape.setOpaque(false);

        labelSubtotalCarrinho = fieldLabel("SUBTOTAL: " + MOEDA.format(0));
        labelSubtotalCarrinho.setFont(FonteUtil.carregarFonte(POPPINS, 16f));
        rodape.add(labelSubtotalCarrinho);
        rodape.add(Box.createVerticalStrut(8));

        JPanel btnLinha = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        btnLinha.setOpaque(false);
        btnLinha.setAlignmentX(Component.LEFT_ALIGNMENT);

        RoundedButton removerBtn = new RoundedButton("REMOVER", RED_BTN, RED_BTN_HOVER);
        removerBtn.addActionListener(e -> removerItemSelecionado());

        RoundedButton finalizarBtn = new RoundedButton("FINALIZAR ✓");
        finalizarBtn.addActionListener(e -> irParaResumo());

        btnLinha.add(removerBtn);
        btnLinha.add(finalizarBtn);
        rodape.add(btnLinha);

        card.add(rodape, BorderLayout.SOUTH);
        return card;
    }

    private void removerItemSelecionado() {
        int idx = listaCarrinho.getSelectedIndex();
        if (idx != -1) {
            carrinho.remove(idx);
            atualizarListaCarrinho();
        }
    }

    private void atualizarListaCarrinho() {
        modeloCarrinho.clear();
        double subtotal = 0;
        for (ItemPedido item : carrinho) {
            modeloCarrinho.addElement(descreverItem(item));
            subtotal += item.calcularSubtotal();
        }
        labelSubtotalCarrinho.setText("SUBTOTAL: " + MOEDA.format(subtotal));
        labelClienteCarrinho.setText("Cliente: " + (clienteSelecionado != null ? clienteSelecionado.getNome() : "-"));
    }

    private String descreverItem(ItemPedido item) {
        String nomeProduto = item.isMeioAMeio()
                ? item.getProduto().getNome() + " / " + item.getSegundoSabor().getNome()
                : item.getProduto().getNome();
        return nomeProduto + " (" + item.getTamanho() + ") x" + item.getQuantidade()
                + " — " + MOEDA.format(item.calcularSubtotal());
    }

    // =====================================================================
    // ETAPA 2 — RESUMO
    // =====================================================================
    private JPanel buildResumoStep() {
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setOpaque(false);

        RoundedPanel card = new RoundedPanel(25, PURPLE_CARD);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(20, 30, 20, 30));
        card.setPreferredSize(new Dimension(520, 560));

        card.add(sectionTitle("RESUMO DO PEDIDO"));
        card.add(Box.createVerticalStrut(10));

        listaResumo = new JList<>(modeloResumo);
        listaResumo.setFont(FonteUtil.carregarFonte(POPPINS, 14f));
        listaResumo.setBackground(WHITE);
        JScrollPane scrollResumo = new JScrollPane(listaResumo);
        scrollResumo.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollResumo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));
        card.add(scrollResumo);
        card.add(Box.createVerticalStrut(12));

        labelClienteResumo = fieldLabel("CLIENTE: -");
        card.add(labelClienteResumo);
        card.add(Box.createVerticalStrut(10));

        card.add(fieldLabel("SAÍDA"));
        card.add(Box.createVerticalStrut(4));
        JPanel saidaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        saidaPanel.setOpaque(false);
        saidaPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnEntrega = new RoundedButton("ENTREGA");
        btnRetirada = new RoundedButton("RETIRADA");
        btnEntrega.addActionListener(e -> selecionarTipoSaida(TipoSaida.ENTREGA));
        btnRetirada.addActionListener(e -> selecionarTipoSaida(TipoSaida.RETIRADA));
        saidaPanel.add(btnEntrega);
        saidaPanel.add(btnRetirada);
        card.add(saidaPanel);
        card.add(Box.createVerticalStrut(10));

        card.add(fieldLabel("FORMA DE PAGAMENTO"));
        card.add(Box.createVerticalStrut(4));
        comboFormaPag = new JComboBox<>(new String[]{"Dinheiro", "Cartão", "Pix"});
        comboFormaPag.setAlignmentX(Component.LEFT_ALIGNMENT);
        comboFormaPag.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        card.add(comboFormaPag);
        card.add(Box.createVerticalStrut(10));

        card.add(fieldLabel("OBSERVAÇÃO GERAL"));
        card.add(Box.createVerticalStrut(2));
        campoObservacaoGeral = new RoundedTextField(20);
        card.add(campoObservacaoGeral);
        card.add(Box.createVerticalStrut(10));

        labelFreteResumo = fieldLabel("FRETE: " + MOEDA.format(0));
        card.add(labelFreteResumo);
        card.add(Box.createVerticalStrut(6));

        labelTotalResumo = new JLabel("TOTAL: " + MOEDA.format(0));
        labelTotalResumo.setFont(FonteUtil.carregarFonte(AUDIOWIDE, 20f));
        labelTotalResumo.setForeground(GREEN_ACCENT);
        labelTotalResumo.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(labelTotalResumo);
        card.add(Box.createVerticalStrut(16));

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        btnPanel.setOpaque(false);
        btnPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        RoundedButton voltarBtn = new RoundedButton("< VOLTAR", ORANGE_BTN, ORANGE_BTN_HOVER);
        voltarBtn.addActionListener(e -> irParaEtapa("montar"));

        RoundedButton finalizarBtn = new RoundedButton("FINALIZAR ✓");
        finalizarBtn.addActionListener(e -> finalizarPedido());

        btnPanel.add(voltarBtn);
        btnPanel.add(finalizarBtn);
        card.add(btnPanel);

        GridBagConstraints gbc = new GridBagConstraints();
        wrapper.add(card, gbc);
        return wrapper;
    }

    private void selecionarTipoSaida(TipoSaida tipo) {
        tipoSaidaSelecionado = tipo;
        btnEntrega.setSelecionado(tipo == TipoSaida.ENTREGA);
        btnRetirada.setSelecionado(tipo == TipoSaida.RETIRADA);
        atualizarTotaisResumo();
    }

    private double calcularFrete() {
        return tipoSaidaSelecionado == TipoSaida.ENTREGA ? 9.00 : 0.00;
    }

    private void atualizarTotaisResumo() {
        double subtotal = 0;
        for (ItemPedido item : carrinho) {
            subtotal += item.calcularSubtotal();
        }
        double frete = calcularFrete();
        labelFreteResumo.setText("FRETE: " + MOEDA.format(frete));
        labelTotalResumo.setText("TOTAL: " + MOEDA.format(subtotal + frete));
    }

    private void irParaResumo() {
        if (carrinho.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Adicione ao menos um item ao pedido.");
            return;
        }
        modeloResumo.clear();
        for (ItemPedido item : carrinho) {
            modeloResumo.addElement(descreverItem(item));
        }
        labelClienteResumo.setText("CLIENTE: " + (clienteSelecionado != null ? clienteSelecionado.getNome() : "-"));
        selecionarTipoSaida(TipoSaida.ENTREGA);
        irParaEtapa("resumo");
    }

    private void finalizarPedido() {
        try {
            Pedido pedido = new Pedido(
                    clienteSelecionado,
                    (String) comboFormaPag.getSelectedItem(),
                    campoObservacaoGeral.getText().trim(),
                    new Date(),
                    tipoSaidaSelecionado
            );
            pedido.setFrete(calcularFrete());
            for (ItemPedido item : carrinho) {
                pedido.adicionarItem(item);
            }
            pedidoDAO.inserir(pedido);
            irParaEtapa("conclusao");
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao finalizar o pedido: " + ex.getMessage());
        }
    }

    // =====================================================================
    // ETAPA 3 — CONCLUSÃO
    // =====================================================================
    private JPanel buildConclusaoStep() {
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setOpaque(false);

        JPanel card = new JPanel();
        card.setOpaque(false);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JLabel check = new JLabel("✔") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(WHITE);
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.setColor(GREEN_ACCENT);
                g2.setStroke(new BasicStroke(6));
                g2.drawOval(4, 4, getWidth() - 8, getHeight() - 8);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                g2.drawString(getText(), x, y);
                g2.dispose();
            }
        };
        check.setFont(FonteUtil.carregarFonte(AUDIOWIDE, 40f));
        check.setForeground(GREEN_ACCENT);
        check.setPreferredSize(new Dimension(120, 120));
        check.setMaximumSize(new Dimension(120, 120));
        check.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(check);
        card.add(Box.createVerticalStrut(16));

        JLabel mensagem = new JLabel("Pedido realizado com sucesso!");
        mensagem.setFont(FonteUtil.carregarFonte(POPPINS, 18f));
        mensagem.setForeground(PURPLE_CARD);
        mensagem.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(mensagem);
        card.add(Box.createVerticalStrut(20));

        JPanel btnLinha = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        btnLinha.setOpaque(false);

        RoundedButton novoPedidoBtn = new RoundedButton("NOVO PEDIDO");
        novoPedidoBtn.addActionListener(e -> reiniciarPedido());

        RoundedButton menuBtn = new RoundedButton("< MENU", ORANGE_BTN, ORANGE_BTN_HOVER);
        menuBtn.addActionListener(e -> {
            reiniciarPedido();
            if (onVoltarAction != null) {
                onVoltarAction.run();
            }
        });

        btnLinha.add(novoPedidoBtn);
        btnLinha.add(menuBtn);
        card.add(btnLinha);

        wrapper.add(card, new GridBagConstraints());
        return wrapper;
    }

    private void reiniciarPedido() {
        carrinho.clear();
        clienteSelecionado = null;
        campoBuscaCliente.setText("");
        labelClienteSelecionado.setText("Nenhum cliente selecionado");
        limparFormularioItem();
        atualizarListaCarrinho();
        carregarTabelaProdutos();
        irParaEtapa("montar");
    }

    // ---------- Auxiliares visuais ----------
    private JLabel sectionTitle(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FonteUtil.carregarFonte(AUDIOWIDE, 20f));
        label.setForeground(GREEN_ACCENT);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JLabel sectionTitleDark(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FonteUtil.carregarFonte(AUDIOWIDE, 20f));
        label.setForeground(PURPLE_CARD);
        label.setBorder(new EmptyBorder(0, 4, 8, 0));
        return label;
    }

    private JLabel fieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FonteUtil.carregarFonte(POPPINS, 16f));
        label.setForeground(WHITE);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    // ---------- Componentes Gráficos Customizados ----------

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

    static class RoundedButton extends JButton {
        private Color normalBg;
        private Color hoverBg;
        private Color currentBg;
        private boolean selecionado = false;

        RoundedButton(String text) {
            this(text, GREEN_BTN, GREEN_BTN_HOVER);
        }

        RoundedButton(String text, Color normalBg, Color hoverBg) {
            super(text);
            this.normalBg = normalBg;
            this.hoverBg = hoverBg;
            this.currentBg = normalBg;

            setFont(FonteUtil.carregarFonte(POPPINS, 12f));
            setForeground(WHITE);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorder(new EmptyBorder(6, 14, 6, 14));
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    if (!selecionado) {
                        currentBg = hoverBg;
                        repaint();
                    }
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    if (!selecionado) {
                        currentBg = normalBg;
                        repaint();
                    }
                }
            });
        }

        void setSelecionado(boolean selecionado) {
            this.selecionado = selecionado;
            this.currentBg = selecionado ? GREEN_ACCENT : normalBg;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(currentBg);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
            g2.dispose();
            super.paintComponent(g);
        }
    }
}