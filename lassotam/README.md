## Getting Started

Welcome to the VS Code Java world. Here is a guideline to help you get started to write Java code in Visual Studio Code.

## Folder Structure

The workspace contains two folders by default, where:

- `src`: the folder to maintain sources
- `lib`: the folder to maintain dependencies

Meanwhile, the compiled output files will be generated in the `bin` folder by default.

> If you want to customize the folder structure, open `.vscode/settings.json` and update the related settings there.

## Dependency Management

The `JAVA PROJECTS` view allows you to manage your dependencies. More details can be found [here](https://github.com/microsoft/vscode-java-dependency#manage-dependencies).

cliente
├── id (PK)
├── nome
├── numero_tel
└── endereco

produto
├── id (PK)
├── nome
├── categoria
├── descricao
├── tamanho
├── preco
└── disponivel

ingrediente
├── id (PK)
├── nome
├── categoria
├── unidade
├── quantidade
└── estoque_minimo

pedido
├── id (PK)
├── cliente_id (FK → cliente.id)
├── forma_pag
├── frete
├── data_pedido
└── tipo_saida   (ENUM('entrega','retirada'))

item_pedido
├── id (PK)
├── pedido_id (FK → pedido.id, ON DELETE CASCADE)
├── produto_id (FK → produto.id)
├── quantidade
└── preco_unitario
