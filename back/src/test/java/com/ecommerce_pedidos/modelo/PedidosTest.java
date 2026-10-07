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

import com.ecommerce_pedidos.excecao.EstoqueInsuficienteException;
import com.ecommerce_pedidos.excecao.PedidoInvalidoException;

class PedidosTest {

    private static final double DELTA = 0.001;

    private Cliente cliente;
    private Produto notebook;
    private Produto mouse;
    private Pedidos pedido;

    @BeforeEach
    void prepararCenario() {
        cliente = new Cliente("Ana Souza", "12345678900", "ana@exemplo.com", null, null,
                null, null, null, null, null);
        notebook = new Produto("NOTE-001", "Notebook", new BigDecimal("3000.00"), 5);
        mouse = new Produto("MOU-001", "Mouse", new BigDecimal("50.00"), 10);
        pedido = new Pedidos(cliente);
    }

    // ---------- criação e situação ----------

    @Test
    @DisplayName("Deve criar pedido aberto e vazio quando há cliente")
    void deveCriarPedidoAbertoEVazioQuandoHaCliente() {
        assertEquals("ABERTO", pedido.getSituacao());
        assertSame(cliente, pedido.getCliente());
        assertTrue(pedido.getItens().isEmpty());
        assertEquals(LocalDate.now(), pedido.getData());
    }

    @Test
    @DisplayName("Deve gerar número no formato PED-ANO-99999 quando o pedido é criado")
    void deveGerarNumeroNoFormatoEsperado() {
        assertTrue(pedido.getNumero().matches("PED-\\d{4}-\\d{5}"));
    }

    @Test
    @DisplayName("Deve recusar pedido quando não há cliente")
    void deveRecusarPedidoQuandoClienteNulo() {
        assertThrows(IllegalArgumentException.class, () -> new Pedidos(null));
    }

    @Test
    @DisplayName("Deve alterar a situação quando a nova situação é informada")
    void deveAlterarSituacaoQuandoInformada() {
        pedido.setSituacao("PAGO");
        assertEquals("PAGO", pedido.getSituacao());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    @DisplayName("Deve recusar situação vazia e manter a anterior")
    void deveRecusarSituacaoVaziaEManterAnterior(String situacao) {
        assertThrows(IllegalArgumentException.class, () -> pedido.setSituacao(situacao));
        assertEquals("ABERTO", pedido.getSituacao());
    }

    // ---------- itens e total ----------

    @Test
    @DisplayName("Deve ter total zero quando o pedido não tem itens")
    void deveTerTotalZeroQuandoSemItens() {
        assertEquals(0.0, pedido.calcularValorTotal(), DELTA);
    }

    @Test
    @DisplayName("Deve somar o total corretamente com um item")
    void deveSomarTotalComUmItem() throws Exception {
        pedido.adicionarItem(notebook, 1);
        assertEquals(3000.00, pedido.calcularValorTotal(), DELTA);
    }

    @Test
    @DisplayName("Deve somar o total corretamente com dois itens")
    void deveSomarTotalComDoisItens() throws Exception {
        pedido.adicionarItem(notebook, 2);
        pedido.adicionarItem(mouse, 3);
        assertEquals(6150.00, pedido.calcularValorTotal(), DELTA);
    }

    @Test
    @DisplayName("Deve somar o total quando o item é adicionado já montado")
    void deveSomarTotalQuandoItemAdicionadoMontado() {
        pedido.adicionarItem(new itemPedido(notebook, 2, 3000.00));
        assertEquals(6000.00, pedido.calcularValorTotal(), DELTA);
    }

    @Test
    @DisplayName("Deve baixar o estoque quando o item é adicionado pelo produto")
    void deveBaixarEstoqueQuandoItemAdicionadoPeloProduto() throws Exception {
        pedido.adicionarItem(notebook, 2);
        assertEquals(3, notebook.getQuantidadeEmEstoque());
    }

    @Test
    @DisplayName("Deve recusar item nulo quando adicionado ao pedido")
    void deveRecusarItemNulo() {
        assertThrows(IllegalArgumentException.class, () -> pedido.adicionarItem((itemPedido) null));
        assertTrue(pedido.getItens().isEmpty());
    }

    // ---------- Mapa de Exceções ----------

    @Test
    @DisplayName("Deve lançar PedidoInvalido quando o estoque é insuficiente e não alterar nada")
    void deveLancarPedidoInvalidoQuandoEstoqueInsuficiente() {
        PedidoInvalidoException erro = assertThrows(PedidoInvalidoException.class,
                () -> pedido.adicionarItem(notebook, 50));

        assertInstanceOf(EstoqueInsuficienteException.class, erro.getCause());
        assertTrue(erro.getMessage().contains("Notebook"));
        assertEquals(5, notebook.getQuantidadeEmEstoque()); // estoque intacto
        assertTrue(pedido.getItens().isEmpty());            // nenhum item entrou
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    @DisplayName("Deve lançar PedidoInvalido quando a quantidade não é positiva")
    void deveLancarPedidoInvalidoQuandoQuantidadeNaoPositiva(int quantidade) {
        PedidoInvalidoException erro = assertThrows(PedidoInvalidoException.class,
                () -> pedido.adicionarItem(notebook, quantidade));

        assertInstanceOf(IllegalArgumentException.class, erro.getCause());
        assertEquals(5, notebook.getQuantidadeEmEstoque());
        assertTrue(pedido.getItens().isEmpty());
    }
}