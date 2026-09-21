package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectionFactory {

    private static final String SERVER_URL = "jdbc:mysql://localhost:3306/";
    private static final String DATABASE_URL = SERVER_URL + "lasottam";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    private static boolean bancoInicializado;

    public static synchronized Connection getConnection() {
        try {
            inicializarBanco();
            return DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao conectar no banco: " + e.getMessage());
        }
    }

    private static void inicializarBanco() throws SQLException {
        if (bancoInicializado) {
            return;
        }

        try (Connection conexaoServidor = DriverManager.getConnection(SERVER_URL, USER, PASSWORD);
                Statement statement = conexaoServidor.createStatement()) {
            statement.executeUpdate("CREATE DATABASE IF NOT EXISTS lasottam");
        }

        try (Connection conexaoBanco = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
                Statement statement = conexaoBanco.createStatement()) {
            statement.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS cliente (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        nome VARCHAR(100) NOT NULL,
                        numero_tel VARCHAR(20),
                        endereco TEXT
                    )
                    """);

            statement.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS funcionario (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        nome_usuario VARCHAR(100) NOT NULL UNIQUE,
                        senha VARCHAR(25) NOT NULL
                    )
                    """);

            statement.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS produto (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        nome VARCHAR(100) NOT NULL,
                        categoria VARCHAR(50),
                        descricao TEXT,
                        preco DECIMAL(10, 2) NOT NULL,
                        disponivel BOOLEAN DEFAULT TRUE
                    )
                    """);

            statement.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS ingrediente (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        nome VARCHAR(100) NOT NULL,
                        categoria VARCHAR(50),
                        unidade VARCHAR(20),
                        quantidade DECIMAL(10, 2) NOT NULL,
                        estoque_minimo DECIMAL(10, 2) NOT NULL
                    )
                    """);

            // NOVO: tabela receita — liga produto + tamanho a um ingrediente e quanto ele consome
            statement.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS receita (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        produto_id INT NOT NULL,
                        tamanho ENUM('PEQUENA','MEDIA','GRANDE') NOT NULL,
                        ingrediente_id INT NOT NULL,
                        quantidade_necessaria DECIMAL(10, 2) NOT NULL,
                        FOREIGN KEY (produto_id) REFERENCES produto(id),
                        FOREIGN KEY (ingrediente_id) REFERENCES ingrediente(id),
                        UNIQUE (produto_id, tamanho, ingrediente_id)
                    )
                    """);

            statement.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS pedido (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        cliente_id INT NOT NULL,
                        forma_pag VARCHAR(50),
                        frete DECIMAL(10, 2) DEFAULT 0.00,
                        observacao TEXT,
                        data_pedido DATETIME DEFAULT CURRENT_TIMESTAMP,
                        tipo_saida ENUM('ENTREGA', 'RETIRADA') NOT NULL,
                        FOREIGN KEY (cliente_id) REFERENCES cliente(id)
                    )
                    """);

            // item_pedido agora com tamanho e segundo_sabor_id (NULL se não for meio a meio)
            statement.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS item_pedido (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        pedido_id INT NOT NULL,
                        produto_id INT NOT NULL,
                        segundo_sabor_id INT NULL,
                        tamanho ENUM('PEQUENA','MEDIA','GRANDE') NOT NULL,
                        quantidade INT NOT NULL,
                        preco_unitario DECIMAL(10, 2) NOT NULL,
                        FOREIGN KEY (pedido_id) REFERENCES pedido(id) ON DELETE CASCADE,
                        FOREIGN KEY (produto_id) REFERENCES produto(id),
                        FOREIGN KEY (segundo_sabor_id) REFERENCES produto(id)
                    )
                    """);
        }

        bancoInicializado = true;
    }
}