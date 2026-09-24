package view;

import dao.IngredienteDAO;
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
import model.Ingrediente;

public class EstoqueView extends JPanel {
        private static final String AUDIOWIDE = "Audiowide-Regular.ttf";
        private static final String POPPINS = "Poppins-Regular.ttf";
        private static final Color BG_LIGHT_BLUE = new Color(0xC7EAF7);
        private static final Color PURPLE_CARD = new Color(0x5B2A86);
        private static final Color GREEN_ACCENT = new Color(0x6FCF52);
        private static final Color GREEN_BTN = new Color(0x3F7D3A);
        private static final Color GREEN_BTN_HOVER = new Color(0x356B31);
        private static final Color RED_BTN = new Color(0xA02323);
        private static final Color RED_BTN_HOVER = new Color(0x821C1C);
        private static final Color WHITE = Color.WHITE;
        private Runnable onVoltarAction;
        
        // Componentes da interface
        private JTable tabelaEstoque;
        private DefaultTableModel tableModel;
        private RoundedTextField nomeField;
        private static final String[] CATEGORIAS = { "Laticínios", "Carnes e frios", "Vegetais", "Massas e farinhas", "Molhos", "Bebidas", "Outros" };
        private static final String[] UNIDADES = { "g", "kg", "ml", "L", "unidade" };
        private JComboBox<String> categoriaCombo;
        private JComboBox<String> unidadeCombo;
        private RoundedTextField quantidadeField;
        private RoundedTextField estoqueMinimoField;
        private RoundedTextField buscaField;
        // Guarda o ID do ingrediente selecionado
        private Integer idEstoqueSelecionado = null;

        public EstoqueView() {
                this(null);
        }

        public EstoqueView(Runnable onVoltarAction) {
                this.onVoltarAction = onVoltarAction;
                setLayout(new BorderLayout());
                setBackground(BG_LIGHT_BLUE);
                setBorder(new EmptyBorder(20, 30, 20, 30));
                add(buildHeader(), BorderLayout.NORTH);
                add(buildBody(), BorderLayout.CENTER);
                carregarDadosTabela(null);
        }

