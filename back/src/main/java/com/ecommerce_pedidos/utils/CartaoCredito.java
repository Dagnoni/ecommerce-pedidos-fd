package com.ecommerce_pedidos.utils;

import java.math.BigDecimal;

import com.ecommerce_pedidos.modelo.FormaPagamento;
import com.ecommerce_pedidos.modelo.pagamento.Estornavel;
import com.ecommerce_pedidos.modelo.pagamento.ProcessadorPagamento;

public class CartaoCredito extends FormaPagamento implements ProcessadorPagamento, Estornavel {

    private static final int MAX_PARCELAS = 12;

    private String numeroCartao;
    private int parcelas;

    public CartaoCredito(BigDecimal valor, String numeroCartao, int parcelas) {
        super(valor);
        setNumeroCartao(numeroCartao);
        setParcelas(parcelas);
    }

    public String getNumeroCartao() {
        return numeroCartao;
    }

    public void setNumeroCartao(String numeroCartao) {
        if (numeroCartao == null || numeroCartao.isBlank()) {
            throw new IllegalArgumentException("Número do cartão é obrigatório");
        }
        this.numeroCartao = numeroCartao;
    }

    public int getParcelas() {
        return parcelas;
    }

    public void setParcelas(int parcelas) {
        if (parcelas < 1 || parcelas > MAX_PARCELAS) {
            throw new IllegalArgumentException("Parcelas deve ser entre 1 e " + MAX_PARCELAS);
        }
        this.parcelas = parcelas;
    }

    private String ultimosDigitos() {
        return numeroCartao.length() <= 4 ? numeroCartao : numeroCartao.substring(numeroCartao.length() - 4);
    }

    @Override
    public boolean processar() {
        System.out.println("Autorizando cartão final " + ultimosDigitos() + " em " + parcelas + "x");
        return true;
    }

    @Override
    public boolean processar(BigDecimal valor) {
        return processar();
    }
<<<<<<< HEAD

    @Override
    public String getComprovante() {
        return "CARTAO-" + ultimosDigitos() + "-" + System.currentTimeMillis();
    }

    @Override
    public String getDescricao() {
        return "Cartão final " + ultimosDigitos() + " em " + parcelas + "x";
    }

    @Override
    public boolean estornar() {
        System.out.println("Estornando compra do cartão final " + ultimosDigitos());
        return true;
    }
}
=======
}




// falta colocar bandeira
>>>>>>> 070516358b824f0c1b9f8885c70100380fe771ed
