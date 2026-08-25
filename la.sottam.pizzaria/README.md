# 🍕 La Sottam Pizzaria

## Sistema de Gestão de Pedidos

Projeto desenvolvido para a disciplina de **Desenvolvimento de Sistemas (DS)** — **2° DSA**.

O sistema tem como objetivo auxiliar no gerenciamento de uma pizzaria, centralizando o cadastro de clientes, produtos, ingredientes e pedidos em uma aplicação Java integrada a um banco de dados MySQL.

---

## 👥 Participantes

- **Yago Costa** — UML e documentação
- **Samira Toledo** — Java e JDBC
- **Vitor Matos** — Java Swing e CRUD
- **Pedro Henrique** — Banco de Dados e SQL

---

## 📋 Sobre o Projeto

O **La Sottam Pizzaria** é um sistema de gestão de pedidos desenvolvido para substituir o controle manual realizado por meio de cadernos e planilhas.

A aplicação busca centralizar e facilitar o gerenciamento de:

- Clientes
- Produtos
- Ingredientes
- Estoque
- Pedidos
- Itens dos pedidos

O sistema também permite registrar pedidos para **entrega** ou **retirada no balcão**, além de armazenar informações como forma de pagamento, frete e data do pedido.

---

## 🎯 Objetivos

O projeto tem como principais objetivos:

- Desenvolver uma aplicação Java integrada a um banco de dados MySQL
- Aplicar conceitos de Programação Orientada a Objetos
- Utilizar JDBC para comunicação entre Java e MySQL
- Implementar operações CRUD
- Desenvolver uma interface gráfica utilizando Java Swing
- Criar uma modelagem de banco de dados relacional
- Aplicar conceitos de UML na documentação do sistema

---

## 📌 Escopo do Sistema

### Funcionalidades contempladas

- Cadastro de clientes
- Consulta de clientes
- Alteração de clientes
- Exclusão de clientes
- Cadastro e manutenção de produtos
- Controle de disponibilidade dos produtos
- Cadastro de ingredientes
- Controle de estoque
- Definição de estoque mínimo
- Registro de pedidos
- Registro dos itens de cada pedido
- Escolha da forma de pagamento
- Definição do tipo de saída:
  - Entrega
  - Retirada
- Cálculo de subtotal e total do pedido

### 🚧 Funcionalidades futuras

As seguintes funcionalidades não fazem parte da primeira versão do projeto, mas podem ser implementadas futuramente:

- Pagamento online integrado
- Aplicativo mobile
- Emissão de nota fiscal
- Sistema de autenticação de usuários
- Relatórios de vendas
- Controle de funcionários
- Dashboard administrativo

---

# 🗄️ Banco de Dados

O sistema utiliza **MySQL** como banco de dados relacional.

A estrutura foi planejada para armazenar as principais informações necessárias para o funcionamento da pizzaria.

## Entidades

### Cliente

