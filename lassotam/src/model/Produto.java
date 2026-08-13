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

}
