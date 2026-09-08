package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import connection.ConnectionFactory;
import model.Funcionario;

public class FuncionarioDAO {
    public void inserir(Funcionario funcionario) {
        String sql = "INSERT INTO funcionario (nome_usuario, senha) VALUES (?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, funcionario.getNome_usuario());
            stmt.setString(2, funcionario.getSenha());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir funcionário: " + e.getMessage());
        }
    }

    public Funcionario buscarPorId(int id) {
        String sql = "SELECT * FROM funcionario WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Funcionario func = new Funcionario(
                            rs.getString("nome_usuario"),
                            rs.getString("senha"));
                    return func;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar funcionário por ID: " + e.getMessage());
        }
        return null;
    }

    public Funcionario buscarPorNomeUsuario(String nomeUsuario) {
        String sql = "SELECT * FROM funcionario WHERE nome_usuario = ?";
        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nomeUsuario);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Funcionario func = new Funcionario(rs.getString("nome_usuario"), rs.getString("senha"));
                    func.setId(rs.getInt("id"));
                    return func;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar funcionário por nome de usuário: " + e.getMessage());
        }
        return null;
    }

    public List<Funcionario> listarTodos() {
        List<Funcionario> funcionario = new ArrayList<>();
        String sql = "SELECT * FROM funcionario";
        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Funcionario func = new Funcionario(
                        rs.getString("nome_usuario"),
                        rs.getString("senha"));
                funcionario.add(func);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar funcionários: " + e.getMessage());
        }
        return funcionario;
    }

    public void alterar(Funcionario funcionario) {
        String sql = "UPDATE funcionario SET nome_usuario = ?, senha = ? WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, funcionario.getNome_usuario());
            stmt.setString(2, funcionario.getSenha());
            stmt.setInt(3, funcionario.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao alterar funcionário: " + e.getMessage());
        }
    }

    public void excluir(int id) {
        String sql = "DELETE FROM funcionario WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir funcionário: " + e.getMessage());
        }
    }
}
