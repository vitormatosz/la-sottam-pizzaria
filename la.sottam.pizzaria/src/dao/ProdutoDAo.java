package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import connection.ConnectionFactory;
import model.Produto;

public class ProdutoDAo {
    public void inserir(Produto produto) {
        String sql = "INSERT INTO produto (nome, categoria, descricao, preco, disponivel) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, produto.getNome());
            stmt.setString(2, produto.getCategoria());
            stmt.setString(3, produto.getDescricao());
            stmt.setDouble(4, produto.getPreco());
            stmt.setBoolean(5, produto.isDisponivel());
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir produto: " + e.getMessage());
        }
    }

    public Produto buscarPorIdOuNome(int id, String nome) {
    String sql = "SELECT * FROM produto WHERE id = ? OR nome LIKE ?";
    try (Connection conn = ConnectionFactory.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, id);
        stmt.setString(2, "%" + nome + "%");
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return montarProduto(rs);
            }
        }
    } catch (SQLException e) {
        throw new RuntimeException("Erro ao buscar produto: " + e.getMessage());
    }
    return null;
}

    public List<Produto> listarTodos() {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT * FROM produto";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Produto p = montarProduto(rs);
                produtos.add(p);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar produtos: " + e.getMessage());
        }
        return produtos;
    }

    public void alterar(Produto produto) {
        String sql = "UPDATE produto SET nome = ?, categoria = ?, descricao = ?, preco = ?, disponivel = ? WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, produto.getNome());
            stmt.setString(2, produto.getCategoria());
            stmt.setString(3, produto.getDescricao());
            stmt.setDouble(4, produto.getPreco());
            stmt.setBoolean(5, produto.isDisponivel());
            stmt.setInt(6, produto.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao alterar produto: " + e.getMessage());
        }
    }

    public void excluir(int id) {
        String sql = "DELETE FROM produto WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir produto: " + e.getMessage());
        }
    }

    private Produto montarProduto(ResultSet rs) throws SQLException {
        Produto p = new Produto(
        rs.getString("nome"),
        rs.getString("categoria"), 
        rs.getString("descricao"), 
        rs.getDouble("preco"));
        p.setId(rs.getInt("id"));
        p.setDisponivel(rs.getBoolean("disponivel"));
        return p;
    }
}