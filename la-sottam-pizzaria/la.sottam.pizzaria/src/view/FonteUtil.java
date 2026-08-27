import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class FonteUtil {

    public static Font carregarFonte(String nomeArquivo, float tamanho) {
        try {
            // Tenta carregar pelo classpath (pasta src)
            InputStream is = FonteUtil.class.getResourceAsStream("/" + nomeArquivo);
            
            if (is != null) {
                Font fonte = Font.createFont(Font.TRUETYPE_FONT, is);
                return fonte.deriveFont(tamanho);
            }

            // Fallback: Tenta carregar pelo caminho direto do arquivo
            File arquivo = new File("src/view/fontes/" + nomeArquivo);
            if (arquivo.exists()) {
                Font fonte = Font.createFont(Font.TRUETYPE_FONT, arquivo);
                return fonte.deriveFont(tamanho);
            }

        } catch (FontFormatException | IOException e) {
            System.err.println("Erro ao carregar fonte: " + e.getMessage());
        }

        // Se falhar, usa a fonte padrao do sistema sem quebrar o programa
        return new Font("SansSerif", Font.BOLD, (int) tamanho);
    }
}