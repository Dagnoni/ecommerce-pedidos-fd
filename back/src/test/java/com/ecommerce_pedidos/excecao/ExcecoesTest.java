package com.ecommerce_pedidos.excecao;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

//import com.ecommerce_pedidos.excecao.ClienteNaoEncontradoException;
//import com.ecommerce_pedidos.excecao.EstoqueInsuficienteException;
//import com.ecommerce_pedidos.excecao.PagamentoRecusadoException;
//import com.ecommerce_pedidos.excecao.PedidoInvalidoException;
import com.ecommerce_pedidos.modelo.Produto;

class ExcecoesTest {

    @Test
    @DisplayName("Deve carregar produto, quantidade e motivo quando o estoque é insuficiente")
    void deveCarregarDadosQuandoEstoqueInsuficiente() {
        Produto notebook = new Produto("NOTE-001", "Notebook", new BigDecimal("3000.00"), 5);

        EstoqueInsuficienteException erro = new EstoqueInsuficienteException(notebook, 50);

        assertSame(notebook, erro.getProduto());
        assertEquals(50, erro.getQuantidadeSolicitada());
        assertTrue(erro.getMessage().contains("Notebook"));
        assertEquals("disponível 5, solicitado 50", erro.getMotivo());
    }

    @Test
    @DisplayName("Deve carregar o documento quando o cliente não é encontrado")
    void deveCarregarDocumentoQuandoClienteNaoEncontrado() {
        ClienteNaoEncontradoException erro = new ClienteNaoEncontradoException("12345678900");

        assertEquals("12345678900", erro.getDocumento());
        assertTrue(erro.getMessage().contains("12345678900"));
    }

    @Test
    @DisplayName("Deve montar mensagem e motivo quando o pagamento é recusado")
    void deveMontarMensagemEMotivoQuandoPagamentoRecusado() {
        PagamentoRecusadoException erro = new PagamentoRecusadoException("Pix", "saldo insuficiente");

        assertEquals("Pagamento por Pix recusado: saldo insuficiente", erro.getMessage());
        assertEquals("saldo insuficiente", erro.getMotivo());
    }

    @Test
    @DisplayName("Deve usar a mensagem da causa como motivo quando há causa")
    void deveUsarMensagemDaCausaComoMotivo() {
        IllegalArgumentException causa = new IllegalArgumentException("quantidade inválida");

        PedidoInvalidoException erro = new PedidoInvalidoException("Pedido inválido", causa);

        assertSame(causa, erro.getCause());
        assertEquals("quantidade inválida", erro.getMotivo());
    }

    @Test
    @DisplayName("Deve usar a própria mensagem como motivo quando não há causa")
    void deveUsarMensagemComoMotivoQuandoSemCausa() {
        PedidoInvalidoException erro = new PedidoInvalidoException("Pedido sem itens");

        assertNull(erro.getCause());
        assertEquals("Pedido sem itens", erro.getMotivo());
    }
}