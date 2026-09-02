package ecommerce_pedidos.git.modelo;

public class itemPedido {
    // 1. Atributos privados
    private String produto;
    private int quantidade;
    private double precoPraticado;

    // 2. Construtor (utiliza os setters para reaproveitar as validações)
    public itemPedido(String produto, int quantidade, double precoPraticado) {
        setProduto(produto);
        setQuantidade(quantidade);
        setPrecoPraticado(precoPraticado);
    }

    // 3. Getters (Lógica de leitura)
    public String getProduto() {
        return this.produto;
    }

    public int getQuantidade() {
        return this.quantidade;
    }

    public double getPrecoPraticado() {
        return this.precoPraticado;
    }

    // 4. Setters (Lógica de escrita com validações de regras de negócio)
    public void setProduto(String produto) {
        if (produto == null || produto.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do produto não pode ser vazio.");
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

    // 5. Método de negócio (pergunta que o objeto responde sobre si mesmo)
    public double calcularSubtotal() {
        return this.quantidade * this.precoPraticado;
    }

    // 6. toString sobrescrito
    @Override
    public String toString() {
        return String.format("ItemPedido [Produto: %s, Quantidade: %d, Preço Praticado: R$ %.2f, Subtotal: R$ %.2f]",
                produto, quantidade, precoPraticado, calcularSubtotal());
    }

}
