package com.ecommerce_pedidos.fluxo;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ecommerce_pedidos.modelo.Cliente;
import com.ecommerce_pedidos.modelo.Pedido;
import com.ecommerce_pedidos.modelo.Produto;
import com.ecommerce_pedidos.modelo.SituacaoDoPedido;
import com.ecommerce_pedidos.modelo.pagamento.Boleto;
import com.ecommerce_pedidos.modelo.pagamento.CartaoCredito;
import com.ecommerce_pedidos.modelo.pagamento.Dinheiro;
import com.ecommerce_pedidos.modelo.pagamento.ProcessadorPagamento;

/**
 * Fluxo de pagamento. O ProcessadorPagamento é o colaborador que, em produção,
 * falaria com um serviço externo (operadora, banco): ninguém cobra um cartão
 * real a cada mvn test, então ele entra como dublê (Mockito). Quando a
 * forma de pagamento é local (Boleto, Dinheiro), usamos a implementação real.
 */
@ExtendWith(MockitoExtension.class)
class PagamentoFluxoIT {

    @Mock
    ProcessadorPagamento processador;

    private Pedido pedido;
    private Produto notebook;

    @BeforeEach
    void prepararCenario() {
        Cliente cliente = new Cliente("Ana", "12345678900", "ana@email.com",
                null, null, null, null, null, null, null);
        notebook = new Produto("NTB-001", "Notebook", new BigDecimal("3000.00"), 5);
        pedido = new Pedido("PED-1", cliente);
        pedido.adicionarItem(notebook, 2);
    }

    @Test
    @DisplayName("Deve marcar o pedido como pago, baixar o estoque e cobrar o total exato quando o processador aprova")
    void devePagarEBaixarEstoqueQuandoProcessadorAprova() {
        when(processador.processar(any(BigDecimal.class))).thenReturn(true);
        when(processador.getComprovante()).thenReturn("COMP-123");

        boolean aprovado = pedido.pagar(processador);

        assertAll(
            () -> assertTrue(aprovado),
            () -> assertEquals(SituacaoDoPedido.PAGO, pedido.getSituacao()),
            () -> assertEquals("COMP-123", pedido.getComprovante()),
            () -> assertEquals(3, notebook.getQuantidadeEmEstoque())
        );
        // o valor cobrado é exatamente o total que o pedido calculou, e só foi cobrado uma vez
        verify(processador).processar(new BigDecimal("6000.00"));
    }

    @Test
    @DisplayName("Deve manter o pedido aberto, o estoque intacto e sem comprovante quando o processador recusa")
    void deveManterPedidoAbertoQuandoProcessadorRecusa() {
        when(processador.processar(any())).thenReturn(false);

        boolean aprovado = pedido.pagar(processador);

        assertAll(
            () -> assertFalse(aprovado),
            () -> assertEquals(SituacaoDoPedido.ABERTO, pedido.getSituacao()),
            () -> assertEquals(5, notebook.getQuantidadeEmEstoque()),
            () -> assertEquals(1, pedido.getItens().size()),
            () -> assertNull(pedido.getComprovante()),
            () -> verify(processador, never()).getComprovante()
        );
    }

    @Test
    @DisplayName("Deve permitir tentar de novo com outra forma depois de uma recusa")
    void devePermitirNovaTentativaDepoisDeRecusa() {
        when(processador.processar(any())).thenReturn(false);
        pedido.pagar(processador);

        boolean aprovado = pedido.pagar(new Dinheiro(new BigDecimal("6000.00")));

        assertAll(
            () -> assertTrue(aprovado),
            () -> assertEquals(SituacaoDoPedido.PAGO, pedido.getSituacao()),
            () -> assertEquals(3, notebook.getQuantidadeEmEstoque())
        );
    }

    @Test
    @DisplayName("Deve recusar pagar um pedido que já está pago, sem cobrar de novo")
    void deveRecusarPagarPedidoJaPago() {
        when(processador.processar(any())).thenReturn(true);
        when(processador.getComprovante()).thenReturn("COMP-1");
        pedido.pagar(processador);

        assertThrows(IllegalStateException.class, () -> pedido.pagar(processador));

        assertAll(
            () -> assertEquals(3, notebook.getQuantidadeEmEstoque()),
            () -> verify(processador, org.mockito.Mockito.times(1)).processar(any())
        );
    }

    @Test
    @DisplayName("Deve recusar processador nulo sem alterar o pedido")
    void deveRecusarProcessadorNulo() {
        assertThrows(IllegalArgumentException.class, () -> pedido.pagar(null));

        assertAll(
            () -> assertEquals(SituacaoDoPedido.ABERTO, pedido.getSituacao()),
            () -> assertEquals(5, notebook.getQuantidadeEmEstoque())
        );
    }

    @Test
    @DisplayName("Deve deixar o pedido aberto e o estoque intacto ao pagar com Boleto (aguarda compensação)")
    void deveManterPedidoAbertoAoPagarComBoleto() {
        Boleto boleto = new Boleto(new BigDecimal("6000.00"), "34191-0001", LocalDate.now().plusDays(3));

        boolean aprovado = pedido.pagar(boleto);

        assertAll(
            () -> assertFalse(aprovado),
            () -> assertEquals(SituacaoDoPedido.ABERTO, pedido.getSituacao()),
            () -> assertEquals(5, notebook.getQuantidadeEmEstoque())
        );
    }

    @Test
    @DisplayName("Deve pagar com cartão em 3 parcelas e baixar o estoque")
    void devePagarComCartaoEmTresParcelas() {
        CartaoCredito cartao = new CartaoCredito(new BigDecimal("6000.00"), "4111111111111111", 3);

        boolean aprovado = pedido.pagar(cartao);

        assertAll(
            () -> assertTrue(aprovado),
            () -> assertEquals(SituacaoDoPedido.PAGO, pedido.getSituacao()),
            () -> assertTrue(pedido.getComprovante().startsWith("CARTAO-1111-")),
            () -> assertEquals(3, notebook.getQuantidadeEmEstoque())
        );
    }

    @Test
    @DisplayName("Deve recusar dinheiro insuficiente sem baixar estoque")
    void deveRecusarDinheiroInsuficiente() {
        boolean aprovado = pedido.pagar(new Dinheiro(new BigDecimal("100.00")));

        assertAll(
            () -> assertFalse(aprovado),
            () -> assertEquals(SituacaoDoPedido.ABERTO, pedido.getSituacao()),
            () -> assertEquals(5, notebook.getQuantidadeEmEstoque())
        );
    }
}
