package com.ecommerce_pedidos.modelo.pagamento;

import java.math.BigDecimal;

import com.ecommerce_pedidos.modelo.FormaPagamento;

public class Pix extends FormaPagamento implements ProcessadorPagamento, Estornavel {

    private String chave;

    public Pix(BigDecimal valor, String chave) {
        super(valor);
        setChave(chave);
    }

    public String getChave() {
        return chave;
    }

    public void setChave(String chave) {
        if (chave == null || chave.isBlank()) {
            throw new IllegalArgumentException("Chave Pix é obrigatória");
        }
        this.chave = chave;
    }

    @Override
    public boolean processar() {
        System.out.println("Enviando cobrança Pix para a chave " + chave);
        return true;
    }

    @Override
    public boolean processar(BigDecimal valor) {
        return processar();
    }

    @Override
    public String getComprovante() {
        return "PIX-" + System.currentTimeMillis();
    }

    @Override
    public String getDescricao() {
        return "Pix - chave " + chave;
    }

    @Override
    public String getResumo() {
        return super.getResumo() + " (chave " + chave + ")";
    }

    @Override
    public String getResumoPagamento() {
        return getDescricao() + " (aprovação imediata)";
    }

    @Override
    public boolean estornar() {
        System.out.println("Estornando Pix da chave " + chave);
        return true;
    }
}