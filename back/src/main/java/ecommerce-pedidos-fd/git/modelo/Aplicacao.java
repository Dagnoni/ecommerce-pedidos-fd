package com.senai.ecommerce;

import com.senai.ecommerce.util.PedidoUtils;

public class Aplicacao {

    public static void main(String[] args) {
        System.out.println("=== TESTE DO MÓDULO DE PEDIDOS ===\n");

        // 1. Teste de geração de código de pedido
        String codigoPedido = PedidoUtils.gerarNumeroDoPedido();
        System.out.println("Código do Pedido Gerado: " + codigoPedido);

        // 2. Teste de Normalização de Nome
        String nomeClienteBruto = "   joAO   da  sILVA   ";
        String nomeNormalizado = PedidoUtils.normalizarNome(nomeClienteBruto);
        System.out.println("Nome Cliente Normalizado: '" + nomeNormalizado + "'\n");

        // 3. Dados para simulação do pedido
        String[] produtos = {"Teclado Mecânico", "Mouse Gamer", "Mousepad XL"};
        int[] quantidades = {1, 2, 1};
        double[] precios = {250.00, 80.00, 40.00};
        double pesoTotalKg = 2.3; // Será arredondado para 3kg
        double percentualDesconto = 15.0; // 15% de desconto

        // 4. Execução dos cálculos
        double subtotal = PedidoUtils.calcularSubtotal(precios, quantidades);
        double frete = PedidoUtils.calcularFrete(subtotal, pesoTotalKg);
        double desconto = PedidoUtils.calcularDesconto(subtotal, percentualDesconto);

        // 5. Impressão do Recibo Completo no Console
        String recibo = PedidoUtils.montarRecibo(produtos, quantidades, precios, frete, desconto);
        System.out.println(recibo);
    }
}