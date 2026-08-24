package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Pedido {
    private int id;
    private Cliente cliente;
    private String formaPag;
    private double frete;
    private String observacao;
    private Date dataPedido;
    private TipoSaida tiposaida;
    private List<ItemPedido> itens;

    public Pedido (Cliente cliente, String formaPag, String observacao, Date dataPedido, TipoSaida tiposaida){
        this.cliente = cliente;
        this.formaPag = formaPag;
        this.frete = 9.00;
        this.observacao = observacao;
        this.dataPedido = new Date();
        this.tiposaida = tiposaida;
        this.itens = new ArrayList<>();
    }

    public int getId() { 
        return id; }

    public Cliente getCliente() { 
        return cliente; }
    public void setCliente(Cliente cliente) { 
        this.cliente = cliente; }

    public String getFormaPag() { 
        return formaPag; }
    public void setFormaPag(String formaPag) {
        this.formaPag = formaPag; }

    public double getFrete() { 
        return frete; }

    public Date getDataPedido() { 
        return dataPedido; }
    public void setDataPedido(Date dataPedido) { 
        this.dataPedido = dataPedido; }

    public TipoSaida getTipoDeSaida() { 
        return tiposaida; }
    public void setTipoDeSaida(TipoSaida tipoDeSaida) { 
        this.tiposaida = tipoDeSaida; }

    public List<ItemPedido> getItens() { 
        return itens; }
    public void setItens(List<ItemPedido> itens) { 
        this.itens = itens; }

    public void adicionarItem(ItemPedido item) {
        itens.add(item);}

    public void removerItem(ItemPedido item) {
        itens.remove(item);}
}

