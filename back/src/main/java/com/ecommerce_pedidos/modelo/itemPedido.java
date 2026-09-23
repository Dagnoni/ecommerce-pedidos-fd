package com.ecommerce_pedidos.modelo;

import java.math.BigDecimal;

public class itemPedido {
    // 1. Atributos privados
    private Produto produto;
    private int quantidade;
    private BigDecimal precoPraticado;

    // 2. Construtor (utiliza os setters para reaproveitar as validações)
    public itemPedido(Produto produto, int quantidade, BigDecimal precoPraticado) {
        setProduto(produto);
        setQuantidade(quantidade);
        setPrecoPraticado(precoPraticado);
    }

    // 3. Getters (Lógica de leitura)
    public Produto getProduto() {
        return this.produto;
    }

    public int getQuantidade() {
        return this.quantidade;
    }

    public BigDecimal getPrecoPraticado() {
        return this.precoPraticado;
    }

    // 4. Setters (Lógica de escrita com validações de regras de negócio)
    public void setProduto(Produto produto) {
        if (produto == null) {
            throw new IllegalArgumentException("O produto não pode ser nulo.");
        }
        this.produto = produto;
    }

    public void setQuantidade(int quantidade) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("A quantidade deve ser maior que zero.");
        }
        this.quantidade = quantidade;
    }

    public void setPrecoPraticado(BigDecimal precoPraticado) {
        if (precoPraticado == null || precoPraticado.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("O preço não pode ser negativo.");
        }
        this.precoPraticado = precoPraticado;
    }

    // 5. Método de negócio (pergunta que o objeto responde sobre si mesmo)
    public BigDecimal calcularSubtotal() {
        return this.precoPraticado.multiply(BigDecimal.valueOf(this.quantidade));
    }

    // 6. toString sobrescrito
    @Override
    public String toString() {
        return String.format("ItemPedido [Produto: %s, Quantidade: %d, Preço Praticado: R$ %s, Subtotal: R$ %s]",
                produto.getNome(), quantidade, precoPraticado.toPlainString(), calcularSubtotal().toPlainString());
    }

}
