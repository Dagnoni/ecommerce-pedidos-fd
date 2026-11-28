package com.ecommerce_pedidos.modelo;

import java.math.BigDecimal;

public class ItemPedido {

    // 1. Atributos privados e imutáveis

    private final Produto produto;
    private final int quantidade;
    private final BigDecimal precoPraticado;

    // 2. Construtor com validações

    public ItemPedido(Produto produto, int quantidade, BigDecimal precoPraticado) {

        if (produto == null) {
            throw new IllegalArgumentException(
                "O produto não pode ser nulo."
            );
        }

        if (quantidade <= 0) {
            throw new IllegalArgumentException(
                "A quantidade deve ser maior que zero."
            );
        }

        if (precoPraticado == null || precoPraticado.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                "O preço não pode ser nulo ou negativo."
            );
        }

        this.produto = produto;
        this.quantidade = quantidade;
        this.precoPraticado = precoPraticado;
    }

    // 3. Getters

    public Produto getProduto() {
        return this.produto;
    }

    public int getQuantidade() {
        return this.quantidade;
    }

    public BigDecimal getPrecoPraticado() {
        return this.precoPraticado;
    }

    // 4. Método de negócio

    public BigDecimal calcularSubtotal() {
        return this.precoPraticado.multiply(
            BigDecimal.valueOf(this.quantidade)
        );
    }

    // 5. Sobrescrita do método toString

    @Override
    public String toString() {
        return String.format(
            "ItemPedido [Produto: %s, Quantidade: %d, Preço Praticado: R$ %s, Subtotal: R$ %s]",
            produto.getNome(),
            quantidade,
            precoPraticado.toPlainString(),
            calcularSubtotal().toPlainString()
        );
    }
}