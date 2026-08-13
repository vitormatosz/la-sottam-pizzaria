package model;

import java.util.Date;
import java.util.List;

public class ItemPedido {
    private int id;
    private Pedido pedido;
    private Produto produto;
    private int quantidade;
    private double precoUnitario;

    public ItemPedido (Pedido pedido, Produto produto, int quantidade, double precoUnitario){
        this.pedido = pedido;
        this.produto = produto;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
    }

}
