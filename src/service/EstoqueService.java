package service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import dao.IngredienteDAO;
import dao.ReceitaDAO;
import model.Ingrediente;
import model.ItemPedido;
import model.ItemReceita;
import model.Produto;
import model.Tamanho;

public class EstoqueService {

    private final ReceitaDAO receitaDAO = new ReceitaDAO();
    private final IngredienteDAO ingredienteDAO = new IngredienteDAO();

    // chamado de dentro do PedidoDAO.inserir(), na MESMA transação/conexão do pedido
    public void darBaixaPorItem(Connection conn, ItemPedido item) throws SQLException {
        if (item.isMeioAMeio()) {
            darBaixaPorProduto(conn, item.getProduto(), item.getTamanho(), item.getQuantidade(), 0.5);
            darBaixaPorProduto(conn, item.getSegundoSabor(), item.getTamanho(), item.getQuantidade(), 0.5);
        } else {
            darBaixaPorProduto(conn, item.getProduto(), item.getTamanho(), item.getQuantidade(), 1.0);
        }
    }

    // fracao = 1.0 pra pizza inteira, 0.5 pra cada metade de uma pizza meio a meio
    private void darBaixaPorProduto(Connection conn, Produto produto, Tamanho tamanho, int quantidadePedida, double fracao)
            throws SQLException {
        List<ItemReceita> receita = receitaDAO.listarPorProdutoETamanho(conn, produto.getId(), tamanho);
        for (ItemReceita ir : receita) {
            Ingrediente ingrediente = ir.getIngrediente();
            double consumo = ir.getQuantidadeNecessaria() * quantidadePedida * fracao;
            ingrediente.darBaixa(consumo); // já existe no Ingrediente.java, lança erro se ficar negativo
            ingredienteDAO.alterar(conn, ingrediente);
        }
    }
}