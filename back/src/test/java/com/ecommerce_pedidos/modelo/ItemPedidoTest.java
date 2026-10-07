package com.ecommerce_pedidos.modelo;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ItemPedidoTest {

    private static final double DELTA = 0.001;

    private Produto teclado;

    @BeforeEach
    void prepararCenario() {
        teclado = new Produto("TEC-001", "Teclado", new BigDecimal("150.00"), 10);
    }

    @Test
    @DisplayName("Deve calcular o subtotal quando a quantidade é um")
    void deveCalcularSubtotalQuandoQuantidadeUm() {
        itemPedido item = new itemPedido(teclado, 1, 150.00);
        assertEquals(150.00, item.calcularSubtotal(), DELTA);
    }

    @Test
    @DisplayName("Deve calcular o subtotal quando a quantidade é maior que um")
    void deveCalcularSubtotalQuandoQuantidadeMaiorQueUm() {
        itemPedido item = new itemPedido(teclado, 3, 50.50);
        assertEquals(151.50, item.calcularSubtotal(), DELTA);
    }

    @Test
    @DisplayName("Deve guardar produto, quantidade e preço praticado quando criado")
    void deveGuardarDadosQuandoCriado() {
        itemPedido item = new itemPedido(teclado, 2, 140.00);
        assertSame(teclado, item.getProduto());
        assertEquals(2, item.getQuantidade());
        assertEquals(140.00, item.getPrecoPraticado(), DELTA);
    }

    @Test
    @DisplayName("Deve manter o preço praticado quando o preço do produto muda depois")
    void deveManterPrecoPraticadoQuandoPrecoDoProdutoMuda() {
        itemPedido item = new itemPedido(teclado, 2, 150.00);
        teclado.setPreco(new BigDecimal("999.00"));
        assertEquals(300.00, item.calcularSubtotal(), DELTA);
    }

    @Test
    @DisplayName("Deve aceitar preço praticado zero")
    void deveAceitarPrecoPraticadoZero() {
        itemPedido item = new itemPedido(teclado, 2, 0.0);
        assertEquals(0.0, item.calcularSubtotal(), DELTA);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -50})
    @DisplayName("Deve recusar item quando a quantidade não é positiva")
    void deveRecusarItemQuandoQuantidadeNaoPositiva(int quantidade) {
        assertThrows(IllegalArgumentException.class,
                () -> new itemPedido(teclado, quantidade, 150.00));
    }

    @Test
    @DisplayName("Deve recusar item quando o preço praticado é negativo")
    void deveRecusarItemQuandoPrecoPraticadoNegativo() {
        assertThrows(IllegalArgumentException.class,
                () -> new itemPedido(teclado, 1, -0.01));
    }

    @Test
    @DisplayName("Deve recusar item quando o produto é nulo")
    void deveRecusarItemQuandoProdutoNulo() {
        assertThrows(IllegalArgumentException.class,
                () -> new itemPedido(null, 1, 150.00));
    }
}