```text
funcionario
├── id (PK)
├── nome_usuario
└── senha

cliente
├── id (PK)
├── nome
├── numero_tel
└── endereco

Produto
produto
├── id (PK)
├── nome
├── categoria
├── descricao
├── tamanho
├── preco
└── disponivel

Ingrediente
ingrediente
├── id (PK)
├── nome
├── categoria
├── unidade
├── quantidade
└── estoque_minimo

Pedido
pedido
├── id (PK)
├── cliente_id (FK → cliente.id)
├── forma_pag
├── frete
├── data_pedido
└── tipo_saida


O campo tipo_saida possui os seguintes valores:

ENTREGA
RETIRADA

Item do Pedido
item_pedido
├── id (PK)
├── pedido_id (FK → pedido.id)
├── produto_id (FK → produto.id)
├── quantidade
└── preco_unitario


A relação entre pedido e item_pedido utiliza ON DELETE CASCADE, fazendo com que os itens sejam removidos automaticamente quando o pedido correspondente for excluído.

🔗 Relacionamentos

O banco de dados possui os seguintes relacionamentos principais:

CLIENTE
   │
   │ 1
   │
   │ 0..*
   ▼
PEDIDO
   │
   │ 1
   │
   │ 1..*
   ▼
ITEM_PEDIDO
   │
   │ *
   │
   │ 1
   ▼
PRODUTO

Relacionamentos
Um Cliente pode possuir vários Pedidos
Cada Pedido pertence a um Cliente
Um Pedido possui um ou mais Itens
Cada ItemPedido pertence a um Pedido
Um Produto pode aparecer em vários ItensPedido
Cada ItemPedido referencia um único Produto
📐 Diagrama de Classes UML

O sistema segue uma estrutura orientada a objetos composta principalmente pelas seguintes classes:

Cliente
Produto
Ingrediente
Pedido
ItemPedido
TipoSaida

O enum TipoSaida representa as opções:

ENTREGA
RETIRADA


As classes possuem atributos privados e métodos responsáveis pelo acesso e manipulação dos dados.

Principais conceitos utilizados
Encapsulamento
Classes e objetos
Construtores
Getters e setters
Associações
Composição
Enumerações
Regras de negócio
Exemplos de métodos
calcularTotal()
precisaReposicao()
validarDadosCadastro()

💻 Tecnologias Utilizadas
☕ Java
🖥️ Java Swing
🔌 JDBC
🗄️ MySQL
📐 UML
💻 Visual Studio Code
📁 Estrutura do Projeto

A estrutura do projeto Java foi planejada da seguinte maneira:

la-sottam-pizzaria/
│
├── src/
│   ├── model/
│   ├── dao/
│   ├── view/
│   └── connection/
│
├── lib/
│   └── dependencias/
│
├── bin/
│   └── arquivos compilados/
│
├── sql/
│   └── banco.sql
│
├── docs/
│   ├── diagrama-classes/
│   └── documentacao/
│
└── README.md

Diretórios
Diretório	Função
src	Código-fonte Java
model	Classes de modelo
dao	Acesso e operações no banco
view	Interfaces gráficas
connection	Conexão com o MySQL
lib	Dependências do projeto
bin	Arquivos compilados
sql	Scripts do banco de dados
docs	Documentação e diagramas
👨‍💻 Divisão das Responsabilidades
👤 Pedro Henrique — Banco de Dados + SQL

Responsável pela implementação do banco de dados.

Atividades
Levantar as entidades necessárias
Criar o modelo do banco
Definir tabelas
Definir PKs e FKs
Criar o script SQL (DDL)
Criar relacionamentos
Definir restrições
Testar o banco no MySQL
Entregar o script SQL para integração com Java
👤 Yago Costa — UML + Documentação

Responsável pela documentação e pelo Diagrama de Classes UML.

Atividades
Definir as classes do sistema
Definir atributos
Criar construtores
Definir métodos
Definir regras de negócio
Criar associações e multiplicidades
Definir composição e outras relações
Criar o Diagrama de Classes UML
Elaborar a documentação do projeto
👤 Samira Toledo — Java + JDBC

Responsável pela estrutura do projeto Java e pela comunicação com o banco de dados.

Atividades
Criar a estrutura do projeto Java
Organizar os pacotes
Criar a conexão com o MySQL
Configurar JDBC
Criar as classes de modelo
Criar os DAOs
Implementar operações:
INSERT
SELECT
UPDATE
DELETE
Testar a comunicação entre Java e MySQL
👤 Vitor Matos — Java Swing + CRUD

Responsável pela interface gráfica e pelo CRUD funcional.

CRUD principal

Para a primeira etapa do projeto, foi definido o CRUD de Clientes.

Funcionalidades
➕ Cadastrar cliente
🔎 Consultar clientes
✏️ Alterar cliente
🗑️ Excluir cliente
Componentes Swing
JFrame
JPanel
JTextField
JTable
JButton
Componentes de validação

A interface será integrada aos métodos DAO desenvolvidos na camada Java/JDBC.

🔄 Fluxo de Desenvolvimento

O desenvolvimento pode seguir o seguinte fluxo:

Pedro
Banco de Dados
      ↓
Yago
UML + Documentação
      ↓
Samira
Java + JDBC + DAO
      ↓
Vitor
Swing + CRUD
      ↓
Todos
Integração + Testes


Apesar dessa divisão, os integrantes devem trabalhar em conjunto para garantir que o banco, as classes Java e a interface estejam de acordo.

🚀 Primeira Etapa

Para a primeira etapa do projeto, o foco será desenvolver um CRUD completo de Clientes.

O CRUD deverá permitir:

Cadastrar
   ↓
Consultar
   ↓
Alterar
   ↓
Excluir


A ideia é garantir que essa funcionalidade esteja totalmente funcional antes da implementação dos demais módulos.

Posteriormente, o sistema poderá ser expandido para:

                 LA SOTTAM PIZZARIA
                         │
          ┌──────────────┼──────────────┐
          ↓              ↓              ↓
      Clientes       Produtos       Ingredientes
          │              │              │
          └──────────────┼──────────────┘
                         ↓
                      Pedidos
                         │
                         ↓
                   Itens do Pedido

📚 Documentação

A documentação do projeto contempla:

Solução Proposta
Escopo do Sistema
Script SQL (DDL)
Diagrama de Classes UML
Descrição das funcionalidades
Estrutura do projeto
🛠️ Como Executar
Pré-requisitos

Antes de executar o projeto, é necessário ter instalado:

Java JDK
MySQL
Visual Studio Code
Extensão Java para VS Code
Driver JDBC do MySQL
Banco de Dados
Abra o MySQL.
Execute o script localizado em:
sql/banco.sql

Verifique se o banco foi criado corretamente.
Configure as credenciais de acesso no projeto Java.
Executando o projeto

Abra o projeto no Visual Studio Code e execute a classe principal da aplicação.

📌 Status do Projeto

🚧 Em desenvolvimento

Etapa atual
 Definição do escopo
 Definição das entidades
 Divisão das responsabilidades
 Modelagem inicial do banco
 Definição das classes UML
 Implementação do banco de dados
 Implementação da conexão JDBC
 Implementação dos DAOs
 Desenvolvimento da interface Swing
 CRUD de Clientes
 Testes de integração
 Documentação final
🍕 La Sottam Pizzaria

Do pedido ao forno, tudo organizado em um só lugar.

Projeto acadêmico desenvolvido pela turma 2° DSA — Desenvolvimento de Sistemas.
