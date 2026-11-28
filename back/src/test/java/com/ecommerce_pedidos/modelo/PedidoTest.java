package com.ecommerce_pedidos.modelo;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import com.ecommerce_pedidos.modelo.pagamento.Pix;
import com.ecommerce_pedidos.modelo.pagamento.Dinheiro;

class PedidoTest {

    private Cliente cliente;
    private Produto notebook;
    private Produto mouse;
    private Pedido pedido;

    @BeforeEach
    void prepararCenario() {
        cliente = new Cliente(
            "Ana Souza", "12345678900", "ana@exemplo.com",
            null, null, null, null, null, null, null
        );

        notebook = new Produto(
            "NOTE-001", "Notebook",
            new BigDecimal("3000.00"), 5
        );

        mouse = new Produto(
            "MOU-001", "Mouse",
            new BigDecimal("50.00"), 10
        );

        pedido = new Pedido(cliente);
    }

    // ---------- Criação e situação ----------

    @Test
    @DisplayName("Deve criar pedido aberto e vazio quando há cliente")
    void deveCriarPedidoAbertoEVazioQuandoHaCliente() {
        assertAll(
            () -> assertEquals(
                SituacaoDoPedido.ABERTO, pedido.getSituacao()
            ),
            () -> assertSame(cliente, pedido.getCliente()),
            () -> assertTrue(pedido.getItens().isEmpty()),
            () -> assertEquals(LocalDate.now(), pedido.getData())
        );
    }

    @Test
    @DisplayName("Deve gerar número no formato PED-ANO-99999")
    void deveGerarNumeroNoFormatoEsperado() {
        assertTrue(
            pedido.getNumero().matches("PED-\\d{4}-\\d{5}")
        );
    }

