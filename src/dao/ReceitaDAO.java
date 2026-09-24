package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import connection.ConnectionFactory;
import model.Ingrediente;
import model.ItemReceita;
import model.Produto;
import model.Tamanho;

public class ReceitaDAO {

    public void inserir(ItemReceita item) {
        String sql = "INSERT INTO receita (produto_id, tamanho, ingrediente_id, quantidade_necessaria) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, item.getProduto().getId());
            stmt.setString(2, item.getTamanho().name());
            stmt.setInt(3, item.getIngrediente().getId());
            stmt.setDouble(4, item.getQuantidadeNecessaria());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir item de receita: " + e.getMessage());
        }
    }

    public void alterar(ItemReceita item) {
        String sql = "UPDATE receita SET produto_id = ?, tamanho = ?, ingrediente_id = ?, quantidade_necessaria = ? WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, item.getProduto().getId());
            stmt.setString(2, item.getTamanho().name());
            stmt.setInt(3, item.getIngrediente().getId());
            stmt.setDouble(4, item.getQuantidadeNecessaria());
            stmt.setInt(5, item.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar item de receita: " + e.getMessage());
        }
    }

    public List<ItemReceita> listarTodos() {
        List<ItemReceita> resultado = new ArrayList<>();
        String sql = "SELECT id, produto_id, tamanho, ingrediente_id, quantidade_necessaria FROM receita";

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            ProdutoDAO produtoDAO = new ProdutoDAO();
            IngredienteDAO ingredienteDAO = new IngredienteDAO();

            while (rs.next()) {
                Produto produto = produtoDAO.buscarPorId(rs.getInt("produto_id"));
                Ingrediente ingrediente = ingredienteDAO.buscarPorId(rs.getInt("ingrediente_id"));
                Tamanho tamanho = Tamanho.valueOf(rs.getString("tamanho"));
                double quantidade = rs.getDouble("quantidade_necessaria");

                ItemReceita item = new ItemReceita(produto, tamanho, ingrediente, quantidade);
                item.setId(rs.getInt("id"));
                resultado.add(item);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar receita: " + e.getMessage());
        }
        return resultado;
    }

    public List<ItemReceita> listarPorProdutoETamanho(int produtoId, Tamanho tamanho) {
        List<ItemReceita> resultado = new ArrayList<>();
        String sql = "SELECT id, ingrediente_id, quantidade_necessaria FROM receita WHERE produto_id = ? AND tamanho = ?";
        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, produtoId);
            stmt.setString(2, tamanho.name());
            try (ResultSet rs = stmt.executeQuery()) {
                IngredienteDAO ingredienteDAO = new IngredienteDAO();
                while (rs.next()) {
                    Ingrediente ingrediente = ingredienteDAO.buscarPorId(rs.getInt("ingrediente_id"));
                    double quantidade = rs.getDouble("quantidade_necessaria");

                    ItemReceita item = new ItemReceita(null, tamanho, ingrediente, quantidade);
                    item.setId(rs.getInt("id"));
                    resultado.add(item);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar receita do produto: " + e.getMessage());
        }
        return resultado;
    }

    // Usada pelo EstoqueService dentro da transação do pedido (mantida como já
    // estava).
    public List<ItemReceita> listarPorProdutoETamanho(Connection conn, int produtoId, Tamanho tamanho)
            throws SQLException {
        List<ItemReceita> resultado = new ArrayList<>();
        String sql = "SELECT id, ingrediente_id, quantidade_necessaria FROM receita WHERE produto_id = ? AND tamanho = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, produtoId);
            stmt.setString(2, tamanho.name());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int ingredienteId = rs.getInt("ingrediente_id");
                    double quantidadeNecessaria = rs.getDouble("quantidade_necessaria");

                    Ingrediente ingrediente = new IngredienteDAO().buscarPorId(ingredienteId);
                    ItemReceita item = new ItemReceita(null, tamanho, ingrediente, quantidadeNecessaria);

                    item.setId(rs.getInt("id"));
                    resultado.add(item);
                }
            }
        }
        return resultado;
    }

    public void excluir(int id) {
        String sql = "DELETE FROM receita WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(
                    "Erro ao excluir item de receita: " + e.getMessage());
        }
    }
}