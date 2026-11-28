package com.ecommerce_pedidos.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PedidosUtilsTest {

    @Test
    @DisplayName("Deve somar preço x quantidade de cada linha")
    void deveCalcularSubtotal() {
        double subtotal = PedidosUtils.calcularSubtotal(new double[] { 10.0, 2.5 }, new int[] { 3, 4 });

        assertEquals(40.0, subtotal, 0.0001);
    }

    @Test
    @DisplayName("Subtotal sem linhas é zero")
    void subtotalSemLinhasEZero() {
        assertEquals(0.0, PedidosUtils.calcularSubtotal(new double[0], new int[0]), 0.0001);
    }

    @Test
    @DisplayName("Deve respeitar o valor mínimo do frete")
    void deveRespeitarFreteMinimo() {
        assertEquals(15.00, PedidosUtils.calcularFrete(0.4, 100.00), 0.01);
    }

    @Test
    @DisplayName("Deve arredondar o peso para cima")
    void deveArredondarPesoParaCima() {
        assertEquals(22.50, PedidosUtils.calcularFrete(2.3, 150.00), 0.01);
    }

    @Test
    @DisplayName("Deve aplicar frete grátis")
    void deveAplicarFreteGratis() {
        assertEquals(0.00, PedidosUtils.calcularFrete(5.0, 300.00), 0.01);
    }

    @Test
    @DisplayName("Deve calcular desconto de dez por cento")
    void deveCalcularDesconto() {
        assertEquals(20.00, PedidosUtils.calcularDesconto(200.00), 0.01);
    }

    @Test
    @DisplayName("Deve respeitar o limite máximo de desconto")
    void deveRespeitarLimiteDesconto() {
        assertEquals(50.00, PedidosUtils.calcularDesconto(900.00), 0.01);
    }
}
