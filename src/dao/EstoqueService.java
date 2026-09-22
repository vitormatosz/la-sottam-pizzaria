package dao; 

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import model.Ingrediente;
import model.ItemPedido;
import model.ItemReceita;
import model.Produto;
import model.Tamanho;

public class EstoqueService {

    private ReceitaDAO receitaDAO = new ReceitaDAO();
    private IngredienteDAO ingredienteDAO = new IngredienteDAO();

    public void darBaixaPorItem(Connection conn, ItemPedido item) throws SQLException {
        if (item.isMeioAMeio()) {
            darBaixaPorProduto(conn, item.getProduto(), item.getTamanho(), item.getQuantidade(), 0.5);
            darBaixaPorProduto(conn, item.getSegundoSabor(), item.getTamanho(), item.getQuantidade(), 0.5);
        } else {
            darBaixaPorProduto(conn, item.getProduto(), item.getTamanho(), item.getQuantidade(), 1.0);
        }
    }

    private void darBaixaPorProduto(Connection conn, Produto produto, Tamanho tamanho, int quantidadePedida, double fracao) throws SQLException {
        List<ItemReceita> receita = receitaDAO.listarPorProdutoETamanho(conn, produto.getId(), tamanho);
        for (ItemReceita ir : receita) {
            Ingrediente ingrediente = ir.getIngrediente();
            double consumo = ir.getQuantidadeNecessaria() * quantidadePedida * fracao;
            ingrediente.darBaixa(consumo);
            ingredienteDAO.alterar(conn, ingrediente);
        }
    }
}