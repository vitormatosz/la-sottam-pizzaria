package model;

public class Produto {
    private int id;
    private String nome;
    private String categoria;
    private char tamanho;
    private String descricao;
    private double preco;
    private boolean disponivel;

    public Produto (String nome, String categoria, char tamanho, String descricao, double preco, boolean disponivel){
        this.nome = nome;
        this.categoria = categoria;
        this.tamanho = tamanho;
        this.descricao = descricao;
        this.preco = preco;
        this.disponivel = disponivel;
    }

    public int getId() {
        return id; 
    }

    public String getNome() {
        return nome; 
    }
    public void setNome(String nome) {
        this.nome = nome; 
    }

    public String getCategoria() {
        return categoria; 
    }
    public void setCategoria(String categoria) {
        this.categoria = categoria; 
    }

    public String getDescricao() {
        return descricao; 
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao; 
    }

    public char getTamanho() {
        return tamanho; 
    }
    public void setTamanho(char tamanho) {
        this.tamanho = tamanho; 
    }

    public double getPreco() {
        return preco; 
    }
    public void setPreco(double preco) {
        this.preco = preco; 
    }

    public boolean isDisponivel() {
        return disponivel; 
    }
    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel; 
    }


}
