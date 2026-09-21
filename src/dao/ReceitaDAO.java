package dao;

import java.sql.*;
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

    // usada pela tela de cadastro de receitas (produto já carregado, então não busca de novo)
    public List<ItemReceita> listarPorProdutoETamanho(int produtoId, Tamanho tamanho) {
        try (Connection conn = ConnectionFactory.getConnection()) {
            return listarPorProdutoETamanho(conn, produtoId, tamanho);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar receita: " + e.getMessage());
        }
    }

    // NOVO: versão que recebe a conexão de fora — usada pelo EstoqueService dentro
    // da MESMA transação do pedido, igual fizemos no IngredienteDAO.alterar(Connection, ...)
    public List<ItemReceita> listarPorProdutoETamanho(Connection conn, int produtoId, Tamanho tamanho) throws SQLException {
        List<ItemReceita> resultado = new ArrayList<>();
        String sql = "SELECT r.id, r.quantidade_necessaria, "
                + "i.id AS ingrediente_id, i.nome, i.categoria, i.unidade, i.quantidade, i.estoque_minimo "
                + "FROM receita r JOIN ingrediente i ON r.ingrediente_id = i.id "
                + "WHERE r.produto_id = ? AND r.tamanho = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, produtoId);
            stmt.setString(2, tamanho.name());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Ingrediente ingrediente = new Ingrediente(
                            rs.getString("nome"),
                            rs.getString("categoria"),
                            rs.getString("unidade"),
                            rs.getDouble("quantidade"),
                            rs.getDouble("estoque_minimo"));
                    ingrediente.setId(rs.getInt("ingrediente_id"));

                    // produto fica null de propósito: pra dar baixa de estoque só
                    // precisamos do ingrediente e da quantidade, não do produto inteiro de novo
                    ItemReceita item = new ItemReceita(null, tamanho, ingrediente, rs.getDouble("quantidade_necessaria"));
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
            throw new RuntimeException("Erro ao excluir item de receita: " + e.getMessage());
        }
    }
}