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
        Produto teclado = new Produto("TEC-001", "Teclado", new BigDecimal("150.00"), 8);
        Produto monitor = new Produto("MON-001", "Monitor", new BigDecimal("899.90"), 76);
        Cliente cliente = new Cliente(
            "Ana Souza", "12345678900", "ana@exemplo.com",
            "11999990000", "Rua das Flores", "Centro", 100,
            "São Carlos", "SP", "Brasil"
        );
        System.out.println(cliente.getIdentificacao());

        // ---- Laço polimórfico (Passo 6 do roteiro): uma chamada, comportamentos diferentes ----
        List<ProcessadorPagamento> formas = List.of(
            new Pix(new BigDecimal("150.00"), "ana@exemplo.com"),
            new Boleto(new BigDecimal("300.00"), "34191-79001", LocalDate.now().plusDays(5)),
            new CartaoCredito(new BigDecimal("899.90"), "4111111111111111", 3)
        );
        System.out.println("\n--- Laço polimórfico ---");
        for (ProcessadorPagamento forma : formas) {
            System.out.println(forma.getResumoPagamento());
            boolean ok = forma.processar(new BigDecimal("150.00"));
            System.out.println(ok ? "Aprovado: " + forma.getComprovante() : "Aguardando");
        }

        // ---- Passo 7: prova do princípio aberto/fechado ----
        // Dinheiro é a quarta forma. Nenhum arquivo acima precisou mudar para ela existir.
        ProcessadorPagamento dinheiro = new Dinheiro(new BigDecimal("200.00"));
        System.out.println("\n--- Quarta forma (prova de extensão) ---");
        System.out.println(dinheiro.getResumoPagamento());
        System.out.println(dinheiro.processar(new BigDecimal("150.00")) ? "Aprovado: " + dinheiro.getComprovante() : "Recusado");

        // ---- Fluxo real de um Pedido, usando o pagar() polimórfico ----
        System.out.println("\n--- Fluxo do Pedido ---");
        Pedido pedido = new Pedido("PED-0001", cliente);
        pedido.adicionarItem(teclado, 2);
        pedido.adicionarItem(monitor); // sobrecarga: quantidade 1 por padrão
        System.out.println(pedido);
        System.out.println("Total: R$ " + pedido.calcularValorTotal());

        Pix pix = new Pix(pedido.calcularValorTotal(), "ana@exemplo.com");
        boolean pago = pedido.pagar(pix); // Pedido não cita "Pix" em lugar nenhum do seu próprio código
        System.out.println("Pagar com Pix -> aprovado: " + pago + " | " + pedido);

        // ---- Uso opcional do contrato Estornavel (capacidade, não decisão de tipo) ----
        ProcessadorPagamento usado = pix; // o Pedido só enxerga o contrato
        if (usado instanceof Estornavel estornavel) {
            estornavel.estornar();
        }

        // ---- Testes funcionais (Passo 5 do roteiro) ----
        System.out.println("\n--- Testes funcionais ---");

        Pedido pedidoBoleto = new Pedido("PED-0002", cliente);
        pedidoBoleto.adicionarItem(monitor, 1);
        boolean aprovadoBoleto = pedidoBoleto.pagar(new Boleto(monitor.getPreco(), "34191-79002", LocalDate.now().plusDays(3)));
        System.out.println("Pagar com Boleto -> aprovado: " + aprovadoBoleto + " (esperado false) | " + pedidoBoleto.getSituacao());

        try {
            new CartaoCredito(new BigDecimal("100.00"), "4111111111111111", 15);
            System.out.println("FALHOU: aceitou 15 parcelas");
        } catch (IllegalArgumentException e) {
            System.out.println("OK: recusou parcelamento inválido -> " + e.getMessage());
        }

        try {
            Pedido pedidoVazio = new Pedido("PED-0003", cliente);
            pedidoVazio.pagar(pix);
            System.out.println("FALHOU: aceitou pagamento de pedido vazio");
        } catch (IllegalStateException e) {
            System.out.println("OK: recusou pagamento de pedido sem itens -> " + e.getMessage());
        }

        try {
            pedido.pagar(null);
            System.out.println("FALHOU: aceitou processador null");
        } catch (IllegalArgumentException e) {
            System.out.println("OK: recusou processador null -> " + e.getMessage());
        }

        try {
            pedido.pagar(new CartaoCredito(pedido.calcularValorTotal(), "4111111111111111", 2));
            System.out.println("FALHOU: pagou um pedido que já estava pago");
        } catch (IllegalStateException e) {
            System.out.println("OK: recusou pagar pedido já pago -> " + e.getMessage());
        }

        // ---- Testes de integridade herdados da Aula 07 ----
        try {
            new Pedido("PED-0004", null);
            System.out.println("FALHOU: aceitou pedido sem cliente");
        } catch (IllegalArgumentException e) {
            System.out.println("OK: recusou pedido sem cliente -> " + e.getMessage());
        }

        try {
            pedido.adicionarItem(null, 1);
            System.out.println("FALHOU: aceitou item sem produto");
        } catch (IllegalArgumentException e) {
            System.out.println("OK: recusou item sem produto -> " + e.getMessage());
        }

        try {
            pedido.adicionarItem(teclado, 1000);
            System.out.println("FALHOU: aceitou quantidade maior que o estoque");
        } catch (IllegalStateException e) {
            System.out.println("OK: recusou estoque insuficiente -> " + e.getMessage());
        }

        try {
            pedido.getItens().clear();
            System.out.println("FALHOU: permitiu limpar a lista de itens por fora");
        } catch (UnsupportedOperationException e) {
            System.out.println("OK: lista de itens protegida contra alteração externa");
        }
    }
}
