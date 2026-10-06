package com.ecommerce_pedidos.excecao;

import com.ecommerce_pedidos.modelo.Produto;

public class EstoqueInsuficienteException extends ECommerceException {
    private final Produto produto;
    private final int quantidadeSolicitada;

    public EstoqueInsuficienteException(Produto produto, int quantidade) {
        this(produto, quantidade, null);
    }

    public EstoqueInsuficienteException(Produto produto, int quantidade, Throwable causa) {
        super("Estoque insuficiente de " + produto.getNome()
                  + ": disponível " + produto.getQuantidadeEmEstoque() + ", solicitado " + quantidade,
              "disponível " + produto.getQuantidadeEmEstoque() + ", solicitado " + quantidade,
              causa);
        this.produto = produto;
        this.quantidadeSolicitada = quantidade;
    }

    public Produto getProduto() { return produto; }
    public int getQuantidadeSolicitada() { return quantidadeSolicitada; }
}