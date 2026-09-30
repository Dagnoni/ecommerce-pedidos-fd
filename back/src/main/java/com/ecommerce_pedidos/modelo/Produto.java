package com.ecommerce_pedidos.modelo;

import java.math.BigDecimal;

public class Produto {

    private String codigo;
    private String nome;
    private String descricao;
    private BigDecimal preco;
    private Integer quantidadeEmEstoque;
    private Boolean ativo;

    public Produto(String codigo, String nome, BigDecimal preco, Integer quantidadeEmEstoque) {
    //  setCodigo(codigo);
        setNome(nome);
        setPreco(preco);
        setQuantidadeEmEstoque(quantidadeEmEstoque);
        this.ativo = true;
    }
    public String getCodigo() {
        return codigo;
    }

   //* private void setCodigo(String codigo) {
   //     if (codigo == null || codigo.isBlank()) {
   //         throw new IllegalArgumentException("Código é obrigatório");
   //    }
   //     this.codigo = codigo.trim();
   // }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome é obrigatório");
        }
        this.nome = nome.trim();
    }


    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }


    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        if (preco == null || preco.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Preço não pode ser negativo: " + preco);
        }
        this.preco = preco;
    }


    public Integer getQuantidadeEmEstoque() {
        return quantidadeEmEstoque;
    }

    public void setQuantidadeEmEstoque(Integer quantidadeEmEstoque) {
        if (quantidadeEmEstoque == null || quantidadeEmEstoque < 0) {
            throw new IllegalArgumentException("Estoque não pode ser negativo: " + quantidadeEmEstoque);
        }
        this.quantidadeEmEstoque = quantidadeEmEstoque;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void ativar() {
        this.ativo = true;
    }

    public void desativar() {
        this.ativo = false;
    }

    public boolean temEstoqueDisponivel(int quantidadeDesejada) {
        return ativo && quantidadeEmEstoque >= quantidadeDesejada;
    }

    public void baixarEstoque(int quantidade) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser positiva");
        }
        if (quantidade > this.quantidadeEmEstoque) {
            throw new EstoqueInsuficienteException(this, quantidade);
        }
        this.quantidadeEmEstoque -= quantidade;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s - R$ %s (%d em estoque)",
            this.codigo, this.nome, this.preco.toPlainString(), this.quantidadeEmEstoque);
    }
}

import com.senai.ecommerce.modelo.Produto;
public class EstoqueInsuficienteException extends ECommerceException {
    private final Produto produto;
    private final int quantidadeSolicitada;
    
    public EstoqueInsuficienteException(Produto produto, int quantidade) {
        super("Estoque insuficiente de " + produto.getNome()
            + ": disponível " + produto.getQuantidadeEmEstoque()
            + ", solicitado " + quantidade);
        this.produto = produto;
        this.quantidadeSolicitada = quantidade;
    }
    public Produto getProduto() {
        return produto;
    }
    public int getQuantidadeSolicitada() {
        return quantidadeSolicitada;
    }
}

// mudar Double para BigDecimal ****