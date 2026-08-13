import javax.swing.*;
import java.awt.*;
import java.io.InputStream;

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

        // Bloco de segurança para carregar a fonte externa sem travar o programa
        try {
            // Procura o arquivo .ttf dentro da pasta 'src' do seu projeto
            InputStream stream = getClass().getResourceAsStream("/Audiowide-Regular.ttf");
            
            if (stream == null) {
                throw new java.io.FileNotFoundException("Arquivo Audiowide-Regular.ttf nao foi encontrado no src!");
            }

            // Cria e registra a fonte no sistema do Java
            Font audiowideBase = Font.createFont(Font.TRUETYPE_FONT, stream);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(audiowideBase);

            // Aplica o estilo Negrito (BOLD) e o tamanho 15f (tem que ter o 'f')
            titulo.setFont(audiowideBase.deriveFont(Font.BOLD, 60f));

        } catch (Exception e) {
            // Se o arquivo sumir ou der erro, o Java usa essa fonte padrão como plano B
            System.out.println("Aviso: Nao foi possivel carregar a fonte Audiowide. Usando padrao.");
            titulo.setFont(new Font("SansSerif", Font.BOLD, 15));
            e.printStackTrace();
        }

        painel.setBackground(new Color(116, 67, 151));
        painel.add(titulo);
        janela.add(painel);
        janela.setVisible(true);
    }
}
