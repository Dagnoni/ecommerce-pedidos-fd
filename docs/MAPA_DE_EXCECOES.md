# Mapa de Exceções — ecommerce_pedidos (Aula 09)

> Rascunho baseado no código atual. Revisem com a equipe e completem os campos marcados com ⚠.

## Critério da equipe (um só para o projeto)

| Pergunta | Resposta |
|---|---|
| Checked quando? | Situação de **negócio prevista** que o chamador pode tratar (estoque acabou, cliente não existe, pagamento recusado). Herdam de `ECommerceException`. |
| Unchecked quando? | **Erro de uso** (argumento inválido → `IllegalArgumentException`; operação em estado errado → `IllegalStateException`). |
| Exceção-raiz do domínio | `ECommerceException` (`extends Exception`) |
| Quem trata | A camada de apresentação (`App`); o domínio só lança. |
| Se não sabemos tratar | Não capturamos; deixamos subir (nunca `catch` vazio nem `printStackTrace`). |

## Hierarquia

| Exceção | Herda de | Checked? | Quem lança |
|---|---|---|---|
| `ECommerceException` | `Exception` | sim | (raiz) |
| `EstoqueInsuficienteException` | `ECommerceException` | sim | `Produto.baixarEstoque` |
| `PedidoInvalidoException` | `ECommerceException` | sim | `Pedidos.adicionarItem` (encapsula a causa) |
| `PagamentoRecusadoException` | `ECommerceException` | sim | ⚠ ainda não lançada por ninguém |
| `ClienteNaoEncontradoException` | `ECommerceException` | sim | ⚠ confirmar o ponto de uso |
| `IllegalArgumentException` | linguagem | não | construtores/setters (valores inválidos) |
| `IllegalStateException` | linguagem | não | `Pedido.adicionarItem`, `Pedido.pagar` |

## Fluxo de pagamento (`Pedido.pagar`)

| # | Situação | Resultado | Teste |
|---|---|---|---|
| 1 | Processador `null` | `IllegalArgumentException` | `deveRecusarPagamentoQuandoProcessadorNulo` |
| 2 | Pedido sem itens | `IllegalStateException` | `deveRecusarPagamentoQuandoPedidoVazio` |
| 3 | Pedido já pago | `IllegalStateException` | `deveImpedirSegundaCobrancaQuandoJaPago` |
| 4 | Valor do pagamento ≠ total | `IllegalArgumentException` | `deveRecusarQuandoValorDiferenteDoTotal` |
| 5 | Boleto (aguarda compensação) | devolve `false`, pedido `ABERTO` | `deveManterAbertoQuandoBoletoAguardaCompensacao` |
| 6 | Dinheiro insuficiente | devolve `false`, pedido `ABERTO` | `deveManterAbertoQuandoDinheiroInsuficiente` |
| 7 | Cartão com parcelas > 12 | `IllegalArgumentException` na criação | `PagamentoTest` |

**Regra única para `false` × exceção:** `false` = pagamento **não aprovado agora** (pedido permanece `ABERTO`);
exceção = chamada **inválida** (argumento ou estado errado).

## Pendências para decidir (Aula 09 → 10)

1. ⚠ `Pedido.pagar` deve lançar `PagamentoRecusadoException` quando devolve `false`, ou mantemos o `false`? Registrar a decisão aqui.
2. ⚠ Manter `Pedidos` (modelo antigo, situação em `String`) ou removê-lo em favor de `Pedido`? Hoje coexistem.
3. ⚠ Mensagens: incluir valor recebido e esperado (ex.: "Forma de pagamento é obrigatória" não informa nada além disso).
