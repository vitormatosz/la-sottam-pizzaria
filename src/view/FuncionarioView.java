package view;

import dao.FuncionarioDAO;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import model.Funcionario;

public class FuncionarioView extends JPanel {
    private static final Color BG_LIGHT_BLUE = new Color(0xC7EAF7);
    private static final Color PURPLE_CARD = new Color(0x5B2A86);
    private static final Color GREEN_ACCENT = new Color(0x6FCF52);
    private static final Color GREEN_BTN = new Color(0x3F7D3A);
    private static final Color GREEN_BTN_HOVER = new Color(0x356B31);
    private static final Color RED_BTN = new Color(0xA02323);
    private static final Color RED_BTN_HOVER = new Color(0x821C1C);
    private static final Color WHITE = Color.WHITE;

    private final Runnable onVoltarAction;
    private JTable tabelaFuncionarios;
    private DefaultTableModel tableModel;
    private ClientesView.RoundedTextField usuarioField;
    private ClientesView.RoundedTextField senhaField;
    private ClientesView.RoundedTextField buscaField;
    private Integer idFuncionarioSelecionado;

    public FuncionarioView(Runnable onVoltarAction) {
        this.onVoltarAction = onVoltarAction;
        setLayout(new BorderLayout());
        setBackground(BG_LIGHT_BLUE);
        setBorder(new EmptyBorder(20, 30, 20, 30));
        add(buildHeader(), BorderLayout.NORTH);
        add(buildBody(), BorderLayout.CENTER);
        carregarDadosTabela(null);
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(0, 0, 15, 0));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        left.setOpaque(false);
        JLabel titulo = new JLabel("FUNCIONÁRIOS");
        titulo.setFont(FonteUtil.carregarFonte("Audiowide-Regular.ttf", 52f));
        titulo.setForeground(PURPLE_CARD);
        titulo.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, PURPLE_CARD));
        JButton voltar = makeCircleButton("<");
        voltar.addActionListener(e -> {
            if (onVoltarAction != null) onVoltarAction.run();
        });
        left.add(titulo);
        left.add(voltar);

        JPanel right = new JPanel();
        right.setOpaque(false);
        URLHelper.adicionarLogo(this, right);
        header.add(left, BorderLayout.WEST);
        header.add(right, BorderLayout.EAST);
        return header;
    }

    private JButton makeCircleButton(String text) {
        JButton button = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(GREEN_ACCENT);
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.setColor(WHITE);
                FontMetrics metrics = g2.getFontMetrics(getFont());
                g2.drawString(getText(), (getWidth() - metrics.stringWidth(getText())) / 2,
                        (getHeight() - metrics.getHeight()) / 2 + metrics.getAscent());
                g2.dispose();
            }
        };
        button.setPreferredSize(new Dimension(36, 36));
        button.setFont(FonteUtil.carregarFonte("Audiowide-Regular.ttf", 20f));
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private JPanel buildBody() {
        JPanel body = new JPanel(new GridBagLayout());
        body.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        body.add(buildTablePanel(), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0;
        JPanel right = new JPanel(new GridBagLayout());
        right.setOpaque(false);
        GridBagConstraints rightGbc = new GridBagConstraints();
        rightGbc.fill = GridBagConstraints.HORIZONTAL;
        rightGbc.anchor = GridBagConstraints.NORTH;
        rightGbc.gridx = 0;
        rightGbc.gridy = 0;
        right.add(buildFormCard(), rightGbc);
        rightGbc.gridy = 1;
        rightGbc.weighty = 1;
        rightGbc.insets = new Insets(15, 0, 0, 0);
        right.add(buildSearchCard(), rightGbc);
        body.add(right, gbc);
        return body;
    }

    private JPanel buildTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        tableModel = new DefaultTableModel(new String[]{"ID", "Usuário", "Senha"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tabelaFuncionarios = new JTable(tableModel);
        tabelaFuncionarios.setFont(FonteUtil.carregarFonte("Poppins-Regular.ttf", 14f));
        tabelaFuncionarios.setRowHeight(32);
        tabelaFuncionarios.getTableHeader().setFont(FonteUtil.carregarFonte("Poppins-Regular.ttf", 15f));
        tabelaFuncionarios.getTableHeader().setForeground(PURPLE_CARD);
        tabelaFuncionarios.setShowGrid(true);
        tabelaFuncionarios.setGridColor(Color.BLACK);
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < tabelaFuncionarios.getColumnCount(); i++) {
            tabelaFuncionarios.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }
        tabelaFuncionarios.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                int row = tabelaFuncionarios.getSelectedRow();
                if (row >= 0) {
                    idFuncionarioSelecionado = (Integer) tableModel.getValueAt(row, 0);
                    usuarioField.setText((String) tableModel.getValueAt(row, 1));
                    senhaField.setText((String) tableModel.getValueAt(row, 2));
                }
            }
        });
        JScrollPane scroll = new JScrollPane(tabelaFuncionarios);
        scroll.getViewport().setBackground(WHITE);
        scroll.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private ClientesView.RoundedPanel buildFormCard() {
        ClientesView.RoundedPanel card = new ClientesView.RoundedPanel(25, PURPLE_CARD);
        card.setPreferredSize(new Dimension(360, 270));
        card.setMaximumSize(new Dimension(360, 270));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(15, 20, 15, 20));
        card.add(sectionTitle("CADASTRAR / EDITAR"));
        card.add(Box.createVerticalStrut(10));
        card.add(fieldLabel("NOME DE USUÁRIO"));
        card.add(Box.createVerticalStrut(2));
        usuarioField = new ClientesView.RoundedTextField(20);
        card.add(usuarioField);
        card.add(Box.createVerticalStrut(8));
        card.add(fieldLabel("SENHA"));
        card.add(Box.createVerticalStrut(2));
        senhaField = new ClientesView.RoundedTextField(20);
        card.add(senhaField);
        card.add(Box.createVerticalStrut(12));
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        buttons.setOpaque(false);
        buttons.setAlignmentX(Component.LEFT_ALIGNMENT);
        ClientesView.RoundedButton limpar = new ClientesView.RoundedButton("LIMPAR");
        limpar.addActionListener(e -> limparFormulario());
        ClientesView.RoundedButton excluir = new ClientesView.RoundedButton("EXCLUIR", RED_BTN, RED_BTN_HOVER);
        excluir.addActionListener(e -> excluirFuncionario());
        ClientesView.RoundedButton salvar = new ClientesView.RoundedButton("SALVAR >");
        salvar.addActionListener(e -> salvarFuncionario());
        buttons.add(limpar);
        buttons.add(excluir);
        buttons.add(salvar);
        card.add(buttons);
        return card;
    }

    private ClientesView.RoundedPanel buildSearchCard() {
        ClientesView.RoundedPanel card = new ClientesView.RoundedPanel(25, PURPLE_CARD);
        card.setPreferredSize(new Dimension(360, 110));
        card.setMaximumSize(new Dimension(360, 110));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(12, 20, 12, 20));
        card.add(sectionTitle("PESQUISAR FUNCIONÁRIOS"));
        card.add(Box.createVerticalStrut(8));
        buscaField = new ClientesView.RoundedTextField(20);
        buscaField.addKeyListener(new KeyAdapter() {
            @Override public void keyReleased(KeyEvent e) { carregarDadosTabela(buscaField.getText().trim()); }
        });
        card.add(buscaField);
        return card;
    }

    private void carregarDadosTabela(String filtro) {
        tableModel.setRowCount(0);
        List<Funcionario> funcionarios = new FuncionarioDAO().listarTodos();
        for (Funcionario funcionario : funcionarios) {
            if (filtro != null && !filtro.isEmpty()
                    && !funcionario.getNomeUsuario().toLowerCase().contains(filtro.toLowerCase())) continue;
            tableModel.addRow(new Object[]{funcionario.getId(), funcionario.getNomeUsuario(), funcionario.getSenha()});
        }
    }

    private void salvarFuncionario() {
        String usuario = usuarioField.getText().trim();
        String senha = senhaField.getText().trim();
        if (usuario.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos para continuar.");
            return;
        }
        Funcionario funcionario = new Funcionario(usuario, senha);
        FuncionarioDAO dao = new FuncionarioDAO();
        if (idFuncionarioSelecionado == null) {
            dao.inserir(funcionario);
            JOptionPane.showMessageDialog(this, "Funcionário cadastrado com sucesso!");
        } else {
            funcionario.setId(idFuncionarioSelecionado);
            dao.alterar(funcionario);
            JOptionPane.showMessageDialog(this, "Funcionário atualizado com sucesso!");
        }
        limparFormulario();
        carregarDadosTabela(null);
    }

    private void excluirFuncionario() {
        if (idFuncionarioSelecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um funcionário na tabela para excluir.");
            return;
        }
        JPasswordField password = new JPasswordField();
        int result = JOptionPane.showConfirmDialog(this, password,
                "Digite a SENHA ADMIN para confirmar a exclusão:",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            if ("LaSottamPizzaria".equals(new String(password.getPassword()))) {
                new FuncionarioDAO().excluir(idFuncionarioSelecionado);
                JOptionPane.showMessageDialog(this, "Funcionário excluído com sucesso!");
                limparFormulario();
                carregarDadosTabela(null);
            } else JOptionPane.showMessageDialog(this, "Senha incorreta. Ação cancelada.");
        }
    }

    private void limparFormulario() {
        idFuncionarioSelecionado = null;
        usuarioField.setText("");
        senhaField.setText("");
        tabelaFuncionarios.clearSelection();
    }

    private JLabel sectionTitle(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FonteUtil.carregarFonte("Audiowide-Regular.ttf", 20f));
        label.setForeground(GREEN_ACCENT);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JLabel fieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FonteUtil.carregarFonte("Poppins-Regular.ttf", 20f));
        label.setForeground(WHITE);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private static class URLHelper {
        static void adicionarLogo(Component owner, JPanel panel) {
            java.net.URL url = owner.getClass().getResource("/assets/logo.png");
            if (url != null) {
                Image image = new ImageIcon(url).getImage().getScaledInstance(350, 70, Image.SCALE_SMOOTH);
                panel.add(new JLabel(new ImageIcon(image)));
            }
        }
    }
}