package com.ecommerce_pedidos.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PedidosUtilsTest {

    @Test
    @DisplayName("Deve somar preço x quantidade de cada linha")
    void deveCalcularSubtotal() {
        double subtotal = PedidosUtils.calcularSubtotal(new double[] {10.0, 2.5}, new int[] {3, 4});

        assertEquals(40.0, subtotal, 0.0001);
    }

    @Test
    @DisplayName("Subtotal sem linhas é zero")
    void subtotalSemLinhasEZero() {
        assertEquals(0.0, PedidosUtils.calcularSubtotal(new double[0], new int[0]), 0.0001);
    }
}
