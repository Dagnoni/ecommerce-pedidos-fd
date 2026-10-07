package com.ecommerce_pedidos.utils;
import java.math.BigDecimal;
import java.time.LocalDate;

import com.ecommerce_pedidos.modelo.FormaPagamento;

public class Boleto extends FormaPagamento {
    private String numeroBoleto;
    private LocalDate dataVencimento;

    public Boleto(BigDecimal valor, String numeroBoleto, LocalDate dataVencimento){
        super(valor);
        setNumeroBoleto(numeroBoleto);
        setDataVencimento(dataVencimento);
    }

    public String getNumeroBoleto(){
        return numeroBoleto;
    }

    public void setNumeroBoleto(String numeroBoleto){
        if (numeroBoleto == null || numeroBoleto.isBlank()){
            throw new IllegalArgumentException("Número do boleto é obrigatório");
        }
        this.numeroBoleto = numeroBoleto;
    }

    public LocalDate getDataVencimento(){
        return dataVencimento;
    }

    public void setDataVencimento(LocalDate dataVencimento){
        if (dataVencimento == null){
            throw new IllegalArgumentException("Data de vencimento é obrigatória");
        }
        this.dataVencimento = dataVencimento;
    }

    @Override
    public boolean processar(){
        //simulação
        if (dataVencimento.isBefore(LocalDate.now())){
            System.out.println("Boleto " + numeroBoleto + " vencido, pagamento recusado");
            return false;
        }
        System.out.println("Processando boleto " + numeroBoleto + " com vencimento em " + dataVencimento);
        return true;
    }

    @Override
    public String getResumo(){
        return super.getResumo() + " (boleto " + numeroBoleto + ", vence em " + dataVencimento + ")";
    }
}
