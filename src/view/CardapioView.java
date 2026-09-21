package view;

import dao.ProdutoDAO;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.net.URL;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import model.Produto;

public class CardapioView extends JPanel {

    private static final String AUDIOWIDE = "Audiowide-Regular.ttf";
    private static final String POPPINS   = "Poppins-Regular.ttf";

    private static final Color BG_LIGHT_BLUE   = new Color(0xC7EAF7);
    private static final Color PURPLE_CARD     = new Color(0x5B2A86);
    private static final Color GREEN_ACCENT    = new Color(0x6FCF52);
    private static final Color GREEN_BTN       = new Color(0x3F7D3A);
    private static final Color GREEN_BTN_HOVER = new Color(0x356B31);
    private static final Color RED_BTN         = new Color(0xA02323);
    private static final Color RED_BTN_HOVER   = new Color(0x821C1C);
    private static final Color WHITE           = Color.WHITE;

    private Runnable onVoltarAction;

    // Componentes de interface
    private JTable tabelaProdutos;
    private DefaultTableModel tableModel;
    private RoundedTextField nomeField;
    private RoundedTextField categoriaField;
    private RoundedTextField descricaoField;
    private RoundedTextField precoField;
    private JCheckBox disponivelCheckBox;
    private RoundedTextField buscaField;

    private Integer idProdutoSelecionado = null; // Guarda o ID do produto selecionado para edição/exclusão

    public CardapioView() {
        this(null);
    }

    public CardapioView(Runnable onVoltarAction) {
        this.onVoltarAction = onVoltarAction;

        setLayout(new BorderLayout());
        setBackground(BG_LIGHT_BLUE);
        setBorder(new EmptyBorder(20, 30, 20, 30));

        add(buildHeader(), BorderLayout.NORTH);
        add(buildBody(), BorderLayout.CENTER);

        carregarDadosTabela(null);
    }

