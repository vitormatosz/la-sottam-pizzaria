package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import connection.ConnectionFactory;
import model.Cliente;

public class ClienteDAO {

    public void inserir(Cliente cliente) {
        String sql = "INSERT INTO cliente (nome, numero_tel, endereco) VALUES (?, ?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getNumeroTel());
            stmt.setString(3, cliente.getEndereco());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir cliente: " + e.getMessage());
        }
    }

    public Cliente buscarPorId(int id) {
        String sql = "SELECT * FROM cliente WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Cliente c = new Cliente(rs.getString("nome"), rs.getString("numero_tel"), rs.getString("endereco"));
                    c.setId(rs.getInt("id"));
                    return c;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar cliente: " + e.getMessage());
        }
        return null;
    }

    public List<Cliente> listarTodos() {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM cliente";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Cliente c = new Cliente(rs.getString("nome"), rs.getString("numero_tel"), rs.getString("endereco"));
                c.setId(rs.getInt("id"));
                clientes.add(c);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar clientes: " + e.getMessage());
        }
        return clientes;
    }

    public void alterar(Cliente cliente) {
        String sql = "UPDATE cliente SET nome = ?, numero_tel = ?, endereco = ? WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getNumeroTel());
            stmt.setString(3, cliente.getEndereco());
            stmt.setInt(4, cliente.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao alterar cliente: " + e.getMessage());
        }
    }

    public void excluir(int id) {
        String sql = "DELETE FROM cliente WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir cliente: " + e.getMessage());
        }
    }
}