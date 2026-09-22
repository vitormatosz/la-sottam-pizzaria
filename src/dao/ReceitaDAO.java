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

    public List<ItemReceita> listarPorProdutoETamanho(
            Connection conn,
            int produtoId,
            Tamanho tamanho) throws SQLException {

        List<ItemReceita> resultado = new ArrayList<>();

        String sql = "SELECT id, ingrediente_id, quantidade_necessaria " +
                     "FROM receita " +
                     "WHERE produto_id = ? AND tamanho = ?";

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
                    "Erro ao excluir item de receita: " + e.getMessage()
            );
        }
    }
}