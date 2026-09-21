package model;

public class ItemReceita {
    private int id;
    private Produto produto;
    private Tamanho tamanho;
    private Ingrediente ingrediente;
    private double quantidadeNecessaria;

    public ItemReceita(Produto produto, Tamanho tamanho, Ingrediente ingrediente, double quantidadeNecessaria) {
        this.produto = produto;
        this.tamanho = tamanho;
        this.ingrediente = ingrediente;
        this.quantidadeNecessaria = quantidadeNecessaria;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Produto getProduto() { return produto; }
    public void setProduto(Produto produto) { this.produto = produto; }

    public Tamanho getTamanho() { return tamanho; }
    public void setTamanho(Tamanho tamanho) { this.tamanho = tamanho; }

    public Ingrediente getIngrediente() { return ingrediente; }
    public void setIngrediente(Ingrediente ingrediente) { this.ingrediente = ingrediente; }

    public double getQuantidadeNecessaria() { return quantidadeNecessaria; }
    public void setQuantidadeNecessaria(double quantidadeNecessaria) { this.quantidadeNecessaria = quantidadeNecessaria; }
}