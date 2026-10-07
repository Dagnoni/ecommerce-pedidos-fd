package com.ecommerce_pedidos.modelo;

import java.math.BigDecimal;

// import java.time.LocalDateTime;

public abstract class FormaPagamento {

<<<<<<< HEAD
    private BigDecimal valor;
    private LocalDateTime dataDoPagamento;
=======
private BigDecimal valor;

// private LocalDateTime dataDoPagamento;
>>>>>>> 070516358b824f0c1b9f8885c70100380fe771ed

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

    public abstract boolean processar();

    public String getResumo() {
        return String.format("%s no valor de R$ %s", getClass().getSimpleName(), valor);
    }

    public String getResumoPagamento() {
        return getResumo();
    }
}