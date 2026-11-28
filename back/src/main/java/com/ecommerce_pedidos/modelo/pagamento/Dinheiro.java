package com.ecommerce_pedidos.modelo.pagamento;

import java.math.BigDecimal;

import com.ecommerce_pedidos.modelo.FormaPagamento;

public class Dinheiro extends FormaPagamento implements ProcessadorPagamento {

    private final BigDecimal valorRecebido;

    public Dinheiro(BigDecimal valorRecebido) {
        super(valorRecebido);
        this.valorRecebido = valorRecebido;
    }

    public BigDecimal getValorRecebido() {
        return valorRecebido;
    }

    public BigDecimal calcularTroco(BigDecimal valorDaCompra) {

        if (!processar(valorDaCompra)) {
            throw new IllegalArgumentException(
                "Valor recebido é insuficiente para a compra"
            );
        }

        return valorRecebido.subtract(valorDaCompra);
    }

    @Override
    public boolean processar(BigDecimal valorCobrado) {

        if (valorCobrado == null
                || valorCobrado.compareTo(BigDecimal.ZERO) <= 0) {

            throw new IllegalArgumentException(
                "Valor cobrado deve ser positivo"
            );
        }

        return valorRecebido.compareTo(valorCobrado) >= 0;
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