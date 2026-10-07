# Sistema de Gestão de Pedidos — E-commerce

> Atividade desafiadora da Unidade Curricular **Desenvolvimento Back-end**
> Curso Superior de Tecnologia em Análise e Desenvolvimento de Sistemas — Turma CSTADS601

## Equipe / Squad

| Nome | Função |
|---|---|
|Daniel Dagnoni | Proce. pagamentos |
|Felipe Fernandes Macari | Ger. /Cliente/Pedidos |
|João Guilherme Araujo nunes| Func. Cadastro/Produtos|

## Descrição do desafio

Nossa equipe recebeu um trabalho para fazer um sistema de gestão de pedidos para um e-commerce, comteplando cadastro de produtos, clientes, pedidos e processamento de pagamentos

## Funcionalidades previstas

- [ ] Cadastro e gerenciamento de produtos
- [ ] Cadastro e gerenciamento de clientes
- [ ] Criação e gerenciamento de pedidos
- [ ] Processamento de pagamentos (cartão, boleto, Pix)
- [ ] Testes automatizados (unitários e de integração)
- [ ] Pipeline de CI/CD
- [ ] API REST para consumo por um front-end

## Tecnologias

- Java
- Maven
- Git / GitHub
- _(demais tecnologias serão adicionadas ao longo do semestre: JUnit, Spring Boot, banco de
  dados, GitHub Actions...)_

## Estrutura de pastas

```
ecommerce-pedidos-fd/
├── back
│    ├── src/
│    │    ├── main/
│    │    │   └── java/
│    │    │       └── ecommerce-pedidos-fd
│    │    │           └── git/   
│    │    │               └──App.jv
│    │    │       
│    │    │       
│    │    └── test/
│    │        └── java/
│    │            ├──ecommerce-pedidos-fd
│    │            │   └── git/
│    │            │        └──AppTest.jv
│    │            │
│    │            │
│    │            │
│                 └──procesamento-pagamento/
│
│    ├── .mvm/
│    │    └── jvm.config
│    │    └── maven.config
│    └── pom.xml
│
├── front
│   └──README.md
├── README.md
└── .gitignore
```

## Como rodar o projeto

_(Preencher a partir das próximas aulas, conforme o projeto evoluir.)_

## Roadmap do projeto (por aula)

| Aula | Entrega |
|---|---|
| 01 | Repositório criado, estruturado, com README e commit inicial |
| 02 | Fluxo de branches e primeiro Pull Request revisado |
| 03 | Classe utilitária (Utils) do domínio |
| 04 | Classes de domínio inicial (Produto, Cliente, Pedido, ItemPedido) |
| 05 | Encapsulamento e abstração aplicados |
| 06 | Hierarquia de formas de pagamento (herança) |
| 07 | Relacionamentos entre classes do domínio |
| 08 | Módulo de pagamento polimórfico |
| 09 | Tratamento de exceções |
| 10 | Suíte de testes unitários |
| 11 | Suíte de testes de integração + relatório de cobertura |
| 12 | Persistência: conexão, Create e Read |
| 13 | Persistência: Update, Delete e padrão DAO/Repository |
| 14 | Migração para Spring Boot |
| 15 | API REST + pipeline CI/CD |
| 16 | Entrega final, documentação e apresentação |

## Combinado da equipe (ética e convivência)

1. Comprometimento com  a tarefa designada, caso não seja possivel a entrega  desta o responsável deve comunicar os demais integrantes do grupo.
2. Caso haja dificuldade na elaboração ou desenvolvimento da tarefa requisitar auxílio dos integrantes do grupo.
3. É de suma importância a responsabilidade de cumprir a entrega das tarefas e a realização das funções respeitando o cronograma elaborado, mudanças no cronograma devem serem discutidas pelo grupo.
4. A comunicação é  importante, portanto cada um é responsável pela atenção e o uso de meios de informações para comunicação por isso se deve ficar atento as notificações.
5. Quanto a responsabilidade do commit, esta ação é de inteira responsabilidade dos integrantes do grupo, observando o consenso e a disponibilidade. Sendo indicado a responsabilidade umintegrante cada aula.


## Licença

Projeto acadêmico — Faculdade de Tecnologia SENAI "Antonio Adolpho Lobbe".
