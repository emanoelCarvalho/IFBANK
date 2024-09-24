# Sistema Bancário Simples - POO

Este é um projeto desenvolvido como parte da disciplina de **Programação Orientada a Objetos (POO)**, utilizando a linguagem **Java** e princípios fundamentais da orientação a objetos. O objetivo é implementar um sistema bancário simples, com funcionalidades básicas e integração a um banco de dados **MySQL** para o gerenciamento de dados.

## Funcionalidades

Este sistema bancário permite a execução das seguintes operações:

- **Adicionar Cliente**: Cadastro de novos clientes no sistema.
- **Adicionar Conta**: Criação de contas bancárias associadas aos clientes.
- **Realizar Saque**: Permite a retirada de saldo de uma conta bancária.
- **Realizar Depósito**: Permite o depósito de valores em uma conta bancária.
- **Transferências**: Realização de transferências entre contas de diferentes clientes.
- **Emissão de Extrato**: Geração de extrato bancário para uma conta específica em um determinado período.

## Tecnologias Utilizadas

- **Java**: Linguagem principal para a implementação do sistema.
- **MySQL**: Banco de dados utilizado para armazenar os registros dos clientes, contas e transações.
- **JDBC (Java Database Connectivity)**: Para conectar e interagir com o banco de dados MySQL.

## Estrutura do Projeto

O projeto é organizado com base nos conceitos da programação orientada a objetos:

- **Classes e Objetos**: O sistema é composto por classes que representam entidades como `Cliente`, `Conta`, `Transacao`, etc.
- **Encapsulamento**: Cada classe contém atributos e métodos relevantes, respeitando o princípio de encapsulamento.
- **Herança e Polimorfismo**: A estrutura permite flexibilidade para adicionar novos tipos de contas ou transações, aplicando herança e polimorfismo conforme necessário.
  
## Instalação e Execução

1. **Configuração do Banco de Dados MySQL**: 
   - Crie um banco de dados no MySQL.
   - Execute os scripts SQL fornecidos para criar as tabelas necessárias (clientes, contas, transações).

2. **Configuração do Projeto**:
   - Certifique-se de que o driver JDBC para MySQL esteja disponível no seu classpath.
   - Configure a conexão com o banco de dados no código.

3. **Execução do Sistema**:
   - Compile e execute o projeto.
   - Use as funcionalidades disponíveis para adicionar clientes, criar contas e realizar transações.

## Contato

Caso tenha dúvidas sobre o projeto ou queira saber mais, entre em contato pelo email: [hemanoel718@gmail.com].
