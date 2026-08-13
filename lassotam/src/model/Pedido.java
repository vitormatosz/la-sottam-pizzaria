package model;

import java.util.Date;
import java.util.List;

public class Pedido {
    private int id;
    private Cliente cliente;
    private String formaPag;
    private String observacao;
    private Date dataPedido;
    private TipoSaida tiposaida;
    private List<ItemPedido> itens;

    public Pedido (Cliente cliente, String formaPag, String observacao, Date dataPedido, TipoSaida tiposaida, List<ItemPedido> itens){
        this.cliente = cliente;
        this.formaPag = formaPag;
        this.observacao = observacao;
        this.dataPedido = dataPedido;
        this.tiposaida = tiposaida;
        this.itens = itens;
    }

}
