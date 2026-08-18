package model;

public class Cliente {
    private int id;
    private String nome;
    private String numeroTel;
    private String endereco;

    public Cliente (String nome, String numeroTel, String endereco){
        this.nome = nome;
        this.numeroTel = numeroTel;
        this.endereco = endereco;
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

    public String getNumeroTel() {
        return numeroTel;
    }
    public void setNumeroTel(String numeroTel) {
        this.numeroTel = numeroTel;
    }

    public String getEndereco() {
        return endereco;
    }
    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }
}
