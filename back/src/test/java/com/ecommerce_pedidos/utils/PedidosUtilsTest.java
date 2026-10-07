package com.ecommerce_pedidos.utils;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;


public class PedidosUtilsTest {
    private static final double DELTA = 0.001;

    @RepeatedTest(20)
    @DisplayName("Deve gerar número do pedido no formato esperado")
    void deveGerarNumeroNoFormatoEsperado() {
        assertTrue(PedidosUtils.gerarNumeroDoPedido().matches("PED-2026-\\d{5}"));
    }

    @Test
    @DisplayName("Deve calcular o subtotal quando há vários preços e quantidades")
    void deveCalcularSubtotalComVariosItens() {
        double subtotal = PedidosUtils.calcularSubtotal(
                new double[] {378.83, 123.67}, new int[] {3, 7});
        assertEquals(2002.18, subtotal, DELTA);
    }

    @Test
    @DisplayName("Deve calcular subtotal zero quando não há itens")
    void deveCalcularSubtotalZeroQuandoSemItens() {
        assertEquals(0.0, PedidosUtils.calcularSubtotal(new double[0], new int[0]), DELTA);
    }
}
