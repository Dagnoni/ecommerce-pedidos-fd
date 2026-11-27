package com.ecommerce_pedidos.modelo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.ecommerce_pedidos.utils.PedidosUtils;
import com.ecommerce_pedidos.excecao.EstoqueInsuficienteException;
import com.ecommerce_pedidos.excecao.PedidoInvalidoException;


public class Pedidos {

    private String numero;
    private Cliente cliente;
    private LocalDate data;
    private String situacao;
    private List<ItemPedido> itens;

    // gera numeroPedido o status do pedido e data de veci
    public Pedidos(Cliente cliente) {
        setCliente(cliente);
        this.numero = PedidosUtils.gerarNumeroDoPedido();
        this.data = LocalDate.now();
        this.situacao = "ABERTO";
        this.itens = new ArrayList<>();
    }

    public String getNumero() {
        return numero;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("Pedido precisa de um cliente");
        }
        this.cliente = cliente;
    }

    public LocalDate getData() {
        return data;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        if (situacao == null || situacao.isBlank()) {
            throw new IllegalArgumentException("Situação do pedido é obrigatória");
        }
        this.situacao = situacao;
    }

    public List<ItemPedido> getItens() {
        return itens;
    }

    //metodo de negocio: adiciona um item na lista do pedido
    public void adicionarItem(ItemPedido item) {
        if (item == null) {
            throw new IllegalArgumentException("Item não pode ser nulo");
        }
        itens.add(item);
    }

 

public void adicionarItem(Produto produto, int quantidade) throws PedidoInvalidoException {
    try {
        ItemPedido item = new ItemPedido(produto, quantidade, produto.getPreco());
        produto.baixarEstoque(quantidade);
        itens.add(item);
    } catch (EstoqueInsuficienteException | IllegalArgumentException e) {
        throw new PedidoInvalidoException("Não foi possível adicionar o item: " + e.getMessage(), e);
    }
}

    //metodo de negocio: soma o subtotal de cada item do pedido
    public BigDecimal calcularValorTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (ItemPedido item : itens) {
            total = total.add(item.calcularSubtotal());
        }
        return total;
    }

    @Override
    public String toString() {
        return String.format("Pedido %s [Cliente: %s, Data: %s, Situação: %s, Itens: %d, Total: R$ %s]",
                numero, cliente.getNome(), data, situacao, itens.size(), calcularValorTotal().toPlainString());
    }
}
