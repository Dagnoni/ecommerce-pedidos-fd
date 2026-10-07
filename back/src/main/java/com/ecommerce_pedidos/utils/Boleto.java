package com.ecommerce_pedidos.utils;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.ecommerce_pedidos.modelo.FormaPagamento;
import com.ecommerce_pedidos.modelo.pagamento.ProcessadorPagamento;

public class Boleto extends FormaPagamento implements ProcessadorPagamento {

    private String numeroBoleto;
    private LocalDate dataVencimento;

    public Boleto(BigDecimal valor, String numeroBoleto, LocalDate dataVencimento) {
        super(valor);
        setNumeroBoleto(numeroBoleto);
        setDataVencimento(dataVencimento);
    }

    public String getNumeroBoleto() {
        return numeroBoleto;
    }

    public void setNumeroBoleto(String numeroBoleto) {
        if (numeroBoleto == null || numeroBoleto.isBlank()) {
            throw new IllegalArgumentException("Número do boleto é obrigatório");
        }
        this.numeroBoleto = numeroBoleto;
    }

    public LocalDate getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(LocalDate dataVencimento) {
        if (dataVencimento == null || dataVencimento.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Data de vencimento inválida: " + dataVencimento);
        }
        this.dataVencimento = dataVencimento;
    }

    @Override
    public boolean processar() {
        System.out.println("Boleto " + numeroBoleto + " gerado, aguardando compensação (vence em " + dataVencimento + ")");
        return false;
    }

    @Override
    public boolean processar(BigDecimal valor) {
        return processar();
    }

    @Override
    public String getComprovante() {
        return "BOL-" + numeroBoleto;
    }

    @Override
    public String getDescricao() {
        return "Boleto " + numeroBoleto + ", vence em " + dataVencimento;
    }
}