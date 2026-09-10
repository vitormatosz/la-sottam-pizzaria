package view;

import dao.ClienteDAO;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.net.URL;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import model.Cliente;

public class ClientesView extends JPanel {

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
    private JTable tabelaClientes;
    private DefaultTableModel tableModel;
    private RoundedTextField nomeField;
    private RoundedTextField telefoneField;
    private RoundedTextField enderecoField;
    private RoundedTextField buscaField;

    private Integer idClienteSelecionado = null; // Guarda o ID do cliente selecionado para edição/exclusão

    public ClientesView() {
        this(null);
    }

    public ClientesView(Runnable onVoltarAction) {
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

        JLabel clientesLabel = new JLabel("CLIENTES");
        clientesLabel.setFont(FonteUtil.carregarFonte(AUDIOWIDE, 64f));
        clientesLabel.setForeground(PURPLE_CARD);
        clientesLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, PURPLE_CARD));

        JButton backBtn = makeCircleButton("<");
        backBtn.addActionListener(e -> {
            if (onVoltarAction != null) {
                onVoltarAction.run();
            }
        });

        left.add(clientesLabel);
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

        // Lado Esquerdo: Tabela
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.55;
        gbc.weighty = 1.0;
        body.add(buildTablePanel(), gbc);

        // Lado Direito: Formulário e Busca
        gbc.gridx = 1;
        gbc.weightx = 0.45;

        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setOpaque(false);

        GridBagConstraints gbcRight = new GridBagConstraints();
        gbcRight.gridx = 0;
        gbcRight.fill = GridBagConstraints.BOTH;
        gbcRight.insets = new Insets(0, 0, 15, 0);

        // Formulário
        gbcRight.gridy = 0;
        gbcRight.weighty = 0.65;
        rightPanel.add(buildFormCard(), gbcRight);

        // Busca
        gbcRight.gridy = 1;
        gbcRight.weighty = 0.35;
        gbcRight.insets = new Insets(0, 0, 0, 0);
        rightPanel.add(buildSearchCard(), gbcRight);

        body.add(rightPanel, gbc);

        return body;
    }

    // ---------- Tabela ----------
    private JPanel buildTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        String[] colunas = {"ID", "Nome", "Telefone", "Endereço"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelaClientes = new JTable(tableModel);
        tabelaClientes.setFont(FonteUtil.carregarFonte(POPPINS, 14f));
        tabelaClientes.setRowHeight(32);
        tabelaClientes.getTableHeader().setFont(FonteUtil.carregarFonte(POPPINS, 15f));
        tabelaClientes.getTableHeader().setBackground(WHITE);
        tabelaClientes.getTableHeader().setForeground(PURPLE_CARD);
        tabelaClientes.setShowGrid(true);
        tabelaClientes.setGridColor(Color.BLACK);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < tabelaClientes.getColumnCount(); i++) {
            tabelaClientes.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        tabelaClientes.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tabelaClientes.getSelectedRow();
                if (row != -1) {
                    idClienteSelecionado = (Integer) tableModel.getValueAt(row, 0);
                    nomeField.setText((String) tableModel.getValueAt(row, 1));
                    telefoneField.setText((String) tableModel.getValueAt(row, 2));
                    enderecoField.setText((String) tableModel.getValueAt(row, 3));
                }
            }
        });

        JScrollPane scroll = new JScrollPane(tabelaClientes);
        scroll.getViewport().setBackground(WHITE);
        scroll.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    // ---------- Card de Cadastro / Edição ----------
    private RoundedPanel buildFormCard() {
        RoundedPanel card = new RoundedPanel(25, PURPLE_CARD);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(20, 25, 20, 25));

        card.add(sectionTitle("CADASTRAR / EDITAR"));
        card.add(Box.createVerticalStrut(12));

        card.add(fieldLabel("NOME"));
        card.add(Box.createVerticalStrut(4));
        nomeField = new RoundedTextField(20);
        card.add(nomeField);
        card.add(Box.createVerticalStrut(10));

        card.add(fieldLabel("TELEFONE"));
        card.add(Box.createVerticalStrut(4));
        telefoneField = new RoundedTextField(20);
        card.add(telefoneField);
        card.add(Box.createVerticalStrut(10));

        card.add(fieldLabel("ENDEREÇO"));
        card.add(Box.createVerticalStrut(4));
        enderecoField = new RoundedTextField(20);
        card.add(enderecoField);
        card.add(Box.createVerticalStrut(15));

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        btnPanel.setOpaque(false);
        btnPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        RoundedButton limparBtn = new RoundedButton("LIMPAR");
        limparBtn.addActionListener(e -> limparFormulario());

        RoundedButton salvarBtn = new RoundedButton("SALVAR >");
        salvarBtn.addActionListener(e -> salvarCliente());

        RoundedButton deletarBtn = new RoundedButton("EXCLUIR", RED_BTN, RED_BTN_HOVER);
        deletarBtn.addActionListener(e -> deletarClienteComAdmin());

        btnPanel.add(limparBtn);
        btnPanel.add(deletarBtn);
        btnPanel.add(salvarBtn);

        card.add(btnPanel);
        return card;
    }

    // ---------- Card de Busca ----------
    private RoundedPanel buildSearchCard() {
        RoundedPanel card = new RoundedPanel(25, PURPLE_CARD);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(15, 25, 15, 25));

        JLabel title = sectionTitle("PESQUISAR CLIENTES");
        card.add(title);
        card.add(Box.createVerticalStrut(10));

        buscaField = new RoundedTextField(20);
        buscaField.setToolTipText("Digite o nome ou telefone");

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
        ClienteDAO dao = new ClienteDAO();
        List<Cliente> lista = dao.listarTodos();

        for (Cliente c : lista) {
            // Aplicar filtro local caso o usuário esteja digitando na barra de pesquisa
            if (filtro != null && !filtro.isEmpty()) {
                String f = filtro.toLowerCase();
                boolean bateuNome = c.getNome() != null && c.getNome().toLowerCase().contains(f);
                boolean bateuTel = c.getNumeroTel() != null && c.getNumeroTel().contains(f);

                if (!bateuNome && !bateuTel) {
                    continue;
                }
            }

            tableModel.addRow(new Object[]{
                c.getId(),
                c.getNome(),
                c.getNumeroTel(), // Método getNumeroTel do seu model
                c.getEndereco()
            });
        }
    }

    private void salvarCliente() {
        String nome = nomeField.getText().trim();
        String telefone = telefoneField.getText().trim();
        String endereco = enderecoField.getText().trim();

        if (nome.isEmpty() || telefone.isEmpty() || endereco.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos para continuar.");
            return;
        }

        ClienteDAO dao = new ClienteDAO();
        Cliente cliente = new Cliente(nome, telefone, endereco);

        if (idClienteSelecionado == null) {
            // Inserir Novo
            dao.inserir(cliente);
            JOptionPane.showMessageDialog(this, "Cliente cadastrado com sucesso!");
        } else {
            // Alterar Existente
            cliente.setId(idClienteSelecionado);
            dao.alterar(cliente); // Método alterar() do seu ClienteDAO
            JOptionPane.showMessageDialog(this, "Cliente atualizado com sucesso!");
        }

        limparFormulario();
        carregarDadosTabela(null);
    }

    private void deletarClienteComAdmin() {
        if (idClienteSelecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um cliente na tabela para excluir.");
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
            if ("1234".equals(senhaAdmin)) {
                ClienteDAO dao = new ClienteDAO();
                dao.excluir(idClienteSelecionado);
                JOptionPane.showMessageDialog(this, "Cliente excluído com sucesso!");
                limparFormulario();
                carregarDadosTabela(null);
            } else {
                JOptionPane.showMessageDialog(this, "Senha incorreta. Ação cancelada.");
            }
        }
    }

    private void limparFormulario() {
        idClienteSelecionado = null;
        nomeField.setText("");
        telefoneField.setText("");
        enderecoField.setText("");
        tabelaClientes.clearSelection();
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
        label.setFont(FonteUtil.carregarFonte(POPPINS, 20f));
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