Sistema de Ponto de Venda (PDV) para Supermercado
📝 Descrição do Projeto
Este projeto é um sistema de gestão para supermercados desenvolvido em Java, com foco nos princípios de Programação Orientada a Objetos (POO). Ele oferece uma solução de Ponto de Venda (PDV) funcional, com gestão de estoque e geração de relatórios gerenciais, utilizando uma interface gráfica construída com Java Swing e um banco de dados MySQL para persistência de dados.

Este projeto foi desenvolvido como um portfólio para demonstrar habilidades em desenvolvimento de software desktop, arquitetura em camadas e integração com bancos de dados.

🎓 Contexto Acadêmico
Este sistema foi originalmente desenvolvido como um projeto acadêmico para a disciplina de Programação Orientada a Objetos, ministrada pelo professor Norton Glasser, no curso de Análise e Desenvolvimento de Sistemas da FATEC Ipiranga.

O objetivo era aplicar os conceitos de POO na construção de uma aplicação desktop completa e funcional.

✨ Funcionalidades Principais
O sistema está organizado em módulos e oferece as seguintes funcionalidades:
- Ponto de Venda (PDV):
. Interface principal para registro de vendas.
. Busca de produtos por código de barras.
. Cálculo automático do total da compra.
. Seleção de formas de pagamento.
. Baixa automática do estoque ao finalizar a venda.

- Gestão de Estoque:
. Tela para cadastro de novos produtos.
. Tela para visualização de todo o estoque, com alertas de estoque baixo.
. Funcionalidade para ajuste manual da quantidade de produtos.

- Relatórios Gerenciais:
. Central de relatórios para visualização de dados estratégicos.
. Relatórios disponíveis: Vendas Diárias, Produtos Mais Vendidos, Estoque Baixo, Margem de Lucro e Fluxo de Caixa.

🛠️ Tecnologias Utilizadas
Linguagem: Java 17+
Interface Gráfica: Java Swing
Banco de Dados: MySQL 8.0+
Conectividade: JDBC (MySQL Connector/J)
IDE: IntelliJ IDEA

🚀 Como Executar o Projeto
Para executar este projeto no seu ambiente local, siga os passos abaixo:

- Pré-requisitos:
. Ter o Java JDK 17 ou superior instalado.
. Ter o MySQL instalado e em execução.
. Ter uma IDE Java como o IntelliJ IDEA ou Eclipse.

- Clone o Repositório:
```bash
git clone https://github.com/kenyamashida/sistema-supermercado-java.git 
```

- Configure o Banco de Dados:
. Crie um usuário e um banco de dados no seu MySQL.
. Execute os scripts localizados na pasta scripts_SQL (https://github.com/kenyamashida/sistema-supermercado-java/tree/main/scipts_SQL) na seguinte ordem:
. `DDL_CriarSupermercado.sql`: para criar toda a estrutura de tabelas.
. `DML_PopularSupermercado.sql`: para inserir dados de exemplo.
. Abra o arquivo `src/dao/ConexaoMySQL.java` e atualize as credenciais USER e PASSWORD com as suas.

- Configure a IDE (IntelliJ IDEA):
. Abra o projeto na IDE.
. Adicione o arquivo .jar do MySQL Connector/J (https://dev.mysql.com/downloads/connector/j/) às bibliotecas do projeto (`File > Project Structure > Libraries`).
. Localize a classe Main.java (ou view/TelaVenda.java), clique com o botão direito e selecione Run.

🏛️ Arquitetura
O projeto segue uma arquitetura em 3 camadas para separar as responsabilidades:
- Model: Classes que representam as entidades de negócio (ex: Produto, Venda). Não contêm lógica de acesso a dados ou de interface.
- View: Classes que constroem a interface gráfica (ex: `TelaVenda`, `TelaRelatorios`). São responsáveis pela interação com o utilizador.
- DAO (Data Access Object): Classes que encapsulam a comunicação com o banco de dados (ex: `ProdutoDAO`, `VendaDAO`).


📖 Metodologia e Referências
Ferramenta de IA Assistiva no Desenvolvimento
Parte do desenvolvimento deste projeto contou com o auxílio de um modelo de linguagem ampla (LLM) como ferramenta de programação em par (pair programming). A ferramenta foi utilizada para as seguintes finalidades metodológicas:
. Refatoração e Otimização: Sugestão de melhorias na legibilidade e estrutura do código.
. Depuração (Debugging): Análise de erros de compilação e execução para identificar causas prováveis e sugerir correções.
. A lógica de negócio, a arquitetura principal e a validação final de todo o código e documentação foram de responsabilidade do desenvolvedor.

Referência:
. Google. (2024). Gemini (Modelo de Linguagem Ampla). Acessado em Julho de 2024, de https://gemini.google.com
