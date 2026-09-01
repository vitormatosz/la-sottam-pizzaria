package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import connection.ConnectionFactory;
import model.Ingrediente;

public class IngredienteDAO {
    public void inserir(Ingrediente ingrediente) {
        String sql = "INSERT INTO ingrediente (nome, categoria, unidade, quantidade, estoque_minimo) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, ingrediente.getNome());
            stmt.setString(2, ingrediente.getCategoria());
            stmt.setString(3, ingrediente.getUnidade());
            stmt.setDouble(4, ingrediente.getQuantidade());
            stmt.setDouble(5, ingrediente.getEstoqueMinimo());
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir ingrediente: " + e.getMessage());
        }
    }

    public Ingrediente buscarPorIdOuNome(int id, String nome) {
        String sql = "SELECT * FROM ingrediente WHERE id = ? OR nome LIKE ?";
        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.setString(2, "%" + nome + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Ingrediente ing = new Ingrediente(
                            rs.getString("nome"),
                            rs.getString("categoria"),
                            rs.getString("unidade"),
                            rs.getDouble("quantidade"),
                            rs.getDouble("estoque_minimo"));
                    ing.setId(rs.getInt("id"));
                    return ing;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar ingrediente: " + e.getMessage());
        }
        return null;
    }

    public List<Ingrediente> listarTodos() {
        List<Ingrediente> ingredientes = new ArrayList<>();
        String sql = "SELECT * FROM ingrediente";
        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Ingrediente ing = new Ingrediente(
                        rs.getString("nome"),
                        rs.getString("categoria"),
                        rs.getString("unidade"),
                        rs.getDouble("quantidade"),
                        rs.getDouble("estoque_minimo"));
                ing.setId(rs.getInt("id"));
                ingredientes.add(ing);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar ingredientes: " + e.getMessage());
        }
        return ingredientes;
    }

    public void alterar(Ingrediente ingrediente) {
        String sql = "UPDATE ingrediente SET nome = ?, categoria = ?, unidade = ?, quantidade = ?, estoque_minimo = ? WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, ingrediente.getNome());
            stmt.setString(2, ingrediente.getCategoria());
            stmt.setString(3, ingrediente.getUnidade());
            stmt.setDouble(4, ingrediente.getQuantidade());
            stmt.setDouble(5, ingrediente.getEstoqueMinimo());
            stmt.setInt(6, ingrediente.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao alterar ingrediente: " + e.getMessage());
        }
    }

    public void excluir(int id) {
        String sql = "DELETE FROM ingrediente WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir ingrediente: " + e.getMessage());
        }
    }
}
