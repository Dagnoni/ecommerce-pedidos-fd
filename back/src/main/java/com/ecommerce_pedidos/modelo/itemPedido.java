package com.ecommerce_pedidos.modelo;

public class itemPedido {
    //Atributos privados
    private Produto produto; //Relacionamento direto de itemPedido com Produto
    private int quantidade;
    private double precoPraticado;

    // Construtor (utiliza os setters para reaproveitar as validações)
    public itemPedido(Produto produto, int quantidade, double precoPraticado) {
        setProduto(produto);
        setQuantidade(quantidade);
        setPrecoPraticado(precoPraticado);
    }

   
    public Produto getProduto() {
        return this.produto;
    }

    public int getQuantidade() {
        return this.quantidade;
    }

    public double getPrecoPraticado() {
        return this.precoPraticado;
    }

    //Setters
    public void setProduto(Produto produto) {
        if (produto == null) {
            throw new IllegalArgumentException("O Produto não pode ser nulo.");
        }
        this.produto = produto;
    }

    public void setQuantidade(int quantidade) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("A quantidade deve ser maior que zero.");
        }
        this.quantidade = quantidade;
    }

    public void setPrecoPraticado(double precoPraticado) {
        if (precoPraticado < 0) {
            throw new IllegalArgumentException("O preço não pode ser negativo.");
        }
        this.precoPraticado = precoPraticado;
    }

    // Método de negócio (pergunta que o objeto responde sobre si mesmo)
    public double calcularSubtotal() {
        return this.quantidade * this.precoPraticado;
    }

    //toString sobrescrito
    @Override
    public String toString() {
        return String.format("ItemPedido [Produto: %s, Quantidade: %d, Preço Praticado: R$ %.2f, Subtotal: R$ %.2f]",
                produto.getNome(), quantidade, precoPraticado, calcularSubtotal());
                //to string agora busca o nome dentro do
            }

}




//Preciso mudar Double para BigDecimal****