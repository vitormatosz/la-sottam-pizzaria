package model;

public class ItemPedido {
    private int id;
    private Pedido pedido;
    private Produto produto;
    private Produto segundoSabor;
    private Tamanho tamanho;
    private int quantidade;
    private double precoUnitario;

    public ItemPedido (Produto produto, Tamanho tamanho, int quantidade){
        this.produto = produto;
        this.tamanho = tamanho;
        this.quantidade = quantidade;
        this.precoUnitario = produto.getPreco() + tamanho.getAcrescimo();
    }

    public ItemPedido(Produto sabor1, Produto sabor2, Tamanho tamanho, int quantidade) {
        this.produto = sabor1;
        this.segundoSabor = sabor2;
        this.tamanho = tamanho;
        this.quantidade = quantidade;
        double precoProduto = (sabor1.getPreco() + sabor2.getPreco()) / 2;
        this.precoUnitario = precoProduto + tamanho.getAcrescimo();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Pedido getPedido() { return pedido; }
    public void setPedido(Pedido pedido) { this.pedido = pedido; }

    public Produto getProduto() { return produto; }
    public void setProduto(Produto produto) { this.produto = produto; }

    public boolean isMeioAMeio() { return segundoSabor != null; }
    public Produto getSegundoSabor() { return segundoSabor; }
    public void setSegundoSabor(Produto segundoSabor) { this.segundoSabor = segundoSabor; }

    public Tamanho getTamanho() { return tamanho; }
    public void setTamanho(Tamanho tamanho) { this.tamanho = tamanho; }

    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }

    public double getPrecoUnitario() { return precoUnitario; }
    public void setPrecoUnitario(double precoUnitario) { this.precoUnitario = precoUnitario; }

    public double calcularSubtotal() {return quantidade * precoUnitario;}
}
