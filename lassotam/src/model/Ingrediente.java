package model;

public class Ingrediente {
    private int id;
    private String nome;
    private String categoria;
    private String unidade;
    private double quantidade;
    private double estoqueMin;

    public Ingrediente (String nome, String categoria, String unidade, double quantidade, double estoqueMin){
        this.nome = nome;
        this.categoria = categoria;
        this.unidade = unidade;
        this.quantidade = quantidade;
        this.estoqueMin = estoqueMin;
    }
}
