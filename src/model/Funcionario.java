package model;

public class Funcionario {
    private int id;
    private String nome_usuario;
    private String senha;

    public Funcionario(String nome_usuario, String senha){
        this.nome_usuario = nome_usuario;
        this.senha = senha;
    }

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

    public String getNome_usuario() {return nome_usuario;}
    public void setNome_usuario(String nome_usuario) {this.nome_usuario = nome_usuario;}

    public String getSenha() {return senha;}
    public void setSenha(String senha) {this.senha = senha;}

    }
