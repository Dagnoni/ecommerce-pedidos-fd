package com.senai.ecommerce.util;

import java.time.LocalDate;
import java.util.Random;

/**
 * Classe utilitária responsável por centralizar os cálculos, 
 * validações e formatações do módulo de pedidos.
 */
public final class PedidoUtils {

    // Constantes nomeadas (Regras de Negócio / Evita Números Mágicos)
    private static final double TAXA_FRETE_POR_KG = 5.0;
    private static final double FRETE_MINIMO = 15.0;
    private static final double VALOR_PARA_FRETE_GRATIS = 200.0;

    private static final double TETO_DESCONTO_MAXIMO = 50.0;

    private static final Random RANDOM = new Random();

    // Construtor privado para evitar instanciação
    private PedidoUtils() {
        throw new UnsupportedOperationException("Classe utilitária não deve ser instanciada.");
    }

    /**
     * Gera o número do pedido no padrão PED-AAAA-NNNNN.
     * 
     * @return String formatada do código do pedido.
     */
    public static String gerarNumeroDoPedido() {
        int anoAtual = LocalDate.now().getYear();
        int numeroAleatorio = RANDOM.nextInt(100000); // Gera de 0 a 99999
        return String.format("PED-%d-%05d", anoAtual, numeroAleatorio);
    }

    /**
     * Calcula o valor subtotal somando (preço * quantidade) de cada item.
     * 
     * @param precios Vetor contendo o preço dos itens.
     * @param quantidades Vetor contendo a quantidade correspondente dos itens.
     * @return Valor total acumulado dos itens.
     */
    public static double calcularSubtotal(double[] precios, int[] quantidades) {
        if (precios == null || quantidades == null || precios.length == 0 || precios.length != quantidades.length) {
            return 0.0;
        }

        double subtotal = 0.0;
        for (int i = 0; i < precios.length; i++) {
            if (precios[i] < 0 || quantidades[i] < 0) {
                throw new IllegalArgumentException("Preço e quantidade não podem ser negativos.");
            }
            subtotal += precios[i] * quantidades[i];
        }
        return subtotal;
    }

    /**
     * Calcula o frete baseado no peso total, respeitando o frete mínimo 
     * e isenção acima do valor estipulado.
     * 
     * @param subtotal Valor subtotal do pedido.
     * @param pesoEmKg Peso total dos produtos em quilos.
     * @return Valor do frete calculado.
     */
    public static double calcularFrete(double subtotal, double pesoEmKg) {
        if (pesoEmKg < 0 || subtotal < 0) {
            throw new IllegalArgumentException("Valor do subtotal ou peso inválido.");
        }
        if (subtotal >= VALOR_PARA_FRETE_GRATIS || pesoEmKg == 0) {
            return 0.0;
        }

        // Cobra por quilo iniciado (arredonda pra cima)
        double quilosArredondados = Math.ceil(pesoEmKg);
        double freteCalculado = quilosArredondados * TAXA_FRETE_POR_KG;

        // Respeita o frete mínimo
        return Math.max(freteCalculado, FRETE_MINIMO);
    }

    /**
     * Aplica uma taxa percentual e respeita o teto máximo de desconto.
     * 
     * @param subtotal Valor subtotal do pedido.
     * @param percentualDesconto Porcentagem do desconto (ex: 10 para 10%).
     * @return Valor em reais a ser descontado (limitado ao teto).
     */
    public static double calcularDesconto(double subtotal, double percentualDesconto) {
        if (subtotal < 0 || percentualDesconto < 0) {
            throw new IllegalArgumentException("Subtotal ou percentual de desconto inválidos.");
        }

        double descontoBruto = subtotal * (percentualDesconto / 100.0);
        
        // Respeita o teto do desconto
        return Math.min(descontoBruto, TETO_DESCONTO_MAXIMO);
    }

    /**
     * Formata uma linha do recibo alinhada em colunas.
     * 
     * @param item Nome/Descrição do produto.
     * @param qtd Quantidade adquirida.
     * @param precoUnitario Preço unitário do produto.
     * @return String formatada com colunas.
     */
    public static String formatarLinhaDoRecibo(String item, int qtd, double precoUnitario) {
        double totalItem = qtd * precoUnitario;
        return String.format("%-20s %3d x %8.2f = %8.2f", item, qtd, precoUnitario, totalItem);
    }

    /**
     * Monta a estrutura completa do recibo usando StringBuilder.
     * 
     * @param itens Nomes dos produtos.
     * @param quantidades Quantidades dos produtos.
     * @param precios Preços unitários dos produtos.
     * @param frete Valor do frete.
     * @param desconto Valor do desconto.
     * @return Texto formatado do recibo completo.
     */
    public static String montarRecibo(String[] itens, int[] quantidades, double[] precios, double frete, double desconto) {
        if (itens == null || quantidades == null || precios == null) {
            return "Dados de entrada inválidos para geração do recibo.";
        }

        StringBuilder sb = new StringBuilder();
        double subtotal = calcularSubtotal(precios, quantidades);
        double totalGeral = subtotal + frete - desconto;

        sb.append("==============================================\n");
        sb.append("              RECIBO DO PEDIDO                \n");
        sb.append("==============================================\n");

        for (int i = 0; i < itens.length; i++) {
            sb.append(formatarLinhaDoRecibo(itens[i], quantidades[i], precios[i])).append("\n");
        }

        sb.append("----------------------------------------------\n");
        sb.append(String.format("%-33s: R$ %8.2f\n", "Subtotal", subtotal));
        sb.append(String.format("%-33s: R$ %8.2f\n", "Frete", frete));
        sb.append(String.format("%-33s: R$ %8.2f\n", "Desconto", desconto));
        sb.append("----------------------------------------------\n");
        sb.append(String.format("%-33s: R$ %8.2f\n", "TOTAL A PAGAR", totalGeral));
        sb.append("==============================================\n");

        return sb.toString();
    }

    /**
     * Normaliza o nome do cliente removendo espaços extras e padronizando caixa alta/baixa.
     * 
     * @param nome Nome do cliente.
     * @return Nome limpo e normalizado.
     */
    public static String normalizarNome(String nome) {
        if (nome == null || nome.isBlank()) {
            return "";
        }
        
        String[] partes = nome.trim().replaceAll("\\s+", " ").toLowerCase().split(" ");
        StringBuilder nomeNormalizado = new StringBuilder();

        for (String parte : partes) {
            if (!parte.isEmpty()) {
                nomeNormalizado.append(Character.toUpperCase(parte.charAt(0)))
                               .append(parte.substring(1))
                               .append(" ");
            }
        }

        return nomeNormalizado.toString().trim();
    }
}