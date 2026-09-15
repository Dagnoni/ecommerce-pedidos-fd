package com.ecommerce_pedidos.utils;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.ecommerce_pedidos.modelo.Cliente;
import com.ecommerce_pedidos.modelo.FormaPagamento;
import com.ecommerce_pedidos.modelo.Produto;
// Se Pix, CartaoCredito e Boleto estiverem em outro pacote (ex: com.ecommerce_pedidos.pagamento), importe-os aqui:
// import com.ecommerce_pedidos.pagamento.Pix;
// import com.ecommerce_pedidos.pagamento.CartaoCredito;
// import com.ecommerce_pedidos.pagamento.Boleto;

public class App {
    public static void main(String[] args) {
        Produto teclado = new Produto("TEC-001", "Teclado", new BigDecimal("150.00"), 8);
        Produto monitor = new Produto("MON-001", "Monitor", new BigDecimal("899.90"), 76);
        
        System.out.println(teclado);
        System.out.println(monitor);

        System.out.println(teclado.temEstoqueDisponivel(5));  // true
        System.out.println(monitor.temEstoqueDisponivel(100)); // false

        teclado.baixarEstoque(3);
        System.out.println(teclado); 

        Cliente cliente = new Cliente(
            "Ana Souza", "12345678900", "ana@exemplo.com",
            "11999990000", "Rua das Flores", "Centro", 100,
            "São Carlos", "SP", "Brasil"
        );
        System.out.println(cliente.getIdentificacao());

        try {
            teclado.setPreco(new BigDecimal("-10.00"));
            System.out.println("FALHOU: aceitou preço negativo");
        } catch (IllegalArgumentException e) {
            System.out.println("OK: recusou preço negativo -> " + e.getMessage());
        }

        try {
            new Cliente("Bruno", "abc123", "bruno@exemplo.com",
                null, null, null, null, null, null, null);
            System.out.println("FALHOU: aceitou documento com letras");
        } catch (IllegalArgumentException e) {
            System.out.println("OK: recusou documento inválido -> " + e.getMessage());
        }

        try {
            new Cliente("Carla", "98765432100", "email-sem-arroba",
                null, null, null, null, null, null, null);
            System.out.println("FALHOU: aceitou e-mail sem @");
        } catch (IllegalArgumentException e) {
            System.out.println("OK: recusou e-mail inválido -> " + e.getMessage());
        }

        // FORMAS DE PAGAMENTO 
        FormaPagamento pagamentoPix = new Pix(new BigDecimal("150.00"), "ana@exemplo.com");
        FormaPagamento pagamentoCartao = new CartaoCredito(new BigDecimal("899.90"), "4111111111111111", 3);
        FormaPagamento pagamentoBoleto = new Boleto(new BigDecimal("300.00"), "34191790010104351004791020150008196610000015000", LocalDate.now().plusDays(5));

        FormaPagamento[] pagamentos = { pagamentoPix, pagamentoCartao, pagamentoBoleto };

        for (FormaPagamento pagamento : pagamentos) {
            System.out.println(pagamento.getResumo());
            pagamento.processar();
        }

        try {
            new CartaoCredito(new BigDecimal("100.00"), "4111111111111111", 15);
            System.out.println("FALHOU: aceitou 15 parcelas");
        } catch (IllegalArgumentException e) {
            System.out.println("OK: recusou parcelamento inválido -> " + e.getMessage());
        }
    }
}