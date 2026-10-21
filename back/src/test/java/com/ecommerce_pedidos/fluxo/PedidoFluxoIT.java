package com.ecommerce_pedidos.fluxo;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.ecommerce_pedidos.modelo.Cliente;
import com.ecommerce_pedidos.modelo.Pedido;
import com.ecommerce_pedidos.modelo.Produto;
import com.ecommerce_pedidos.modelo.SituacaoDoPedido;
import com.ecommerce_pedidos.modelo.pagamento.Pix;

/**
 * Fluxo do pedido: criação, itens, total, pagamento e baixa de estoque.
 * Tudo com objetos reais — o objetivo é ver o encaixe entre as peças.
 */
class PedidoFluxoIT {

    private Cliente cliente;
    private Produto notebook;
    private Produto mouse;

    @BeforeEach
    void prepararCenario() {
        cliente = new Cliente("Ana", "12345678900", "ana@email.com",
                null, null, null, null, null, null, null);
        notebook = new Produto("NTB-001", "Notebook", new BigDecimal("3000.00"), 5);
        mouse = new Produto("MOU-001", "Mouse", new BigDecimal("50.00"), 10);
    }

    @Test
    @DisplayName("Deve criar, somar e pagar um pedido, baixando o estoque")
    void deveExecutarFluxoCompletoDePedido() {
        Pedido pedido = new Pedido("PED-1", cliente);   // Arrange
        pedido.adicionarItem(notebook, 2);               // Act
        boolean aprovado = pedido.pagar(new Pix(new BigDecimal("6000.00"), "ana@email.com"));

        assertAll(                                       // Assert
            () -> assertTrue(aprovado),
            () -> assertEquals(0, new BigDecimal("6000.00").compareTo(pedido.calcularValorTotal())),
            () -> assertEquals(SituacaoDoPedido.PAGO, pedido.getSituacao()),
            () -> assertEquals(3, notebook.getQuantidadeEmEstoque()),
            () -> assertEquals(1, pedido.getItens().size())
        );
    }

    @Test
    @DisplayName("Deve somar vários itens e recusar o mesmo produto duas vezes")
    void deveSomarVariosItensSemDuplicar() {
        Pedido pedido = new Pedido("PED-2", cliente);
        pedido.adicionarItem(notebook, 1);
        pedido.adicionarItem(mouse, 3);
        pedido.adicionarItem(new Produto("TEC-001", "Teclado", new BigDecimal("150.00"), 4)); // sobrecarga: quantidade 1

        assertThrows(IllegalStateException.class, () -> pedido.adicionarItem(mouse, 1));

        // 3000 + 3 x 50 + 150 = 3300
        assertAll(
            () -> assertEquals(0, new BigDecimal("3300.00").compareTo(pedido.calcularValorTotal())),
            () -> assertEquals(3, pedido.getItens().size()),
            () -> assertEquals(0, new BigDecimal("150.00")
                    .compareTo(pedido.getItens().get(1).calcularSubtotal()))
        );
    }

    @Test
    @DisplayName("Deve manter o pedido utilizável quando o estoque é insuficiente")
    void devePreservarPedidoQuandoEstoqueInsuficiente() {
        Pedido pedido = new Pedido("PED-3", cliente);
        pedido.adicionarItem(mouse, 2);
        BigDecimal totalAntes = pedido.calcularValorTotal();

        assertThrows(IllegalStateException.class, () -> pedido.adicionarItem(notebook, 99));

        // depois da falha: nada mudou e o pedido continua aceitando itens
        assertAll(
            () -> assertEquals(1, pedido.getItens().size()),
            () -> assertEquals(0, totalAntes.compareTo(pedido.calcularValorTotal())),
            () -> assertEquals(5, notebook.getQuantidadeEmEstoque()),
            () -> assertEquals(SituacaoDoPedido.ABERTO, pedido.getSituacao())
        );
        pedido.adicionarItem(notebook, 1);
        assertEquals(2, pedido.getItens().size());
    }

    @Test
    @DisplayName("Deve recusar pagar pedido sem itens e não mudar nada no sistema")
    void deveRecusarPagamentoDePedidoSemItens() {
        Pedido pedido = new Pedido("PED-4", cliente);

        assertThrows(IllegalStateException.class,
                () -> pedido.pagar(new Pix(new BigDecimal("10.00"), "ana@email.com")));

        assertAll(
            () -> assertEquals(SituacaoDoPedido.ABERTO, pedido.getSituacao()),
            () -> assertNull(pedido.getComprovante()),
            () -> assertEquals(0, pedido.getItens().size())
        );
    }

    @Test
    @DisplayName("Deve proteger a lista de itens contra alteração por fora")
    void deveProtegerListaDeItens() {
        Pedido pedido = new Pedido("PED-5", cliente);
        pedido.adicionarItem(mouse, 1);

        assertThrows(UnsupportedOperationException.class, () -> pedido.getItens().clear());
        assertEquals(1, pedido.getItens().size());
    }

    @Test
    @DisplayName("Deve recusar o pagamento quando o estoque acabou depois de o item entrar no pedido")
    void deveRecusarPagamentoQuandoEstoqueAcabouDepoisDeAdicionar() {
        Pedido pedidoDaAna = new Pedido("PED-6", cliente);
        Pedido pedidoDoBruno = new Pedido("PED-7", cliente);
        pedidoDaAna.adicionarItem(notebook, 5);   // os dois pedidos reservam, no papel, o mesmo estoque
        pedidoDoBruno.adicionarItem(notebook, 5);

        pedidoDaAna.pagar(new Pix(new BigDecimal("15000.00"), "ana@email.com"));

        assertThrows(IllegalStateException.class,
                () -> pedidoDoBruno.pagar(new Pix(new BigDecimal("15000.00"), "bruno@email.com")));

        assertAll(
            () -> assertEquals(0, notebook.getQuantidadeEmEstoque()),
            () -> assertEquals(SituacaoDoPedido.PAGO, pedidoDaAna.getSituacao()),
            () -> assertEquals(SituacaoDoPedido.ABERTO, pedidoDoBruno.getSituacao()),
            () -> assertNull(pedidoDoBruno.getComprovante())
        );
    }
}
