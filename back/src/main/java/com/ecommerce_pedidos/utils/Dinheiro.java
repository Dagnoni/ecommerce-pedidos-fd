package com.ecommerce_pedidos.utils;

import java.math.BigDecimal;

import com.ecommerce_pedidos.modelo.FormaPagamento;
import com.ecommerce_pedidos.modelo.pagamento.ProcessadorPagamento;

public class Dinheiro extends FormaPagamento implements ProcessadorPagamento {

    private final BigDecimal valorRecebido;

    public Dinheiro(BigDecimal valorRecebido) {
        super(valorRecebido);
        if (valorRecebido == null || valorRecebido.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Valor recebido não pode ser negativo");
        }
        this.valorRecebido = valorRecebido;
    }

    public BigDecimal calcularTroco(BigDecimal valorDaCompra) {
        return valorRecebido.subtract(valorDaCompra);
    }

    @Override
    public boolean processar() {
        return valorRecebido.compareTo(getValor()) >= 0;
    }

    @Override
    public boolean processar(BigDecimal valor) {
        return valorRecebido.compareTo(valor) >= 0;
    }

    @Override
    public String getComprovante() {
        return "RECIBO-" + System.currentTimeMillis();
    }

    @Override
    public String getDescricao() {
        return "Dinheiro";
    }
}