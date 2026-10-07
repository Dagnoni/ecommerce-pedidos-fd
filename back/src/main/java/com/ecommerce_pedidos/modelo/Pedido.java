package com.ecommerce_pedidos.modelo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.ecommerce_pedidos.modelo.pagamento.ProcessadorPagamento;

public class Pedido {

    private final String numero;
    private final Cliente cliente;
    private final List<itemPedido> itens = new ArrayList<>();
    private SituacaoDoPedido situacao = SituacaoDoPedido.ABERTO;
    private String comprovante;

    public Pedido(String numero, Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("Pedido exige um cliente");
        }
        this.numero = numero;
        this.cliente = cliente;
    }

    public String getNumero() {
        return numero;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public SituacaoDoPedido getSituacao() {
        return situacao;
    }

    public String getComprovante() {
        return comprovante;
    }

    public void adicionarItem(Produto produto, int quantidade) {
        if (produto == null) {
            throw new IllegalArgumentException("Produto é obrigatório");
        }
        if (quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser maior que zero");
        }
        if (!produto.temEstoqueDisponivel(quantidade)) {
            throw new IllegalStateException("Estoque insuficiente: " + produto.getNome());
        }

        for (itemPedido item : itens) {
            if (item.getProduto() == produto) {
                throw new IllegalStateException(
                    "Produto já está no pedido: " + produto.getNome() + ". Ajuste a quantidade em vez de adicionar de novo.");
            }
        }

        itens.add(new itemPedido(produto, quantidade, produto.getPreco()));
    }

    public void adicionarItem(Produto produto) {
        adicionarItem(produto, 1);
    }

    public List<itemPedido> getItens() {
        return Collections.unmodifiableList(itens);
    }

    public BigDecimal calcularValorTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (itemPedido item : itens) {
            total = total.add(item.calcularSubtotal());
        }
        return total;
    }

    public boolean pagar(ProcessadorPagamento processador) {
        if (processador == null) {
            throw new IllegalArgumentException("Forma de pagamento é obrigatória");
        }
        if (itens.isEmpty()) {
            throw new IllegalStateException("Pedido sem itens não pode ser pago");
        }
        if (situacao == SituacaoDoPedido.PAGO) {
            throw new IllegalStateException("Pedido já está pago");
        }

        boolean aprovado = processador.processar(calcularValorTotal());
        if (aprovado) {
            this.situacao = SituacaoDoPedido.PAGO;
            this.comprovante = processador.getComprovante();
        }
        return aprovado;
    }

    @Override
    public String toString() {
        return String.format("Pedido %s - %s - %d item(ns) - Total: R$ %s - %s",
            numero, cliente.getNome(), itens.size(), calcularValorTotal().toPlainString(), situacao);
    }
}