package com.ecommerce_pedidos.modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.ecommerce_pedidos.utils.PedidosUtils;

public class Pedidos {

    private String numero;
    private Cliente cliente;
    private LocalDate data;
    private String situacao;
    private List<itemPedido> itens;

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

    public List<itemPedido> getItens() {
        return itens;
    }

    //metodo de negocio: adiciona um item na lista do pedido
    public void adicionarItem(itemPedido item) {
        if (item == null) {
            throw new IllegalArgumentException("Item não pode ser nulo");
        }
        itens.add(item);
    }

    //metodo de negocio: soma o subtotal de cada item do pedido
    public double calcularValorTotal() {
        double total = 0.0;
        for (itemPedido item : itens) {
            total = total + item.calcularSubtotal();
        }
        return total;
    }

    @Override
    public String toString() {
        return String.format("Pedido %s [Cliente: %s, Data: %s, Situação: %s, Itens: %d, Total: R$ %.2f]",
                numero, cliente.getNome(), data, situacao, itens.size(), calcularValorTotal());
    }
}
