// package dao;

// import java.sql.*;
// import java.util.ArrayList;
// import java.util.List;
// import connection.ConnectionFactory;
// import model.Pedido;
// import model.Produto;

// public class PedidoDAO {
//     String sqlPedido = "INSERT INTO pedido (cliente_id, form_pag, frete, data_pedido, tipo_saida) VALUES (?, ?, ?, ?, ?)";
//     String sqlItem = "INSERT INTO item_pedido (pedido_id, produto_id, quantidade, preco_unitario) VALUES (?, ?, ?, ?)";
//         try (Connection conn = ConnectionFactory.getConnection();
//              PreparedStatement stmt = conn.prepareStatement(sql)) {
//             stmt.setObject(1, pedido.getCliente());
//             stmt.setString(2, pedido.getProduto());
//             stmt.setInt(3, pedido.getQuantidade());
//             stmt.setDouble(4, pedido.getValorTotal());
//         } catch (SQLException e) {
//             throw new RuntimeException("Erro ao inserir pedido: " + e.getMessage());
//         }
//     }

//     public Produto buscarPorId(int id) {
//         String sql = "SELECT * FROM produto WHERE id = ?";
//         try (Connection conn = ConnectionFactory.getConnection();
//              PreparedStatement stmt = conn.prepareStatement(sql)) {
//             stmt.setInt(1, id);
//             try (ResultSet rs = stmt.executeQuery()) {
//                 if (rs.next()) {
//                     return montarProduto(rs);
//                 }
//             }
//         } catch (SQLException e) {
//             throw new RuntimeException("Erro ao buscar produto: " + e.getMessage());
//         }
//         return null;
//     }

//     public List<Produto> listarTodos() {
//         List<Produto> produtos = new ArrayList<>();
//         String sql = "SELECT * FROM produto";
//         try (Connection conn = ConnectionFactory.getConnection();
//              PreparedStatement stmt = conn.prepareStatement(sql);
//              ResultSet rs = stmt.executeQuery()) {
//             while (rs.next()) {
//                 Produto p = montarProduto(rs);
//                 produtos.add(p);
//             }
//         } catch (SQLException e) {
//             throw new RuntimeException("Erro ao listar produtos: " + e.getMessage());
//         }
//         return produtos;
//     }

//     public void alterar(Produto produto) {
//         String sql = "UPDATE produto SET nome = ?, categoria = ?, descricao = ?, preco = ?, disponivel = ? WHERE id = ?";
//         try (Connection conn = ConnectionFactory.getConnection();
//              PreparedStatement stmt = conn.prepareStatement(sql)) {
//             stmt.setString(1, produto.getNome());
//             stmt.setString(2, produto.getCategoria());
//             stmt.setString(3, produto.getDescricao());
//             stmt.setDouble(4, produto.getPreco());
//             stmt.setBoolean(5, produto.isDisponivel());
//             stmt.setInt(6, produto.getId());
//             stmt.executeUpdate();
//         } catch (SQLException e) {
//             throw new RuntimeException("Erro ao alterar produto: " + e.getMessage());
//         }
//     }

//     public void excluir(int id) {
//         String sql = "DELETE FROM produto WHERE id = ?";
//         try (Connection conn = ConnectionFactory.getConnection();
//              PreparedStatement stmt = conn.prepareStatement(sql)) {
//             stmt.setInt(1, id);
//             stmt.executeUpdate();
//         } catch (SQLException e) {
//             throw new RuntimeException("Erro ao excluir produto: " + e.getMessage());
//         }
//     }

//     private Produto montarProduto(ResultSet rs) throws SQLException {
//         Produto p = new Produto(
//         rs.getString("nome"),
//         rs.getString("categoria"), 
//         rs.getString("descricao"), 
//         rs.getDouble("preco"));
//         p.setId(rs.getInt("id"));
//         p.setDisponivel(rs.getBoolean("disponivel"));
//         return p;
// }


// // package dao;

// // import java.sql.*;
// // import java.util.ArrayList;
// // import java.util.List;
// // import connection.ConnectionFactory;
// // import model.*;

// // public class PedidoDAO {

// //     public void inserir(Pedido pedido) {
// //         String sqlPedido = "INSERT INTO pedido (cliente_id, forma_pag, frete, data_pedido, tipo_saida) VALUES (?, ?, ?, ?, ?)";
// //         String sqlItem = "INSERT INTO item_pedido (pedido_id, produto_id, tamanho, quantidade, preco_unitario) VALUES (?, ?, ?, ?, ?)";

// //         Connection conn = null;
// //         try {
// //             conn = ConnectionFactory.getConnection();
// //             conn.setAutoCommit(false); // inicia a transação: nada é salvo de verdade até o commit()

