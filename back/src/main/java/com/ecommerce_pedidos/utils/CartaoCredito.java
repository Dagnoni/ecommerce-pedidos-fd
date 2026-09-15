package com.ecommerce_pedidos.utils;
import java.math.BigDecimal;

import com.ecommerce_pedidos.modelo.FormaPagamento;

public class CartaoCredito extends FormaPagamento {
    private String numeroCartao;
    private int parcelas;

    public CartaoCredito(BigDecimal valor, String numeroCartao, int parcelas){
        super(valor);
        setNumeroCartao(numeroCartao);
        setParcelas(parcelas);
    }

    public String getNumeroCartao(){
        return numeroCartao;
    }

    public void setNumeroCartao(String numeroCartao){
        if (numeroCartao == null || numeroCartao.isBlank()){
            throw new IllegalArgumentException("Número do cartão é obrigatório");
        }
        this.numeroCartao = numeroCartao;
    }

    public int getParcelas(){
        return parcelas;
    }

    public void setParcelas(int parcelas){
        if (parcelas < 1 || parcelas > 12){
            throw new IllegalArgumentException("Parcelas deve ser entre 1 e 12");
        }
        this.parcelas = parcelas;
    }

    private String ultimosDigitos(){
        if (numeroCartao.length() <= 4){
            return numeroCartao;
        }
        return numeroCartao.substring(numeroCartao.length() - 4);
    }

    @Override
    public boolean processar(){
        //simulação
        System.out.println("Processando cartão final " + ultimosDigitos() + " em " + parcelas + "x");
        return true;
    }

    @Override
    public String getResumo(){
        return super.getResumo() + " (cartão final " + ultimosDigitos() + ", " + parcelas + "x)";
    }
}
