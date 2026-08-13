package model;

public class Cliente {
    private int id;
    private String nome;
    private String numeroTel;
    private String endereco;

    public Cliente (String nome, String numroTel, String endereco){
        this.nome = nome;
        this.numeroTel = numroTel;
        this.endereco = endereco;
    }

}