        // CABEÇALHO
        private JPanel buildHeader() {
                JPanel header = new JPanel(new BorderLayout());
                header.setOpaque(false);
                header.setBorder(new EmptyBorder(0, 0, 15, 0));
                JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
                left.setOpaque(false);
                JLabel estoqueLabel = new JLabel("ESTOQUE");
                estoqueLabel.setFont(FonteUtil.carregarFonte(AUDIOWIDE, 64f));
                estoqueLabel.setForeground(PURPLE_CARD);
                estoqueLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, PURPLE_CARD));
                JButton backBtn = makeCircleButton("<");
                backBtn.addActionListener(e -> {
                        if (onVoltarAction != null) {
                                onVoltarAction.run();
                        }
                });
                left.add(estoqueLabel);
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
                                g2.setFont(getFont());
                                FontMetrics fm = g2.getFontMetrics();
                                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                                int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                                g2.setColor(WHITE);
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

        // CORPO
        private JPanel buildBody() {
                JPanel body = new JPanel(new GridBagLayout());
                body.setOpaque(false);
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.fill = GridBagConstraints.BOTH;
                gbc.insets = new Insets(5, 10, 5, 10);
                // TABELA
                gbc.gridx = 0;
                gbc.gridy = 0;
                gbc.weightx = 1.0;
                gbc.weighty = 1.0;
                body.add(buildTablePanel(), gbc);
                // PAINEL DIREITO
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
                // FORMULÁRIO
                gbcRight.gridy = 0;
                gbcRight.weighty = 0.0;
                rightPanel.add(buildFormCard(), gbcRight);
                // BUSCA
                gbcRight.gridy = 1;
                gbcRight.weighty = 1.0;
                gbcRight.insets = new Insets(0, 0, 0, 0);
                rightPanel.add(buildSearchCard(), gbcRight);
                body.add(rightPanel, gbc);
                return body;
        }


        // TABELA
        private JPanel buildTablePanel() {
                JPanel panel = new JPanel(new BorderLayout());
                panel.setOpaque(false);
                String[] colunas = { "ID", "Nome", "Categoria", "Unidade", "Quantidade", "Estoque Mínimo", "Status" };
                tableModel = new DefaultTableModel(colunas, 0) {
                        @Override
                        public boolean isCellEditable(int row, int column) {
                                return false;
                        }
                };
                tabelaEstoque = new JTable(tableModel);
                tabelaEstoque.setFont(FonteUtil.carregarFonte(POPPINS, 13f));
                tabelaEstoque.setRowHeight(32);
                tabelaEstoque.getTableHeader().setFont(FonteUtil.carregarFonte(POPPINS, 14f));
                tabelaEstoque.getTableHeader().setBackground(WHITE);
                tabelaEstoque.getTableHeader().setForeground(PURPLE_CARD);
                tabelaEstoque.setShowGrid(true);
                tabelaEstoque.setGridColor(Color.BLACK);

                // Centralizar todas as colunas
                DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
                centerRenderer.setHorizontalAlignment(JLabel.CENTER);
                for (int i = 0; i < tabelaEstoque.getColumnCount(); i++) {
                        tabelaEstoque.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
                }

                // Clique na tabela
                tabelaEstoque.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                                int row = tabelaEstoque.getSelectedRow();
                                if (row != -1) {
                                        idEstoqueSelecionado = (Integer) tableModel.getValueAt(row, 0);
                                        nomeField.setText(String.valueOf(tableModel.getValueAt(row, 1)));
                                        selecionarNoCombo(categoriaCombo, String.valueOf(tableModel.getValueAt(row, 2)));
                                        selecionarNoCombo(unidadeCombo, String.valueOf(tableModel.getValueAt(row, 3)));
                                        quantidadeField.setText(String.valueOf(tableModel.getValueAt(row, 4)));
                                        estoqueMinimoField.setText(String.valueOf(tableModel.getValueAt(row, 5)));
                                }
                        }
                });
                JScrollPane scroll = new JScrollPane(tabelaEstoque);
                scroll.getViewport().setBackground(WHITE);
                scroll.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
                panel.add(scroll, BorderLayout.CENTER);
                return panel;
        }


        // FORMULÁRIO
        private RoundedPanel buildFormCard() {
                RoundedPanel card = new RoundedPanel(25, PURPLE_CARD);
                Dimension formSize = new Dimension(360, 500);
                card.setPreferredSize(formSize);
                card.setMaximumSize(formSize);
                card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
                card.setBorder(new EmptyBorder(15, 20, 15, 20));
                card.add(sectionTitle("CADASTRAR / EDITAR"));
                card.add(Box.createVerticalStrut(10));
                // NOME
                card.add(fieldLabel("NOME"));
                card.add(Box.createVerticalStrut(2));
                nomeField = new RoundedTextField(20);
                card.add(nomeField);
                card.add(Box.createVerticalStrut(8));
                // CATEGORIA
                card.add(fieldLabel("CATEGORIA"));
                card.add(Box.createVerticalStrut(2));
                categoriaCombo = criarCombo(CATEGORIAS);
                card.add(categoriaCombo);
                card.add(Box.createVerticalStrut(8));
                // UNIDADE
                card.add(fieldLabel("UNIDADE"));
                card.add(Box.createVerticalStrut(2));
                unidadeCombo = criarCombo(UNIDADES);
                unidadeCombo.setToolTipText("Unidade usada no estoque e nas receitas");
                card.add(unidadeCombo);
                card.add(Box.createVerticalStrut(8));
                // QUANTIDADE
                card.add(fieldLabel("QUANTIDADE"));
                card.add(Box.createVerticalStrut(2));
                quantidadeField = new RoundedTextField(20);
                quantidadeField.setToolTipText("Digite um número. Exemplo: 10.5");
                card.add(quantidadeField);
                card.add(Box.createVerticalStrut(8));
                // ESTOQUE MÍNIMO
                card.add(fieldLabel("ESTOQUE MÍNIMO"));
                card.add(Box.createVerticalStrut(2));
                estoqueMinimoField = new RoundedTextField(20);
                estoqueMinimoField.setToolTipText("Quantidade mínima antes de precisar repor");
                card.add(estoqueMinimoField);
                card.add(Box.createVerticalStrut(12));
                // BOTÕES
                JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
                btnPanel.setOpaque(false);
                btnPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
                RoundedButton limparBtn = new RoundedButton("LIMPAR");
                limparBtn.addActionListener(e -> limparFormulario());
                RoundedButton deletarBtn = new RoundedButton("EXCLUIR", RED_BTN, RED_BTN_HOVER);
                deletarBtn.addActionListener(e -> deletarIngredienteComAdmin());
                RoundedButton salvarBtn = new RoundedButton("SALVAR >");
                salvarBtn.addActionListener(e -> salvarIngrediente());
                btnPanel.add(limparBtn);
                btnPanel.add(deletarBtn);
                btnPanel.add(salvarBtn);
                card.add(btnPanel);
                return card;
        }


        // BUSCA
        private RoundedPanel buildSearchCard() {
                RoundedPanel card = new RoundedPanel(25, PURPLE_CARD);
                Dimension searchSize = new Dimension(360, 120);
                card.setPreferredSize(searchSize);
                card.setMaximumSize(searchSize);
                card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
                card.setBorder(new EmptyBorder(12, 20, 12, 20));
                JLabel title = sectionTitle("PESQUISAR ESTOQUE");
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

        // =========================================================

        // BANCO DE DADOS
        private void carregarDadosTabela(String filtro) {
                tableModel.setRowCount(0);
                IngredienteDAO dao = new IngredienteDAO();
                List<Ingrediente> lista = dao.listarTodos();
                for (Ingrediente ing : lista) {
                        if (filtro != null && !filtro.isEmpty()) {
                                String f = filtro.toLowerCase();
                                boolean bateuNome = ing.getNome() != null && ing.getNome().toLowerCase().contains(f);
                                boolean bateuCategoria = ing.getCategoria() != null
                                                && ing.getCategoria().toLowerCase().contains(f);
                                if (!bateuNome && !bateuCategoria) {
                                        continue;
                                }
                        }
                        String status;
                        if (ing.precisaReposicao()) {
                                status = "REPOR";
                        } else {
                                status = "OK";
                        }
                        tableModel.addRow(new Object[] {
                                        ing.getId(), ing.getNome(), ing.getCategoria(), ing.getUnidade(),
                                        ing.getQuantidade(), ing.getEstoqueMinimo(), status });
                }
        }


        // SALVAR / EDITAR
        private void salvarIngrediente() {
                String nome = nomeField.getText().trim();
                String categoria = (String) categoriaCombo.getSelectedItem();
                String unidade = (String) unidadeCombo.getSelectedItem();
                String quantidadeTexto = quantidadeField.getText().trim().replace(",", ".");
                String estoqueMinimoTexto = estoqueMinimoField.getText().trim().replace(",", ".");
                if (nome.isEmpty() || quantidadeTexto.isEmpty() || estoqueMinimoTexto.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Preencha todos os campos para continuar.", "Atenção",
                                        JOptionPane.WARNING_MESSAGE);
                        return;
                }
                double quantidade;
                double estoqueMinimo;
                try {
                        quantidade = Double.parseDouble(quantidadeTexto);
                        estoqueMinimo = Double.parseDouble(estoqueMinimoTexto);
                } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(this, "Quantidade e estoque mínimo devem ser números válidos.",
                                        "Erro", JOptionPane.ERROR_MESSAGE);
                        return;
                }
                if (quantidade < 0 || estoqueMinimo < 0) {
                        JOptionPane.showMessageDialog(this, "Quantidade e estoque mínimo não podem ser negativos.",
                                        "Atenção", JOptionPane.WARNING_MESSAGE);
                        return;
                }
                IngredienteDAO dao = new IngredienteDAO();
                Ingrediente ingrediente = new Ingrediente(nome, categoria, unidade, quantidade, estoqueMinimo);
                if (idEstoqueSelecionado == null) {
                        dao.inserir(ingrediente);
                        JOptionPane.showMessageDialog(this, "Ingrediente cadastrado com sucesso!", "Sucesso",
                                        JOptionPane.INFORMATION_MESSAGE);
                } else {
                        ingrediente.setId(idEstoqueSelecionado);
                        dao.alterar(ingrediente);
                        JOptionPane.showMessageDialog(this, "Ingrediente atualizado com sucesso!", "Sucesso",
                                        JOptionPane.INFORMATION_MESSAGE);
                }
                limparFormulario();
                carregarDadosTabela(null);
        }


        // EXCLUIR
        private void deletarIngredienteComAdmin() {
                if (idEstoqueSelecionado == null) {
                        JOptionPane.showMessageDialog(this, "Selecione um ingrediente na tabela para excluir.",
                                        "Atenção", JOptionPane.WARNING_MESSAGE);
                        return;
                }
                int confirmar = JOptionPane.showConfirmDialog(this, "Deseja realmente excluir este ingrediente?",
                                "Confirmar exclusão", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (confirmar != JOptionPane.YES_OPTION) {
                        return;
                }
                JPasswordField pf = new JPasswordField();
                int ok = JOptionPane.showConfirmDialog(this, pf, "Digite a SENHA ADMIN para confirmar a exclusão:",
                                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (ok == JOptionPane.OK_OPTION) {
                        String senhaAdmin = new String(pf.getPassword());
                        if ("LaSottamPizzaria".equals(senhaAdmin)) {
                                IngredienteDAO dao = new IngredienteDAO();
                                dao.excluir(idEstoqueSelecionado);
                                JOptionPane.showMessageDialog(this, "Ingrediente excluído com sucesso!", "Sucesso",
                                                JOptionPane.INFORMATION_MESSAGE);
                                limparFormulario();
                                carregarDadosTabela(null);
                        } else {
                                JOptionPane.showMessageDialog(this, "Senha incorreta. Ação cancelada.", "Erro",
                                                JOptionPane.ERROR_MESSAGE);
                        }
                }
        }


        // LIMPAR
        private void limparFormulario() {
                idEstoqueSelecionado = null;
                nomeField.setText("");
                categoriaCombo.setSelectedIndex(0);
                unidadeCombo.setSelectedIndex(0);
                quantidadeField.setText("");
                estoqueMinimoField.setText("");
                tabelaEstoque.clearSelection();
        }


        // LABELS
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

        private JComboBox<String> criarCombo(String[] opcoes) {
                JComboBox<String> combo = new JComboBox<>(opcoes);
                combo.setFont(FonteUtil.carregarFonte(POPPINS, 13f));
                combo.setBackground(WHITE);
                combo.setPreferredSize(new Dimension(200, 32));
                combo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
                combo.setAlignmentX(Component.LEFT_ALIGNMENT);
                return combo;
        }

        private void selecionarNoCombo(JComboBox<String> combo, String valor) {
                for (int i = 0; i < combo.getItemCount(); i++) {
                        if (combo.getItemAt(i).equalsIgnoreCase(valor)) {
                                combo.setSelectedIndex(i);
                                return;
                        }
                }
                combo.setSelectedIndex(0); 
        }


        // PAINEL ARREDONDADO
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


        // CAMPO DE TEXTO ARREDONDADO
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


        // BOTÃO ARREDONDADO
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