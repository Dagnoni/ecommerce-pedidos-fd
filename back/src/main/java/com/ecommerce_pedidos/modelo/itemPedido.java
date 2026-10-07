package com.ecommerce_pedidos.modelo;

import java.math.BigDecimal;

public class itemPedido {
<<<<<<< HEAD
    // 1. Atributos privados
    private Produto produto;
=======
    //Atributos privados
    private Produto produto; //Relacionamento direto de itemPedido com Produto
>>>>>>> 070516358b824f0c1b9f8885c70100380fe771ed
    private int quantidade;
    private BigDecimal precoPraticado;

<<<<<<< HEAD
    // 2. Construtor (utiliza os setters para reaproveitar as validações)
    public itemPedido(Produto produto, int quantidade, BigDecimal precoPraticado) {
=======
    // Construtor (utiliza os setters para reaproveitar as validações)
    public itemPedido(Produto produto, int quantidade, double precoPraticado) {
>>>>>>> 070516358b824f0c1b9f8885c70100380fe771ed
        setProduto(produto);
        setQuantidade(quantidade);
        setPrecoPraticado(precoPraticado);
    }

<<<<<<< HEAD
    // 3. Getters (Lógica de leitura)
=======
   
>>>>>>> 070516358b824f0c1b9f8885c70100380fe771ed
    public Produto getProduto() {
        return this.produto;
    }

    public int getQuantidade() {
        return this.quantidade;
    }

    public BigDecimal getPrecoPraticado() {
        return this.precoPraticado;
    }

<<<<<<< HEAD
    // 4. Setters (Lógica de escrita com validações de regras de negócio)
    public void setProduto(Produto produto) {
        if (produto == null) {
            throw new IllegalArgumentException("O produto não pode ser nulo.");
=======
    //Setters
    public void setProduto(Produto produto) {
        if (produto == null) {
            throw new IllegalArgumentException("O Produto não pode ser nulo.");
>>>>>>> 070516358b824f0c1b9f8885c70100380fe771ed
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

<<<<<<< HEAD
    // 5. Método de negócio (pergunta que o objeto responde sobre si mesmo)
    public BigDecimal calcularSubtotal() {
        return this.precoPraticado.multiply(BigDecimal.valueOf(this.quantidade));
=======
    // Método de negócio (pergunta que o objeto responde sobre si mesmo)
    public double calcularSubtotal() {
        return this.quantidade * this.precoPraticado;
>>>>>>> 070516358b824f0c1b9f8885c70100380fe771ed
    }

    //toString sobrescrito
    @Override
    public String toString() {
<<<<<<< HEAD
        return String.format("ItemPedido [Produto: %s, Quantidade: %d, Preço Praticado: R$ %s, Subtotal: R$ %s]",
                produto.getNome(), quantidade, precoPraticado.toPlainString(), calcularSubtotal().toPlainString());
    }
=======
        return String.format("ItemPedido [Produto: %s, Quantidade: %d, Preço Praticado: R$ %.2f, Subtotal: R$ %.2f]",
                produto.getNome(), quantidade, precoPraticado, calcularSubtotal());
                //to string agora busca o nome dentro do
            }
>>>>>>> 070516358b824f0c1b9f8885c70100380fe771ed

}




//Preciso mudar Double para BigDecimal****