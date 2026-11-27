# Relatório de Cobertura — Aula 11

> Medido com JaCoCo 0.8.11 sobre a suíte JUnit 5 (execução local pelo launcher do JUnit, não pelo `mvn`).
> Com o Maven configurado, o relatório HTML sai em `back/target/site/jacoco/index.html` após `mvn clean test`.

## 1. Antes × depois

| Momento | Testes | Linhas | Ramos |
|---|---|---|---|
| Início da aula (só `AppTest`, com `assertTrue(true)`) | 1 | 0 / 387 = **0,0 %** | 0 / 136 = **0,0 %** |
| Fim da aula (unitários + integração) | 36 | 240 / 387 = **62,0 %** | 107 / 136 = **78,7 %** |

Meta do projeto: 70 % de linhas. **Não atingida no total (62,0 %)**, por causa do pacote `utils` (ver seção 3).
Sem a classe `App` (um `main` de demonstração, só imprime), o total seria 77,4 % de linhas — número informativo,
**não** usado como resultado oficial (a exclusão está comentada no `pom.xml`, decisão da equipe).

## 2. Por pacote

| Pacote | Linhas | Ramos |
|---|---|---|
| `modelo` | 169 / 195 = **87 %** | 75 / 86 = 87 % |
| `modelo.pagamento` | 67 / 72 = **93 %** | 30 / 32 = 94 % |
| `utils` | 4 / 120 = **3 %** | 2 / 18 = 11 % |

## 3. Por classe (as mais e as menos cobertas)

| Classe | Linhas | Ramos | Comentário |
|---|---|---|---|
| `Dinheiro`, `Pessoa`, `ProcessadorPagamento` | 100 % | 90–100 % | Regras pequenas e todas exercitadas. |
| `Pedido` | 96 % | 92 % | Coração do fluxo: coberta pelos testes de integração. |
| `Cliente` | 54 % | 83 % | Vermelho = getters/setters de endereço (rua, bairro, cidade...) sem regra. |
| `PedidosUtils` | 9 % | 20 % | Ver abaixo. |
| `App` | 0 % | 0 % | `main` de demonstração. |

### As linhas vermelhas contêm regra de negócio?

- **`App` (0 %)** — Não. É a demonstração das aulas anteriores (só imprime). Sem teste, de propósito.
- **`Cliente` (endereço)** — Não. Atributos sem validação; teste seria só para subir a porcentagem.
- **`PedidosUtils`** — Em parte. `calcularSubtotal` tem regra e foi testado. Já `calcularFrete()` **resiste a ser testado**:
  não recebe parâmetros (peso fixo no código), sorteia o número do pedido e imprime tudo em vez de devolver valor.
  É excesso de responsabilidade (cálculo + sorteio + formatação + impressão numa classe só). Fica como ponto
  para discussão: separar em métodos que recebem dados e devolvem resultado tornaria cada regra testável.

## 4. O que os testes de integração revelaram

Na primeira execução, **6 testes falharam com o mesmo erro**: `expected: <3> but was: <5>`.
O pedido era pago e o **estoque nunca baixava**. Nenhum teste unitário teria pego: cada classe fazia o que prometia
sozinha. A regra simplesmente nunca tinha sido decidida. Decisão tomada e registrada no código e no README:

- Adicionar item ao pedido **não reserva** estoque; o estoque **baixa quando o pagamento é aprovado**.
- Como o estoque pode acabar entre adicionar e pagar (dois pedidos disputando o mesmo produto), o `pagar`
  **confere o estoque antes de cobrar** e recusa com exceção, sem cobrar nada.
- Pagamento recusado (`false`): pedido continua ABERTO, estoque intacto, sem comprovante.

## 5. Suíte rápida

36 testes em ~6 s no total (inclui subir a JVM e o agente do JaCoCo). Nenhum teste usa banco, rede ou espera de tempo.

## 6. Parte do sistema em que a equipe menos confia hoje

Todo o estado vive em memória: dois pedidos disputando estoque só se comportam bem porque o programa tem uma única
thread e nenhum banco. Quando a Aula 12 trouxer persistência, a regra "estoque baixa no pagamento" precisará de
transação — e os testes de integração desta aula são justamente o que vai avisar se ela quebrar.
