package com.ecommerce_pedidos.utils;
import java.math.BigDecimal;

import com.ecommerce_pedidos.modelo.FormaPagamento;

public class Pix extends FormaPagamento {
    private String chave;   
    public Pix(BigDecimal valor, String chave){
        super(valor);
        setChave(chave);
    }
    public void setChave(String chave){
        if (chave == null || chave.isBlank()){
            throw new IllegalArgumentException("Chave Pix é Obrigatória");
        }
        this.chave = chave;
    }
    @Override 
    public boolean processar(){
        //simulação
        System.out.println("Processando Pix para a chave " + chave);
        return true;
    }
    @Override 
    public String getResumo(){
        return super.getResumo() + "(chave" + chave + ")";
    }

}