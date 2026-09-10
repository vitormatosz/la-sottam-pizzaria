package model;

public class Funcionario {
    private int id;
    private String nomeUsuario;
    private String senha;

    public Funcionario(String nomeUsuario, String senha){
        this.nomeUsuario = nomeUsuario;
        this.senha = senha;
    }

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

    public String getNomeUsuario() {return nomeUsuario;}
    public void setNomeUsuario(String nomeUsuario) {this.nomeUsuario = nomeUsuario;}

    public String getSenha() {return senha;}
    public void setSenha(String senha) {this.senha = senha;}

    }
