CREATE DATABASE lasottam;

USE lasottam;

CREATE TABLE cliente (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    numero_tel VARCHAR(20),
    endereco TEXT
);

CREATE TABLE funcionario (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome_usuario VARCHAR(100) NOT NULL UNIQUE,
    senha VARCHAR(25) NOT NULL
);

CREATE TABLE produto (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    categoria VARCHAR(50),
    tamanho VARCHAR(20),
    descricao TEXT,
    preco DECIMAL(10, 2) NOT NULL,
    disponivel BOOLEAN DEFAULT TRUE
);

CREATE TABLE ingrediente (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    categoria VARCHAR(50),
    unidade VARCHAR(20),
    quantidade DECIMAL(10, 2) NOT NULL,
    estoque_minimo DECIMAL(10, 2) NOT NULL
);

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

CREATE TABLE IF NOT EXISTS pedido (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cliente_id INT NOT NULL,
    forma_pag VARCHAR(50),
    frete DECIMAL(10, 2) DEFAULT 0.00,
    observacao TEXT,
    data_pedido DATETIME DEFAULT CURRENT_TIMESTAMP,
    tipo_saida ENUM('ENTREGA', 'RETIRADA') NOT NULL,
    FOREIGN KEY (cliente_id) REFERENCES cliente(id)
);

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
);

INSERT INTO produto (nome, categoria, descricao, preco, disponivel) VALUES

('Pizza Calabresa', 'Pizza', 'Pizza de calabresa, cebola e muçarela', 60.00, TRUE),
('Pizza Mussarela', 'Pizza', 'Pizza de muçarela, tomate e orégano', 60.00, TRUE),
('Pizza Frango com Catupiry', 'Pizza', 'Pizza de frango desfiado com catupiry', 60.00, TRUE),
('Pizza Portuguesa', 'Pizza', 'Pizza com presunto, ovo, cebola, tomate e muçarela', 60.00, TRUE),
('Pizza Quatro Queijos', 'Pizza', 'Pizza com muçarela, provolone, parmesão e catupiry', 60.00, TRUE),

('Esfiha de Carne', 'Esfiha', 'Esfiha aberta recheada com carne temperada', 8.00, TRUE),
('Esfiha de Queijo', 'Esfiha', 'Esfiha aberta recheada com muçarela', 8.00, TRUE),
('Esfiha de Frango', 'Esfiha', 'Esfiha aberta recheada com frango desfiado', 8.00, TRUE),
('Esfiha de Calabresa', 'Esfiha', 'Esfiha aberta recheada com calabresa e muçarela', 8.00, TRUE),
('Esfiha de Peperoni', 'Esfiha', 'Esfiha recheada com peperoni', 8.00, TRUE),

('Coca-Cola', 'Bebida', 'Coca-Cola 2 litros', 12.00, TRUE),
('Guaraná Antarctica', 'Bebida', 'Guaraná Antarctica 2 litros', 12.00, TRUE),
('Fanta Laranja', 'Bebida', 'Fanta Laranja 2 litros', 12.00, TRUE),
('Sprite', 'Bebida', 'Sprite 2 litros', 12.00, TRUE),
('Suco de Laranja', 'Bebida', 'Suco de laranja 1 litro', 12.00, TRUE);