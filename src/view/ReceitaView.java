package view;

import dao.IngredienteDAO;
import dao.ProdutoDAO;
import dao.ReceitaDAO;
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
import model.ItemReceita;
import model.Produto;
import model.Tamanho;

public class ReceitaView extends JPanel {
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
        private JTable tabelaReceita;
        private DefaultTableModel tableModel;
        private JComboBox<Produto> produtoCombo;
        private JComboBox<Tamanho> tamanhoCombo;
        private JComboBox<Ingrediente> ingredienteCombo;
        private RoundedTextField quantidadeField;
        private RoundedTextField buscaField;
        private JTextArea receitaCompletaArea;
        // Guarda o ID do item de receita selecionado
        private Integer idReceitaSelecionado = null;

        public ReceitaView() {
                this(null);
        }

        public ReceitaView(Runnable onVoltarAction) {
                this.onVoltarAction = onVoltarAction;
                setLayout(new BorderLayout());
                setBackground(BG_LIGHT_BLUE);
                setBorder(new EmptyBorder(20, 30, 20, 30));
                add(buildHeader(), BorderLayout.NORTH);
                add(buildBody(), BorderLayout.CENTER);
                carregarCombos();
                carregarDadosTabela(null);
                atualizarReceitaCompleta();
        }

        // =========================================================
        // CABEÇALHO
        // =========================================================
        private JPanel buildHeader() {
                JPanel header = new JPanel(new BorderLayout());
                header.setOpaque(false);
                header.setBorder(new EmptyBorder(0, 0, 15, 0));
                JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
                left.setOpaque(false);
                JLabel receitaLabel = new JLabel("RECEITA");
                receitaLabel.setFont(FonteUtil.carregarFonte(AUDIOWIDE, 64f));
                receitaLabel.setForeground(PURPLE_CARD);
                receitaLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, PURPLE_CARD));
                JButton backBtn = makeCircleButton("<");
                backBtn.addActionListener(e -> {
                        if (onVoltarAction != null) {
                                onVoltarAction.run();
                        }
                });
                left.add(receitaLabel);
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

        // =========================================================
        // CORPO
        // =========================================================
        private JPanel buildBody() {
                JPanel body = new JPanel(new GridBagLayout());
                body.setOpaque(false);
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.fill = GridBagConstraints.BOTH;
                gbc.insets = new Insets(5, 10, 5, 10);
                // TABELA
                gbc.gridx = 0; //coluna 0
                gbc.gridy = 0; //linha 0
                gbc.gridheight = 1; //ocupa 1 linha
                gbc.weightx = 1.0; //espande horizontalmente
                gbc.weighty = 0.8; //espande verticalmente
                body.add(buildTablePanel(), gbc);

                // RECEITA COMPLETA
                gbc.gridy = 1; //linha 1
                gbc.weighty = 0.2; //espande verticalmente
                body.add(buildReceitaCompletaCard(), gbc);

                // PAINEL DIREITO (ocupa as duas linhas, alinhado com o topo da tabela)
                gbc.gridx = 1; //coluna 1
                gbc.gridy = 0; //linha 0
                gbc.gridheight = 2; //ocupa 2 linhas
                gbc.weightx = 0.0; //não espande horizontalmente
                gbc.weighty = 1.0; //espande verticalmente
                JPanel rightPanel = new JPanel(new GridBagLayout());
                rightPanel.setOpaque(false);
                GridBagConstraints gbcRight = new GridBagConstraints();
                gbcRight.gridx = 0;
                gbcRight.fill = GridBagConstraints.HORIZONTAL;
                gbcRight.anchor = GridBagConstraints.NORTH;
                gbcRight.insets = new Insets(0, 0, 15, 0);
                // FORMULÁRIO
                gbcRight.gridy = 0; //linha 0
                gbcRight.weighty = 0.0; //não espande verticalmente
                rightPanel.add(buildFormCard(), gbcRight);
                // BUSCA
                gbcRight.gridy = 1; //linha 1
                gbcRight.weighty = 1.0;
                gbcRight.insets = new Insets(0, 0, 0, 0);
                rightPanel.add(buildSearchCard(), gbcRight);
                body.add(rightPanel, gbc);
                return body;
        }

        // =========================================================
        // TABELA
        // =========================================================
        private JPanel buildTablePanel() {
                JPanel panel = new JPanel(new BorderLayout());
                panel.setOpaque(false);
                String[] colunas = { "ID", "Produto", "Tamanho", "Ingrediente", "Quantidade", "Unidade" };
                tableModel = new DefaultTableModel(colunas, 0) {
                        @Override
                        public boolean isCellEditable(int row, int column) {
                                return false;
                        }
                };
                tabelaReceita = new JTable(tableModel);
                tabelaReceita.setFont(FonteUtil.carregarFonte(POPPINS, 13f));
                tabelaReceita.setRowHeight(32);
                tabelaReceita.getTableHeader().setFont(FonteUtil.carregarFonte(POPPINS, 14f));
                tabelaReceita.getTableHeader().setBackground(WHITE);
                tabelaReceita.getTableHeader().setForeground(PURPLE_CARD);
                tabelaReceita.setShowGrid(true);
                tabelaReceita.setGridColor(Color.BLACK);

                // Centralizar todas as colunas
                DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
                centerRenderer.setHorizontalAlignment(JLabel.CENTER);
                for (int i = 0; i < tabelaReceita.getColumnCount(); i++) {
                        tabelaReceita.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
                }

                // Clique na tabela
                tabelaReceita.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                                int row = tabelaReceita.getSelectedRow();
                                if (row != -1) {
                                        idReceitaSelecionado = (Integer) tableModel.getValueAt(row, 0);
                                        selecionarProdutoNoCombo(String.valueOf(tableModel.getValueAt(row, 1)));
                                        selecionarTamanhoNoCombo(String.valueOf(tableModel.getValueAt(row, 2)));
                                        selecionarIngredienteNoCombo(String.valueOf(tableModel.getValueAt(row, 3)));
                                        quantidadeField.setText(String.valueOf(tableModel.getValueAt(row, 4)));
                                        atualizarReceitaCompleta();
                                }
                        }
                });
                JScrollPane scroll = new JScrollPane(tabelaReceita);
                scroll.getViewport().setBackground(WHITE);
                scroll.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
                panel.add(scroll, BorderLayout.CENTER);
                return panel;
        }

        // =========================================================
        // FORMULÁRIO
        // =========================================================
        private RoundedPanel buildFormCard() {
                RoundedPanel card = new RoundedPanel(25, PURPLE_CARD);
                Dimension formSize = new Dimension(360, 440);
                card.setPreferredSize(formSize);
                card.setMaximumSize(formSize);
                card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
                card.setBorder(new EmptyBorder(15, 20, 15, 20));
                card.add(sectionTitle("CADASTRAR / EDITAR"));
                card.add(Box.createVerticalStrut(10));
                // PRODUTO
                card.add(fieldLabel("PRODUTO"));
                card.add(Box.createVerticalStrut(2));
                produtoCombo = new JComboBox<>();
                estilizarCombo(produtoCombo);
                produtoCombo.setRenderer(new NomeCellRenderer());
                produtoCombo.addActionListener(e -> atualizarReceitaCompleta());
                card.add(produtoCombo);
                card.add(Box.createVerticalStrut(8));
                // TAMANHO
                card.add(fieldLabel("TAMANHO"));
                card.add(Box.createVerticalStrut(2));
                tamanhoCombo = new JComboBox<>(Tamanho.values());
                estilizarCombo(tamanhoCombo);
                tamanhoCombo.addActionListener(e -> atualizarReceitaCompleta());
                card.add(tamanhoCombo);
                card.add(Box.createVerticalStrut(8));
                // INGREDIENTE
                card.add(fieldLabel("INGREDIENTE"));
                card.add(Box.createVerticalStrut(2));
                ingredienteCombo = new JComboBox<>();
                estilizarCombo(ingredienteCombo);
                ingredienteCombo.setRenderer(new NomeCellRenderer());
                card.add(ingredienteCombo);
                card.add(Box.createVerticalStrut(8));
                // QUANTIDADE
                card.add(fieldLabel("QUANTIDADE NECESSÁRIA"));
                card.add(Box.createVerticalStrut(2));
                quantidadeField = new RoundedTextField(20);
                quantidadeField.setToolTipText("Quantidade consumida por pizza nesse tamanho. Exemplo: 250");
                card.add(quantidadeField);
                card.add(Box.createVerticalStrut(12));
                // BOTÕES
                JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
                btnPanel.setOpaque(false);
                btnPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
                RoundedButton limparBtn = new RoundedButton("LIMPAR");
                limparBtn.addActionListener(e -> limparFormulario());
                RoundedButton deletarBtn = new RoundedButton("EXCLUIR", RED_BTN, RED_BTN_HOVER);
                deletarBtn.addActionListener(e -> deletarItemComAdmin());
                RoundedButton salvarBtn = new RoundedButton("SALVAR >");
                salvarBtn.addActionListener(e -> salvarItem());
                btnPanel.add(limparBtn);
                btnPanel.add(deletarBtn);
                btnPanel.add(salvarBtn);
                card.add(btnPanel);
                return card;
        }

        // =========================================================
        // BUSCA
        // =========================================================
        private RoundedPanel buildSearchCard() {
                RoundedPanel card = new RoundedPanel(25, PURPLE_CARD);
                Dimension searchSize = new Dimension(360, 120);
                card.setPreferredSize(searchSize);
                card.setMaximumSize(searchSize);
                card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
                card.setBorder(new EmptyBorder(12, 20, 12, 20));
                JLabel title = sectionTitle("PESQUISAR RECEITA");
                card.add(title);
                card.add(Box.createVerticalStrut(8));
                buscaField = new RoundedTextField(20);
                buscaField.setToolTipText("Digite o nome do produto ou do ingrediente");
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
        // RECEITA COMPLETA (junta todos os ingredientes de um produto+tamanho)
        // =========================================================
        private RoundedPanel buildReceitaCompletaCard() {
                RoundedPanel card = new RoundedPanel(25, PURPLE_CARD);
                card.setPreferredSize(new Dimension(10, 150));
                card.setLayout(new BorderLayout(0, 8));
                card.setBorder(new EmptyBorder(12, 20, 12, 20));

                JLabel title = sectionTitle("RECEITA COMPLETA");
                card.add(title, BorderLayout.NORTH);

                receitaCompletaArea = new JTextArea();
                receitaCompletaArea.setEditable(false);
                receitaCompletaArea.setLineWrap(true);
                receitaCompletaArea.setFont(FonteUtil.carregarFonte(POPPINS, 13f));
                receitaCompletaArea.setForeground(PURPLE_CARD);
                receitaCompletaArea.setBackground(WHITE);
                receitaCompletaArea.setBorder(new EmptyBorder(8, 10, 8, 10));

                JScrollPane scroll = new JScrollPane(receitaCompletaArea);
                scroll.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
                card.add(scroll, BorderLayout.CENTER);

                return card;
        }

        private void atualizarReceitaCompleta() {
                Produto produto = (Produto) produtoCombo.getSelectedItem();
                Tamanho tamanho = (Tamanho) tamanhoCombo.getSelectedItem();
                if (produto == null || tamanho == null) {
                        receitaCompletaArea.setText("");
                        return;
                }

                ReceitaDAO dao = new ReceitaDAO();
                List<ItemReceita> itens = dao.listarPorProdutoETamanho(produto.getId(), tamanho);

                if (itens.isEmpty()) {
                        receitaCompletaArea.setText(produto.getNome() + " - " + tamanho.name()
                                        + "\n(nenhum ingrediente cadastrado ainda)");
                        return;
                }

                StringBuilder texto = new StringBuilder();
                texto.append(produto.getNome()).append(" - ").append(tamanho.name()).append("\n");
                for (ItemReceita item : itens) {
                        texto.append("- ").append(item.getIngrediente().getNome()).append(": ")
                                        .append(item.getQuantidadeNecessaria()).append(" ")
                                        .append(item.getIngrediente().getUnidade()).append("\n");
                }
                receitaCompletaArea.setText(texto.toString());
        }

        // =========================================================
        // COMBOS AUXILIARES
        // =========================================================
        private void estilizarCombo(JComboBox<?> combo) {
                combo.setFont(FonteUtil.carregarFonte(POPPINS, 13f));
                combo.setBackground(WHITE);
                combo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
                combo.setAlignmentX(Component.LEFT_ALIGNMENT);
        }

        // Mostra o nome do Produto ou do Ingrediente no lugar do objeto inteiro
        private class NomeCellRenderer extends DefaultListCellRenderer {
                @Override
                public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                boolean isSelected, boolean cellHasFocus) {
                        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                        if (value instanceof Produto) {
                                setText(((Produto) value).getNome());
                        } else if (value instanceof Ingrediente) {
                                setText(((Ingrediente) value).getNome());
                        }
                        return this;
                }
        }

        private void carregarCombos() {
                produtoCombo.removeAllItems();
                for (Produto p : new ProdutoDAO().listarTodos()) {
                        produtoCombo.addItem(p);
                }
                ingredienteCombo.removeAllItems();
                for (Ingrediente i : new IngredienteDAO().listarTodos()) {
                        ingredienteCombo.addItem(i);
                }
        }

        private void selecionarProdutoNoCombo(String nome) {
                for (int i = 0; i < produtoCombo.getItemCount(); i++) {
                        if (produtoCombo.getItemAt(i).getNome().equalsIgnoreCase(nome)) {
                                produtoCombo.setSelectedIndex(i);
                                return;
                        }
                }
        }

        private void selecionarIngredienteNoCombo(String nome) {
                for (int i = 0; i < ingredienteCombo.getItemCount(); i++) {
                        if (ingredienteCombo.getItemAt(i).getNome().equalsIgnoreCase(nome)) {
                                ingredienteCombo.setSelectedIndex(i);
                                return;
                        }
                }
        }

        private void selecionarTamanhoNoCombo(String nome) {
                for (Tamanho t : Tamanho.values()) {
                        if (t.name().equalsIgnoreCase(nome)) {
                                tamanhoCombo.setSelectedItem(t);
                                return;
                        }
                }
        }

        // =========================================================
        // BANCO DE DADOS
        // =========================================================
        private void carregarDadosTabela(String filtro) {
                tableModel.setRowCount(0);
                ReceitaDAO dao = new ReceitaDAO();
                List<ItemReceita> lista = dao.listarTodos();
                for (ItemReceita item : lista) {
                        if (filtro != null && !filtro.isEmpty()) {
                                String f = filtro.toLowerCase();
                                boolean bateuProduto = item.getProduto().getNome().toLowerCase().contains(f);
                                boolean bateuIngrediente = item.getIngrediente().getNome().toLowerCase().contains(f);
                                if (!bateuProduto && !bateuIngrediente) {
                                        continue;
                                }
                        }
                        tableModel.addRow(new Object[] {
                                        item.getId(), item.getProduto().getNome(), item.getTamanho().name(),
                                        item.getIngrediente().getNome(), item.getQuantidadeNecessaria(),
                                        item.getIngrediente().getUnidade() });
                }
        }

        // =========================================================
        // SALVAR / EDITAR
        // =========================================================
        private void salvarItem() {
                Produto produto = (Produto) produtoCombo.getSelectedItem();
                Tamanho tamanho = (Tamanho) tamanhoCombo.getSelectedItem();
                Ingrediente ingrediente = (Ingrediente) ingredienteCombo.getSelectedItem();
                String quantidadeTexto = quantidadeField.getText().trim().replace(",", ".");

                if (produto == null || ingrediente == null) {
                        JOptionPane.showMessageDialog(this,
                                        "Cadastre um Produto e um Ingrediente antes de montar a receita.", "Atenção",
                                        JOptionPane.WARNING_MESSAGE);
                        return;
                }
                if (quantidadeTexto.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Informe a quantidade necessária.", "Atenção",
                                        JOptionPane.WARNING_MESSAGE);
                        return;
                }
                double quantidade;
                try {
                        quantidade = Double.parseDouble(quantidadeTexto);
                } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(this, "Quantidade deve ser um número válido.", "Erro",
                                        JOptionPane.ERROR_MESSAGE);
                        return;
                }
                if (quantidade <= 0) {
                        JOptionPane.showMessageDialog(this, "Quantidade deve ser maior que zero.", "Atenção",
                                        JOptionPane.WARNING_MESSAGE);
                        return;
                }

                ReceitaDAO dao = new ReceitaDAO();
                ItemReceita item = new ItemReceita(produto, tamanho, ingrediente, quantidade);
                try {
                        if (idReceitaSelecionado == null) {
                                dao.inserir(item);
                                JOptionPane.showMessageDialog(this, "Item de receita cadastrado com sucesso!",
                                                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                                item.setId(idReceitaSelecionado);
                                dao.alterar(item);
                                JOptionPane.showMessageDialog(this, "Item de receita atualizado com sucesso!",
                                                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                        }
                } catch (RuntimeException e) {
                        // Acontece se esse produto+tamanho+ingrediente já estiver cadastrado
                        JOptionPane.showMessageDialog(this,
                                        "Esse ingrediente já está na receita desse produto/tamanho.", "Atenção",
                                        JOptionPane.WARNING_MESSAGE);
                        return;
                }
                limparFormulario();
                carregarDadosTabela(null);
                atualizarReceitaCompleta();
        }

        // =========================================================
        // EXCLUIR
        // =========================================================
        private void deletarItemComAdmin() {
                if (idReceitaSelecionado == null) {
                        JOptionPane.showMessageDialog(this, "Selecione um item na tabela para excluir.", "Atenção",
                                        JOptionPane.WARNING_MESSAGE);
                        return;
                }
                int confirmar = JOptionPane.showConfirmDialog(this, "Deseja realmente excluir este item da receita?",
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
                                ReceitaDAO dao = new ReceitaDAO();
                                dao.excluir(idReceitaSelecionado);
                                JOptionPane.showMessageDialog(this, "Item excluído com sucesso!", "Sucesso",
                                                JOptionPane.INFORMATION_MESSAGE);
                                limparFormulario();
                                carregarDadosTabela(null);
                                atualizarReceitaCompleta();
                        } else {
                                JOptionPane.showMessageDialog(this, "Senha incorreta. Ação cancelada.", "Erro",
                                                JOptionPane.ERROR_MESSAGE);
                        }
                }
        }

        // =========================================================
        // LIMPAR
        // =========================================================
        private void limparFormulario() {
                idReceitaSelecionado = null;
                if (produtoCombo.getItemCount() > 0) {
                        produtoCombo.setSelectedIndex(0);
                }
                tamanhoCombo.setSelectedIndex(0);
                if (ingredienteCombo.getItemCount() > 0) {
                        ingredienteCombo.setSelectedIndex(0);
                }
                quantidadeField.setText("");
                tabelaReceita.clearSelection();
        }

        // =========================================================
        // LABELS
        // =========================================================
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

        // =========================================================
        // PAINEL ARREDONDADO
        // =========================================================
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

        // =========================================================
        // CAMPO DE TEXTO ARREDONDADO
        // =========================================================
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

        // =========================================================
        // BOTÃO ARREDONDADO
        // =========================================================
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