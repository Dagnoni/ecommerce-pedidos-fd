# Verificação dos checklists (A08 / A09 / A10)

Legenda: ✅ atendido no código · ⚠ decisão ou ação da equipe · ▶ rodar `mvn test` para confirmar

## Aula 08 — Polimorfismo
| Bloco | Status |
|---|---|
| Interface `ProcessadorPagamento` (3 métodos, sem estado, nomes de "o quê") | ✅ |
| Implementações (Pix, Cartão, Boleto + Dinheiro) com `@Override` e regra própria | ✅ |
| Critério `false` × exceção igual em todas as formas | ✅ definido em `MAPA_DE_EXCECOES.md` (⚠ equipe validar) |
| `Pedido.pagar(ProcessadorPagamento)` sem `instanceof`, `String` ou cast | ✅ |
| Sobrecarga: versão curta delega à completa | ✅ corrigido (`FormaPagamento.processar()`) |
| Aberto/fechado: `Dinheiro` como forma extra, sem alterar o `Pedido` | ✅ ⚠ registrar quais arquivos mudaram (a refatoração tocou `FormaPagamento` e as 4 formas) |
| Testes funcionais da tabela | ✅ `PedidoTest` ▶ |
| Versionamento: branch própria, commits pequenos, PR, diagrama UML | ⚠ ver `GIT_PASSO_A_PASSO.md`; o diagrama é com vocês |

## Aula 09 — Exceções
| Item | Status |
|---|---|
| Hierarquia sob `ECommerceException`, causa preservada | ✅ |
| Mapa de Exceções | ⚠ rascunho em `MAPA_DE_EXCECOES.md` |
| `PagamentoRecusadoException` sem uso | ⚠ decidir |
| Sem `catch` vazio / `printStackTrace` | ✅ nenhum encontrado nas fontes |

## Aula 10 — Testes
| Item | Status |
|---|---|
| `junit-jupiter` + surefire no `pom.xml` | ✅ |
| Um comportamento por teste, nome `deve...Quando...`, AAA | ✅ nos testes novos; ⚠ `PedidosTest`/`PagamentoTest` antigos não foram reescritos |
| Teste placeholder (`AppTest`) e `Test.txt` | ✅ removidos |
| Testes dependentes da data de hoje | ⚠ `PedidosTest`/`PagamentoTest` ainda usam `LocalDate.now()` |