// //             // 1. insere o pedido e recupera o id gerado pelo banco
// //             int pedidoId;
// //             try (PreparedStatement stmt = conn.prepareStatement(sqlPedido, Statement.RETURN_GENERATED_KEYS)) {
// //                 stmt.setInt(1, pedido.getCliente().getId());
// //                 stmt.setString(2, pedido.getFormaPag());
// //                 stmt.setDouble(3, pedido.getFrete());
// //                 stmt.setTimestamp(4, new Timestamp(pedido.getDataPedido().getTime()));
// //                 stmt.setString(5, pedido.getTipoDeSaida().name());
// //                 stmt.executeUpdate();

// //                 try (ResultSet keys = stmt.getGeneratedKeys()) {
// //                     keys.next();
// //                     pedidoId = keys.getInt(1);
// //                 }
// //             }
// //             pedido.setId(pedidoId);

// //             // 2. insere cada item, usando o id do pedido que acabou de ser gerado
// //             try (PreparedStatement stmt = conn.prepareStatement(sqlItem)) {
// //                 for (ItemPedido item : pedido.getItens()) {
// //                     stmt.setInt(1, pedidoId);
// //                     stmt.setInt(2, item.getProduto().getId());
// //                     stmt.setString(3, item.getTamanho().name());
// //                     stmt.setInt(4, item.getQuantidade());
// //                     stmt.setDouble(5, item.getPrecoUnitario());
// //                     stmt.addBatch(); // acumula os inserts pra rodar todos de uma vez
// //                 }
// //                 stmt.executeBatch();
// //             }

// //             conn.commit(); // só aqui os dados ficam de fato salvos no banco
// //         } catch (SQLException e) {
// //             if (conn != null) {
// //                 try { conn.rollback(); } catch (SQLException ex) { /* ignora erro no rollback */ }
// //             }
// //             throw new RuntimeException("Erro ao inserir pedido: " + e.getMessage());
// //         } finally {
// //             if (conn != null) {
// //                 try { conn.setAutoCommit(true); conn.close(); } catch (SQLException ex) { /* ignora */ }
// //             }
// //         }
// //     }

// //     public Pedido buscarPorId(int id) {
// //         String sqlPedido = "SELECT * FROM pedido WHERE id = ?";
// //         String sqlItens = "SELECT * FROM item_pedido WHERE pedido_id = ?";
// //         ClienteDAO clienteDAO = new ClienteDAO();
// //         ProdutoDAO produtoDAO = new ProdutoDAO();

// //         try (Connection conn = ConnectionFactory.getConnection()) {
// //             Pedido pedido;

// //             try (PreparedStatement stmt = conn.prepareStatement(sqlPedido)) {
// //                 stmt.setInt(1, id);
// //                 try (ResultSet rs = stmt.executeQuery()) {
// //                     if (!rs.next()) return null;

// //                     Cliente cliente = clienteDAO.buscarPorId(rs.getInt("cliente_id"));
// //                     TipoSaida tipoSaida = TipoSaida.valueOf(rs.getString("tipo_saida"));

// //                     pedido = new Pedido(cliente, rs.getString("forma_pag"), tipoSaida);
// //                     pedido.setId(rs.getInt("id"));
// //                     pedido.setFrete(rs.getDouble("frete"));
// //                     pedido.setDataPedido(rs.getTimestamp("data_pedido"));
// //                 }
// //             }

// //             try (PreparedStatement stmt = conn.prepareStatement(sqlItens)) {
// //                 stmt.setInt(1, id);
// //                 try (ResultSet rs = stmt.executeQuery()) {
// //                     while (rs.next()) {
// //                         Produto produto = produtoDAO.buscarPorId(rs.getInt("produto_id"));
// //                         Tamanho tamanho = Tamanho.valueOf(rs.getString("tamanho"));
// //                         ItemPedido item = new ItemPedido(produto, tamanho, rs.getInt("quantidade"));
// //                         item.setId(rs.getInt("id"));
// //                         item.setPrecoUnitario(rs.getDouble("preco_unitario"));
// //                         pedido.adicionarItem(item);
// //                     }
// //                 }
// //             }

// //             return pedido;
// //         } catch (SQLException e) {
// //             throw new RuntimeException("Erro ao buscar pedido: " + e.getMessage());
// //         }
// //     }

// //     public List<Pedido> listarTodos() {
// //         List<Pedido> pedidos = new ArrayList<>();
// //         String sql = "SELECT id FROM pedido";
// //         try (Connection conn = ConnectionFactory.getConnection();
// //              PreparedStatement stmt = conn.prepareStatement(sql);
// //              ResultSet rs = stmt.executeQuery()) {
// //             while (rs.next()) {
// //                 pedidos.add(buscarPorId(rs.getInt("id")));
// //             }
// //         } catch (SQLException e) {
// //             throw new RuntimeException("Erro ao listar pedidos: " + e.getMessage());
// //         }
// //         return pedidos;
// //     }
// // }