    // ---------- Cabeçalho ----------
    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(0, 0, 15, 0));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        left.setOpaque(false);

        JLabel cardapioLabel = new JLabel("CARDÁPIO");
        cardapioLabel.setFont(FonteUtil.carregarFonte(AUDIOWIDE, 64f));
        cardapioLabel.setForeground(PURPLE_CARD);
        cardapioLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, PURPLE_CARD));

        JButton backBtn = makeCircleButton("<");
        backBtn.addActionListener(e -> {
            if (onVoltarAction != null) {
                onVoltarAction.run();
            }
        });

        left.add(cardapioLabel);
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

    // ---------- Corpo Principal ----------
    private JPanel buildBody() {
        JPanel body = new JPanel(new GridBagLayout());
        body.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 10, 5, 10);

        // Lado Esquerdo: Tabela (ocupa todo o espaço dinâmico)
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        body.add(buildTablePanel(), gbc);

        // Lado Direito: Formulário e Busca
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

        // Formulário
        gbcRight.gridy = 0;
        gbcRight.weighty = 0.0;
        rightPanel.add(buildFormCard(), gbcRight);

        // Busca
        gbcRight.gridy = 1;
        gbcRight.weighty = 1.0;
        gbcRight.insets = new Insets(0, 0, 0, 0);
        rightPanel.add(buildSearchCard(), gbcRight);

        body.add(rightPanel, gbc);

        return body;
    }

    // ---------- Tabela ----------
    private JPanel buildTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        String[] colunas = {"ID", "Nome", "Categoria", "Descrição", "Preço", "Disponível"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelaProdutos = new JTable(tableModel);
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

        tabelaProdutos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tabelaProdutos.getSelectedRow();
                if (row != -1) {
                    idProdutoSelecionado = (Integer) tableModel.getValueAt(row, 0);
                    nomeField.setText((String) tableModel.getValueAt(row, 1));
                    categoriaField.setText((String) tableModel.getValueAt(row, 2));
                    descricaoField.setText((String) tableModel.getValueAt(row, 3));
                    
                    Object precoObj = tableModel.getValueAt(row, 4);
                    if (precoObj instanceof Double) {
                        precoField.setText(String.format(Locale.US, "%.2f", (Double) precoObj));
                    } else {
                        precoField.setText(precoObj.toString().replace("R$", "").replace(",", ".").trim());
                    }

                    String disp = (String) tableModel.getValueAt(row, 5);
                    disponivelCheckBox.setSelected("Sim".equalsIgnoreCase(disp));
                }
            }
        });

        JScrollPane scroll = new JScrollPane(tabelaProdutos);
        scroll.getViewport().setBackground(WHITE);
        scroll.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    // ---------- Card de Cadastro / Edição ----------
    private RoundedPanel buildFormCard() {
        RoundedPanel card = new RoundedPanel(25, PURPLE_CARD);
        
        Dimension formSize = new Dimension(360, 420);
        card.setPreferredSize(formSize);
        card.setMaximumSize(formSize);

        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(15, 20, 15, 20));

        card.add(sectionTitle("CADASTRAR / EDITAR"));
        card.add(Box.createVerticalStrut(10));

        card.add(fieldLabel("NOME"));
        card.add(Box.createVerticalStrut(2));
        nomeField = new RoundedTextField(20);
        card.add(nomeField);
        card.add(Box.createVerticalStrut(6));

        card.add(fieldLabel("CATEGORIA"));
        card.add(Box.createVerticalStrut(2));
        categoriaField = new RoundedTextField(20);
        card.add(categoriaField);
        card.add(Box.createVerticalStrut(6));

        card.add(fieldLabel("DESCRIÇÃO"));
        card.add(Box.createVerticalStrut(2));
        descricaoField = new RoundedTextField(20);
        card.add(descricaoField);
        card.add(Box.createVerticalStrut(6));

        card.add(fieldLabel("PREÇO (R$)"));
        card.add(Box.createVerticalStrut(2));
        precoField = new RoundedTextField(20);
        card.add(precoField);
        card.add(Box.createVerticalStrut(6));

        disponivelCheckBox = new JCheckBox("Disponível no Cardápio");
        disponivelCheckBox.setOpaque(false);
        disponivelCheckBox.setForeground(WHITE);
        disponivelCheckBox.setFont(FonteUtil.carregarFonte(POPPINS, 13f));
        disponivelCheckBox.setSelected(true);
        disponivelCheckBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(disponivelCheckBox);

        card.add(Box.createVerticalStrut(10));

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        btnPanel.setOpaque(false);
        btnPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        RoundedButton limparBtn = new RoundedButton("LIMPAR");
        limparBtn.addActionListener(e -> limparFormulario());

        RoundedButton salvarBtn = new RoundedButton("SALVAR >");
        salvarBtn.addActionListener(e -> salvarProduto());

        RoundedButton deletarBtn = new RoundedButton("EXCLUIR", RED_BTN, RED_BTN_HOVER);
        deletarBtn.addActionListener(e -> deletarProdutoComAdmin());

        btnPanel.add(limparBtn);
        btnPanel.add(deletarBtn);
        btnPanel.add(salvarBtn);

        card.add(btnPanel);
        return card;
    }

    // ---------- Card de Busca ----------
    private RoundedPanel buildSearchCard() {
        RoundedPanel card = new RoundedPanel(25, PURPLE_CARD);
        
        Dimension searchSize = new Dimension(360, 110);
        card.setPreferredSize(searchSize);
        card.setMaximumSize(searchSize);

        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(12, 20, 12, 20));

        JLabel title = sectionTitle("PESQUISAR PRODUTOS");
        card.add(title);
        card.add(Box.createVerticalStrut(8));

        buscaField = new RoundedTextField(20);
        buscaField.setToolTipText("Digite o nome ou categoria");

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
        ProdutoDAO dao = new ProdutoDAO();
        List<Produto> lista = dao.listarTodos();

        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

        for (Produto p : lista) {
            if (filtro != null && !filtro.isEmpty()) {
                String f = filtro.toLowerCase();
                boolean bateuNome = p.getNome() != null && p.getNome().toLowerCase().contains(f);
                boolean bateuCat = p.getCategoria() != null && p.getCategoria().toLowerCase().contains(f);

                if (!bateuNome && !bateuCat) {
                    continue;
                }
            }

            tableModel.addRow(new Object[]{
                p.getId(),
                p.getNome(),
                p.getCategoria(),
                p.getDescricao(),
                currencyFormat.format(p.getPreco()),
                p.isDisponivel() ? "Sim" : "Não"
            });
        }
    }

    private void salvarProduto() {
        String nome = nomeField.getText().trim();
        String categoria = categoriaField.getText().trim();
        String descricao = descricaoField.getText().trim();
        String precoTxt = precoField.getText().trim().replace(",", ".");

        if (nome.isEmpty() || categoria.isEmpty() || precoTxt.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha Nome, Categoria e Preço para continuar.");
            return;
        }

        double preco;
        try {
            preco = Double.parseDouble(precoTxt);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Informe um preço válido (ex: 29.90 ou 29,90).");
            return;
        }

        ProdutoDAO dao = new ProdutoDAO();
        Produto produto = new Produto(nome, categoria, descricao, preco);
        produto.setDisponivel(disponivelCheckBox.isSelected());

        if (idProdutoSelecionado == null) {
            dao.inserir(produto);
            JOptionPane.showMessageDialog(this, "Produto cadastrado com sucesso!");
        } else {
            produto.setId(idProdutoSelecionado);
            dao.alterar(produto);
            JOptionPane.showMessageDialog(this, "Produto atualizado com sucesso!");
        }

        limparFormulario();
        carregarDadosTabela(null);
    }

    private void deletarProdutoComAdmin() {
        if (idProdutoSelecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um produto na tabela para excluir.");
            return;
        }

        JPasswordField pf = new JPasswordField();
        int ok = JOptionPane.showConfirmDialog(
            this,
            pf,
            "Digite a SENHA ADMIN para confirmar a exclusão:",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
        );

        if (ok == JOptionPane.OK_OPTION) {
            String senhaAdmin = new String(pf.getPassword());
            if ("LaSottamPizzaria".equals(senhaAdmin)) {
                ProdutoDAO dao = new ProdutoDAO();
                dao.excluir(idProdutoSelecionado);
                JOptionPane.showMessageDialog(this, "Produto excluído com sucesso!");
                limparFormulario();
                carregarDadosTabela(null);
            } else {
                JOptionPane.showMessageDialog(this, "Senha incorreta. Ação cancelada.");
            }
        }
    }

    private void limparFormulario() {
        idProdutoSelecionado = null;
        nomeField.setText("");
        categoriaField.setText("");
        descricaoField.setText("");
        precoField.setText("");
        disponivelCheckBox.setSelected(true);
        tabelaProdutos.clearSelection();
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
                    currentBg = hoverBg;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    currentBg = normalBg;
                    repaint();
                }
            });
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