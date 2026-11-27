package com.ecommerce_pedidos.modelo;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ItemPedidoTest {


    private Produto teclado;

    @BeforeEach
    void prepararCenario() {
        teclado = new Produto("TEC-001", "Teclado", new BigDecimal("150.00"), 10);
    }

    @Test
    @DisplayName("Deve calcular o subtotal quando a quantidade é um")
    void deveCalcularSubtotalQuandoQuantidadeUm() {
        ItemPedido item = new ItemPedido(teclado, 1, new BigDecimal("150.00"));
        assertEquals(0, new BigDecimal("150.00").compareTo(item.calcularSubtotal()));
    }

    @Test
    @DisplayName("Deve calcular o subtotal quando a quantidade é maior que um")
    void deveCalcularSubtotalQuandoQuantidadeMaiorQueUm() {
        ItemPedido item = new ItemPedido(teclado, 3, new BigDecimal("50.50"));
        assertEquals(0, new BigDecimal("151.50").compareTo(item.calcularSubtotal()));
    }

    @Test
    @DisplayName("Deve guardar produto, quantidade e preço praticado quando criado")
    void deveGuardarDadosQuandoCriado() {
        ItemPedido item = new ItemPedido(teclado, 2, new BigDecimal("140.00"));
        assertSame(teclado, item.getProduto());
        assertEquals(2, item.getQuantidade());
        assertEquals(0, new BigDecimal("140.00").compareTo(item.getPrecoPraticado()));
    }

    @Test
    @DisplayName("Deve manter o preço praticado quando o preço do produto muda depois")
    void deveManterPrecoPraticadoQuandoPrecoDoProdutoMuda() {
        ItemPedido item = new ItemPedido(teclado, 2, new BigDecimal("150.00"));
        teclado.setPreco(new BigDecimal("999.00"));
        assertEquals(0, new BigDecimal("300.00").compareTo(item.calcularSubtotal()));
    }

    @Test
    @DisplayName("Deve aceitar preço praticado zero")
    void deveAceitarPrecoPraticadoZero() {
        ItemPedido item = new ItemPedido(teclado, 2, new BigDecimal("0.0"));
        assertEquals(0, new BigDecimal("0.0").compareTo(item.calcularSubtotal()));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -50})
    @DisplayName("Deve recusar item quando a quantidade não é positiva")
    void deveRecusarItemQuandoQuantidadeNaoPositiva(int quantidade) {
        assertThrows(IllegalArgumentException.class,
                () -> new ItemPedido(teclado, quantidade, new BigDecimal("150.00")));
    }

    @Test
    @DisplayName("Deve recusar item quando o preço praticado é negativo")
    void deveRecusarItemQuandoPrecoPraticadoNegativo() {
        assertThrows(IllegalArgumentException.class,
                () -> new ItemPedido(teclado, 1, new BigDecimal("-0.01")));
    }

    @Test
    @DisplayName("Deve recusar item quando o produto é nulo")
    void deveRecusarItemQuandoProdutoNulo() {
        assertThrows(IllegalArgumentException.class,
                () -> new ItemPedido(null, 1, new BigDecimal("150.00")));
    }
}