package com.ecommerce_pedidos.modelo;

import java.math.BigDecimal;

public abstract class FormaPagamento {

    private BigDecimal valor;

    protected FormaPagamento(BigDecimal valor) {
        setValor(valor);
    }

    public void setValor(BigDecimal valor) {
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor do pagamento deve ser positivo");
        }
        this.valor = valor;
    }

    public BigDecimal getValor() {
        return valor;
    }

    // Versão completa: cada forma de pagamento implementa a sua regra aqui.
    public abstract boolean processar(BigDecimal valorCobrado);

    // Versão curta: delega à completa, sem duplicar lógica.
    public boolean processar() {
        return processar(valor);
    }

    // Regra comum: o valor cobrado pelo pedido deve ser o valor deste pagamento.
    protected void validarValorCobrado(BigDecimal valorCobrado) {
        if (valorCobrado == null || valorCobrado.compareTo(valor) != 0) {
            throw new IllegalArgumentException(
                "Valor cobrado R$ " + valorCobrado + " difere do valor do pagamento R$ " + valor);
        }
    }

    public String getResumo() {
        return String.format("%s no valor de R$ %s", getClass().getSimpleName(), valor);
    }
}