package com.ecommerce.modelo;

public class Produto{

    private String codigo;
    private String nome;
    private String descricao;
    private Double preco;
    private Integer quantidadeEmEstoque;
    private Boolean ativo;

    public Produto(String codigo, String nome, Integer quantidadeEmEstoque){

        this.codigo = codigo;
        this.nome = nome;
        this.preco = preco;
        this.descricao = descricao;
        this.quantidadeEmEstoque = quantidadeEmEstoque;
        this.ativo = true;
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public boolean temEstoqueDisponivel() {
        return ativo && quantidadeEmEstoque >= quantidadeDesejada;
    }

    @Override
    public String toString(){
        return String.format("[%s] %s - R$ %.2f (%d em estoque)", 
        this.codigo, this.nome, this.preco, this.quantidadeEmEstoque);
    }

    public void baixarEstoque(int quantidade) {
        this.quantidadeEmEstoque = quantidadeEmEstoque - quantidade;
    }
}