    @Test
    @DisplayName("Deve recusar pedido quando não há cliente")
    void deveRecusarPedidoQuandoClienteNulo() {
        assertThrows(
            IllegalArgumentException.class,
            () -> new Pedido(null)
        );
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    @DisplayName("Deve recusar número de pedido inválido")
    void deveRecusarNumeroInvalido(String numero) {
        assertThrows(
            IllegalArgumentException.class,
            () -> new Pedido(numero, cliente)
        );
    }

    // ---------- Itens e total ----------

    @Test
    @DisplayName("Deve ter total zero quando o pedido não tem itens")
    void deveTerTotalZeroQuandoSemItens() {
        assertEquals(
            0, BigDecimal.ZERO.compareTo(pedido.calcularValorTotal())
        );
    }

    @Test
    @DisplayName("Deve somar o total corretamente com um item")
    void deveSomarTotalComUmItem() {
        pedido.adicionarItem(notebook, 1);

        assertEquals(
            0, new BigDecimal("3000.00")
                .compareTo(pedido.calcularValorTotal())
        );
    }

    @Test
    @DisplayName("Deve somar o total corretamente com dois itens")
    void deveSomarTotalComDoisItens() {
        pedido.adicionarItem(notebook, 2);
        pedido.adicionarItem(mouse, 3);

        assertEquals(
            0, new BigDecimal("6150.00")
                .compareTo(pedido.calcularValorTotal())
        );
    }

    @Test
    @DisplayName("Deve adicionar uma unidade por padrão")
    void deveAdicionarUmaUnidadePorPadrao() {
        pedido.adicionarItem(notebook);

        assertEquals(1, pedido.getItens().get(0).getQuantidade());
    }

    @Test
    @DisplayName("Deve preservar o estoque ao adicionar um produto")
    void devePreservarEstoqueAoAdicionarProduto() {
        pedido.adicionarItem(notebook, 2);

        assertEquals(5, notebook.getQuantidadeEmEstoque());
    }

    @Test
    @DisplayName("Deve recusar produto nulo")
    void deveRecusarProdutoNulo() {
        assertThrows(
            IllegalArgumentException.class,
            () -> pedido.adicionarItem(null, 1)
        );

        assertTrue(pedido.getItens().isEmpty());
    }

    @Test
    @DisplayName("Deve recusar produtos com o mesmo código")
    void deveRecusarProdutoDuplicado() {
        pedido.adicionarItem(notebook, 1);

        Produto outroNotebook = new Produto(
            "NOTE-001", "Notebook",
            new BigDecimal("3000.00"), 5
        );

        assertThrows(
            IllegalStateException.class,
            () -> pedido.adicionarItem(outroNotebook, 1)
        );

        assertEquals(1, pedido.getItens().size());
    }

    @Test
    @DisplayName("Deve proteger a lista de itens")
    void deveProtegerListaDeItens() {
        pedido.adicionarItem(notebook, 1);

        assertThrows(
            UnsupportedOperationException.class,
            () -> pedido.getItens().clear()
        );

        assertEquals(1, pedido.getItens().size());
    }

    // ---------- Validações e exceções ----------

    @Test
    @DisplayName("Deve recusar quando o estoque é insuficiente")
    void deveRecusarEstoqueInsuficiente() {
        IllegalStateException erro = assertThrows(
            IllegalStateException.class,
            () -> pedido.adicionarItem(notebook, 50)
        );

        assertAll(
            () -> assertTrue(erro.getMessage().contains("Notebook")),
            () -> assertEquals(5, notebook.getQuantidadeEmEstoque()),
            () -> assertTrue(pedido.getItens().isEmpty())
        );
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    @DisplayName("Deve recusar quantidade não positiva")
    void deveRecusarQuantidadeNaoPositiva(int quantidade) {
        assertThrows(
            IllegalArgumentException.class,
            () -> pedido.adicionarItem(notebook, quantidade)
        );

        assertAll(
            () -> assertEquals(5, notebook.getQuantidadeEmEstoque()),
            () -> assertTrue(pedido.getItens().isEmpty())
        );
    }

    // ---------- Pagamento ----------

    @Test
    @DisplayName("Deve alterar a situação para PAGO após aprovação")
    void deveAlterarSituacaoAposPagamento() {
        pedido.adicionarItem(notebook, 1);

        Pix pix = new Pix(
            pedido.calcularValorTotal(), "ana@exemplo.com"
        );

        assertTrue(pedido.pagar(pix));

        assertAll(
            () -> assertEquals(
                SituacaoDoPedido.PAGO, pedido.getSituacao()
            ),
            () -> assertSame(pix, pedido.getFormaPagamento()),
            () -> assertNotNull(pedido.getComprovante())
        );
    }

    @Test
    @DisplayName("Deve baixar o estoque após pagamento aprovado")
    void deveBaixarEstoqueAposPagamento() {
        pedido.adicionarItem(notebook, 2);

        Pix pix = new Pix(
            pedido.calcularValorTotal(), "ana@exemplo.com"
        );

        pedido.pagar(pix);

        assertEquals(3, notebook.getQuantidadeEmEstoque());
    }

    @Test
    @DisplayName("Deve preservar o pedido quando o pagamento é recusado")
    void devePreservarPedidoQuandoPagamentoRecusado() {
        pedido.adicionarItem(notebook, 1);

        Dinheiro dinheiro = new Dinheiro(new BigDecimal("100.00"));

        assertFalse(pedido.pagar(dinheiro));

        assertAll(
            () -> assertEquals(
                SituacaoDoPedido.ABERTO, pedido.getSituacao()
            ),
            () -> assertEquals(5, notebook.getQuantidadeEmEstoque()),
            () -> assertNull(pedido.getComprovante()),
            () -> assertNull(pedido.getFormaPagamento())
        );
    }

    @Test
    @DisplayName("Deve impedir pagamento de pedido vazio")
    void deveImpedirPagamentoDePedidoVazio() {
        Pix pix = new Pix(
            new BigDecimal("50.00"), "ana@exemplo.com"
        );

        assertThrows(
            IllegalStateException.class,
            () -> pedido.pagar(pix)
        );
    }

    @Test
    @DisplayName("Deve impedir pagamento com processador nulo")
    void deveImpedirPagamentoComProcessadorNulo() {
        pedido.adicionarItem(notebook, 1);

        assertThrows(
            IllegalArgumentException.class,
            () -> pedido.pagar(null)
        );
    }

    @Test
    @DisplayName("Deve impedir pagamento duplicado")
    void deveImpedirPagamentoDuplicado() {
        pedido.adicionarItem(notebook, 1);

        Pix pix = new Pix(
            pedido.calcularValorTotal(), "ana@exemplo.com"
        );

        pedido.pagar(pix);

        assertThrows(
            IllegalStateException.class,
            () -> pedido.pagar(pix)
        );

        assertEquals(4, notebook.getQuantidadeEmEstoque());
    }

    @Test
    @DisplayName("Deve impedir inclusão de itens em pedido pago")
    void deveImpedirInclusaoDeItensEmPedidoPago() {
        pedido.adicionarItem(notebook, 1);

        Pix pix = new Pix(
            pedido.calcularValorTotal(), "ana@exemplo.com"
        );

        pedido.pagar(pix);

        assertThrows(
            IllegalStateException.class,
            () -> pedido.adicionarItem(mouse, 1)
        );

        assertAll(
            () -> assertEquals(1, pedido.getItens().size()),
            () -> assertEquals(
                0, new BigDecimal("3000.00")
                    .compareTo(pedido.calcularValorTotal())
            )
        );
    }
}