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

    public int getId() { 
        return id; 
    }

    public Pedido getPedido() { 
        return pedido; 
    }
    public void setPedido(Pedido pedido) { 
        this.pedido = pedido; 
    }

    public Produto getProduto() { 
        return produto; 
    }
    public void setProduto(Produto produto) { 
        this.produto = produto; 
    }

    public int getQuantidade() { 
        return quantidade; 
    }
    public void setQuantidade(int quantidade) { 
        this.quantidade = quantidade; 
    }

    public double getPrecoUnitario() { 
        return precoUnitario; 
    }
    public void setPrecoUnitario(double precoUnitario) { 
        this.precoUnitario = precoUnitario; 
    }

    public double calcularSubtotal() {
        return quantidade * precoUnitario;
    }

}
