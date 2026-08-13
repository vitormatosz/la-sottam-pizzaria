import javax.swing.*;
import java.awt.*;

public class teste {
    public void calcDon() {
        JFrame janela = new JFrame("O 'Filtro de Streamer' (Calculo de Donates/Subs)");
        janela.setSize(1400, 1000);
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        janela.setLocationRelativeTo(null); // Centraliza a janela na tela

        JPanel painel = new JPanel(new GridLayout(10, 1, 5, 5));
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titulo = new JLabel();
        titulo.setText("Valor da Compra");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("", Font.BOLD, 15));

        painel.setBackground(new Color(116, 67, 151));

        painel.add(titulo);

        janela.add(painel);

        janela.setVisible(true);
    }
}