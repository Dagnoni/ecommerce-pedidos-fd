package com.ecommerce_pedidos.utils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.ecommerce_pedidos.modelo.Cliente;
import com.ecommerce_pedidos.modelo.Pedido;
import com.ecommerce_pedidos.modelo.Produto;
import com.ecommerce_pedidos.modelo.pagamento.Boleto;
import com.ecommerce_pedidos.modelo.pagamento.CartaoCredito;
import com.ecommerce_pedidos.modelo.pagamento.Dinheiro;
import com.ecommerce_pedidos.modelo.pagamento.Estornavel;
import com.ecommerce_pedidos.modelo.pagamento.Pix;
import com.ecommerce_pedidos.modelo.pagamento.ProcessadorPagamento;

public class App {

    public static void main(String[] args) {

        // ---- Catálogo e cliente (Aulas 04 a 06) ----

        Produto teclado = new Produto(
            "TEC-001", "Teclado", new BigDecimal("150.00"), 8
        );

        Produto monitor = new Produto(
            "MON-001", "Monitor", new BigDecimal("899.90"), 76
        );

        Cliente cliente = new Cliente(
            "Ana Souza", "12345678900", "ana@exemplo.com",
            "11999990000", "Rua das Flores", "Centro", 100,
            "São Carlos", "SP", "Brasil"
        );

        System.out.println(cliente.getIdentificacao());

        // ---- Laço polimórfico ----

        BigDecimal valorCompra = new BigDecimal("150.00");

        List<ProcessadorPagamento> formas = List.of(
            new Pix(valorCompra, "ana@exemplo.com"),

            new Boleto(
                valorCompra,
                "34191-79001",
                LocalDate.now().plusDays(5)
            ),

            new CartaoCredito(
                valorCompra,
                "4111111111111111",
                3
            )
        );

        System.out.println("\n--- Laço polimórfico ---");

        for (ProcessadorPagamento forma : formas) {

            System.out.println(forma.getResumoPagamento());

            boolean ok = forma.processar(valorCompra);

            System.out.println(
                ok ? "Aprovado: " + forma.getComprovante()
                   : "Aguardando compensação"
            );
        }

        // ---- Princípio aberto/fechado ----

        Dinheiro dinheiro = new Dinheiro(new BigDecimal("200.00"));

        System.out.println("\n--- Quarta forma de pagamento ---");
        System.out.println(dinheiro.getResumoPagamento());

        if (dinheiro.processar(valorCompra)) {
            System.out.println("Aprovado: " + dinheiro.getComprovante());
            System.out.println("Troco: R$ " + dinheiro.calcularTroco(valorCompra));
        } else {
            System.out.println("Pagamento recusado");
        }

        // ---- Fluxo de um pedido ----

        System.out.println("\n--- Fluxo do Pedido ---");

        Pedido pedido = new Pedido(cliente);

        pedido.adicionarItem(teclado, 2);
        pedido.adicionarItem(monitor);

        System.out.println(pedido);
        System.out.println("Total: R$ " + pedido.calcularValorTotal());

        Pix pix = new Pix(
            pedido.calcularValorTotal(),
            "ana@exemplo.com"
        );

        boolean pago = pedido.pagar(pix);

        System.out.println(
            "Pagar com Pix -> aprovado: " + pago + " | " + pedido
        );

        // ---- Demonstração da interface Estornavel ----
        // A demonstração é separada do pedido pago.

        ProcessadorPagamento usado = new Pix(
            valorCompra, "ana@exemplo.com"
        );

        if (usado.processar(valorCompra)) {
            if (usado instanceof Estornavel estornavel) {
                estornavel.estornar();
            }
        }

        // ---- Testes funcionais ----

        System.out.println("\n--- Testes funcionais ---");

        Pedido pedidoBoleto = new Pedido(cliente);

        pedidoBoleto.adicionarItem(monitor, 1);

        boolean aprovadoBoleto = pedidoBoleto.pagar(
            new Boleto(
                pedidoBoleto.calcularValorTotal(),
                "34191-79002",
                LocalDate.now().plusDays(3)
            )
        );

        System.out.println(
            "Pagar com Boleto -> aprovado: " + aprovadoBoleto
            + " (esperado false) | " + pedidoBoleto.getSituacao()
        );

        // Validação do parcelamento

        try {
            new CartaoCredito(
                new BigDecimal("100.00"),
                "4111111111111111",
                15
            );

            System.out.println("FALHOU: aceitou 15 parcelas");

        } catch (IllegalArgumentException e) {
            System.out.println(
                "OK: recusou parcelamento inválido -> " + e.getMessage()
            );
        }

        // Validação de pedido vazio

        try {
            Pedido pedidoVazio = new Pedido(cliente);
            pedidoVazio.pagar(pix);

            System.out.println("FALHOU: aceitou pagamento de pedido vazio");

        } catch (IllegalStateException e) {
            System.out.println(
                "OK: recusou pagamento de pedido sem itens -> "
                + e.getMessage()
            );
        }

        // Validação de processador nulo

        try {
            pedidoBoleto.pagar(null);

            System.out.println("FALHOU: aceitou processador null");

        } catch (IllegalArgumentException e) {
            System.out.println(
                "OK: recusou processador null -> " + e.getMessage()
            );
        }

        // Validação de pagamento duplicado

        try {
            pedido.pagar(
                new CartaoCredito(
                    pedido.calcularValorTotal(),
                    "4111111111111111",
                    2
                )
            );

            System.out.println("FALHOU: pagou um pedido que já estava pago");

        } catch (IllegalStateException e) {
            System.out.println(
                "OK: recusou pagar pedido já pago -> " + e.getMessage()
            );
        }

        // ---- Testes de integridade (Aula 07) ----

        try {
            new Pedido("PED-0004", null);

            System.out.println("FALHOU: aceitou pedido sem cliente");

        } catch (IllegalArgumentException e) {
            System.out.println(
                "OK: recusou pedido sem cliente -> " + e.getMessage()
            );
        }

        // Pedido aberto para testar as validações dos itens

        Pedido pedidoTeste = new Pedido(cliente);

        try {
            pedidoTeste.adicionarItem(null, 1);

            System.out.println("FALHOU: aceitou item sem produto");

        } catch (IllegalArgumentException e) {
            System.out.println(
                "OK: recusou item sem produto -> " + e.getMessage()
            );
        }

        // Validação de estoque

        try {
            pedidoTeste.adicionarItem(teclado, 1000);

            System.out.println("FALHOU: aceitou quantidade maior que o estoque");

        } catch (IllegalStateException e) {
            System.out.println(
                "OK: recusou estoque insuficiente -> " + e.getMessage()
            );
        }

        // Proteção da lista de itens

        try {
            pedido.getItens().clear();

            System.out.println("FALHOU: permitiu limpar a lista de itens");

        } catch (UnsupportedOperationException e) {
            System.out.println(
                "OK: lista de itens protegida contra alteração externa"
            );
        }

        // Proteção de pedidos pagos

        try {
            pedido.adicionarItem(teclado, 1);

            System.out.println("FALHOU: permitiu alterar pedido pago");

        } catch (IllegalStateException e) {
            System.out.println(
                "OK: recusou alteração de pedido pago -> " + e.getMessage()
            );
        }

        // Validação de valor divergente no pagamento

        try {
            Pix pixInvalido = new Pix(
                new BigDecimal("100.00"),
                "ana@exemplo.com"
            );

            pixInvalido.processar(valorCompra);

            System.out.println("FALHOU: aceitou pagamento com valor diferente");

        } catch (IllegalArgumentException e) {
            System.out.println(
                "OK: recusou valor divergente -> " + e.getMessage()
            );
        }
    }
}