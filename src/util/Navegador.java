package util;

import javax.swing.*;
import java.awt.*;

public class Navegador {
    private final CardLayout cardLayout;
    private final JPanel painelContainer;

    public Navegador(CardLayout cardLayout, JPanel painelContainer) {
        this.cardLayout = cardLayout;
        this.painelContainer = painelContainer;
    }

    public void irPara(String nomeTela) {
        cardLayout.show(painelContainer, nomeTela);
    }
}