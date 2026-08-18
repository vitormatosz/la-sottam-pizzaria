# La Sott'Impizzaria — Sistema de Gestão de Pedidos

**Desenvolvimento de Sistemas (DS)** — **2° DSA**

## Participantes
- Yago Costa
- Samira Toledo
- Vitor Matos
- Pedro Henrique

## Sobre o Projeto
Sistema de gestão de pedidos desenvolvido para nossa pizzaria **La Sottam' Pizzaria**, com o objetivo de substituir o controle manual (caderno/planilhas) por uma aplicação que centraliza o cadastro de clientes, o catálogo de produtos, o controle de estoque de ingredientes e o registro de pedidos (entrega ou retirada no balcão).

## Escopo
**Contempla:**
- Cadastro, consulta, edição e exclusão de clientes
- Cadastro e manutenção do catálogo de produtos (categoria, tamanho, preço, disponibilidade)
- Controle de estoque de ingredientes (quantidade atual e estoque mínimo)
- Registro de pedidos (cliente, forma de pagamento, tipo de saída, frete)
- Registro dos itens de cada pedido, com cálculo automático de subtotal e total

**Não contempla (futuros upgrades):**
- Pagamento on-line integrado
- Aplicativo mobile dedicado
- Emissão fiscal de nota

## Banco de Dados
Modelagem relacional em **MySQL**, com script SQL (DDL) para criação das tabelas: `clientes`, `produtos`, `ingredientes`, `pedidos` e `itempedido`.

## Diagrama de Classes UML
Modelo orientado a objetos com as classes: **Cliente**, **Produto**, **Ingrediente**, **Pedido** e **ItemPedido**, além do enum **TipoSaida** (ENTREGA/RETIRADA).

- **Cliente 1 → 0..\* Pedido** — associação simples
- **Pedido 1 ◆→ 1..\* ItemPedido** — composição (itens dependem do pedido)
- **ItemPedido 0..\* → 1 Produto** — associação simples

Todos os atributos são privados, com getters/setters e métodos de regra de negócio (ex.: `calcularTotal()`, `precisaReposicao()`, `validarDadosCadastro()`).

## Estrutura da Documentação
- `Solução Proposta` — descrição da solução tecnológica
- `Escopo do Sistema` — funcionalidades contempladas e não contempladas
- `Script SQL (DDL)` — criação do banco MySQL
- `Diagrama de Classes UML` — classes, atributos, métodos e relacionamentos
