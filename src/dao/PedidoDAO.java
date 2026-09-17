package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import connection.ConnectionFactory;
import model.*;

public class PedidoDAO {

    public void inserir(Pedido pedido) {
        String sqlPedido = "INSERT INTO pedido (cliente_id, forma_pag, frete, data_pedido, tipo_saida) VALUES (?, ?, ?, ?, ?)";
        String sqlItem = "INSERT INTO item_pedido (pedido_id, produto_id, segundo_sabor,tamanho, quantidade, preco_unitario) VALUES (?, ?, ?, ?, ?)";

        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false); 

            int pedidoId;
            try (PreparedStatement stmt = conn.prepareStatement(sqlPedido, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1, pedido.getCliente().getId());
                stmt.setString(2, pedido.getFormaPag());
                stmt.setDouble(3, pedido.getFrete());
                stmt.setTimestamp(4, new Timestamp(pedido.getDataPedido().getTime()));
                stmt.setString(5, pedido.getTipoDeSaida().name());
                stmt.executeUpdate();

                try (ResultSet keys = stmt.getGeneratedKeys()) {
                    keys.next();
                    pedidoId = keys.getInt(1);
                }
            }
            pedido.setId(pedidoId);

            try (PreparedStatement stmt = conn.prepareStatement(sqlItem)) {
                for (ItemPedido item : pedido.getItens()) {
                    stmt.setInt(1, pedidoId);
                    stmt.setInt(2, item.getProduto().getId());
                    if (item.isMeioAMeio()) {
                        stmt.setInt(3, item.getSegundoSabor().getId());
                    } else {
                        stmt.setNull(3, java.sql.Types.INTEGER);
                    }
                    stmt.setString(4, item.getTamanho().name());
                    stmt.setInt(5, item.getQuantidade());
                    stmt.setDouble(6, item.getPrecoUnitario());
                    stmt.addBatch(); 
                }
                stmt.executeBatch();
            }

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    /* ignora erro no rollback */ }
            }
            throw new RuntimeException("Erro ao inserir pedido: " + e.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException ex) {
                    /* ignora */ }
            }
        }
    }

    public Pedido buscarPorId(int id) {
        String sqlPedido = "SELECT * FROM pedido WHERE id = ?";
        String sqlItens = "SELECT * FROM item_pedido WHERE pedido_id = ?";
        ClienteDAO clienteDAO = new ClienteDAO();
        ProdutoDAO produtoDAO = new ProdutoDAO();

        try (Connection conn = ConnectionFactory.getConnection()) {
            Pedido pedido;

            try (PreparedStatement stmt = conn.prepareStatement(sqlPedido)) {
                stmt.setInt(1, id);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (!rs.next())
                        return null;

                    Cliente cliente = clienteDAO.buscarPorId(rs.getInt("cliente_id"));
                    TipoSaida tipoSaida = TipoSaida.valueOf(rs.getString("tipo_saida"));

                    pedido = new Pedido(cliente, rs.getString("forma_pag"), rs.getString("observacao"), rs.getDate("dataPedido"), tipoSaida);
                    pedido.setId(rs.getInt("id"));
                    pedido.setFrete(rs.getDouble("frete"));
                    pedido.setDataPedido(rs.getTimestamp("data_pedido"));
                }
            }

            try (PreparedStatement stmt = conn.prepareStatement(sqlItens)) {
                stmt.setInt(1, id);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        Produto produto = produtoDAO.buscarPorId(rs.getInt("produto_id"));
                        Produto segundoSabor = null;
                        if (rs.getInt("segundo_sabor") != 0) {
                            segundoSabor = produtoDAO.buscarPorId(rs.getInt("segundo_sabor"));
                        }
                        Tamanho tamanho = Tamanho.valueOf(rs.getString("tamanho"));
                        ItemPedido item = new ItemPedido(produto, tamanho, rs.getInt("quantidade"));
                        item.setId(rs.getInt("id"));
                        item.setPrecoUnitario(rs.getDouble("preco_unitario"));
                        pedido.adicionarItem(item);
                    }
                }
            }

            return pedido;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar pedido: " + e.getMessage());
        }
    }

    public List<Pedido> listarTodos() {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT id FROM pedido";
        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                pedidos.add(buscarPorId(rs.getInt("id")));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar pedidos: " + e.getMessage());
        }
        return pedidos;
    }
}