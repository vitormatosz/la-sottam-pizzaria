package model;

public enum Tamanho {
    PEQUENA(5.0),
    MEDIA(10.0),
    GRANDE(15.0);

    private final double acrescimo;

    Tamanho(double acrescimo) {
        this.acrescimo = acrescimo;
    }

    public double getAcrescimo() {
        return acrescimo;
    }
}