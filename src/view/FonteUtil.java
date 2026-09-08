package view;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class FonteUtil {

    public static Font carregarFonte(String nomeArquivo, float tamanho) {
        try {
            // 1. Tenta pelo Classpath (ex: pasta resources/fontes/ ou src/view/fontes/)
            String caminhoResource = "/view/fontes/" + nomeArquivo;
            InputStream is = FonteUtil.class.getResourceAsStream(caminhoResource);
            
            if (is != null) {
                Font fonte = Font.createFont(Font.TRUETYPE_FONT, is);
                return fonte.deriveFont(tamanho);
            }

            // 2. Fallback: Tenta pelo sistema de arquivos local
            File arquivo = new File("src/view/fontes/" + nomeArquivo);
            if (arquivo.exists()) {
                Font fonte = Font.createFont(Font.TRUETYPE_FONT, arquivo);
                return fonte.deriveFont(tamanho);
            }

        } catch (FontFormatException | IOException e) {
            System.err.println("Erro ao carregar fonte (" + nomeArquivo + "): " + e.getMessage());
        }

        // Fallback do sistema caso o arquivo não seja localizado
        return new Font("SansSerif", Font.BOLD, (int) tamanho);
    }
}