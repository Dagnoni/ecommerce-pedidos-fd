package com.ecommerce_pedidos.modelo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.ecommerce_pedidos.utils.PedidosUtils;
import com.ecommerce_pedidos.excecao.EstoqueInsuficienteException;
import com.ecommerce_pedidos.modelo.pagamento.ProcessadorPagamento;

public class Pedido {

    private final String numero;
    private final LocalDate data;
    private final Cliente cliente;
    private final List<ItemPedido> itens = new ArrayList<>();

    private SituacaoDoPedido situacao = SituacaoDoPedido.ABERTO;
    private ProcessadorPagamento formaPagamento;
    private String comprovante;

    // Construtores

    public Pedido(String numero, Cliente cliente) {

        if (cliente == null) {
            throw new IllegalArgumentException(
                "Pedido exige um cliente"
            );
        }

        if (numero == null || numero.isBlank()) {
            throw new IllegalArgumentException(
                "Número do pedido é obrigatório"
            );
        }

        this.numero = numero.trim();
        this.cliente = cliente;
        this.data = LocalDate.now();
    }

    public Pedido(Cliente cliente) {
        this(PedidosUtils.gerarNumeroDoPedido(), cliente);
    }

    // Getters

    public String getNumero() {
        return numero;
    }

    public LocalDate getData() {
        return data;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public SituacaoDoPedido getSituacao() {
        return situacao;
    }

    public ProcessadorPagamento getFormaPagamento() {
        return formaPagamento;
    }

    public String getComprovante() {
        return comprovante;
    }

    public List<ItemPedido> getItens() {
        return Collections.unmodifiableList(itens);
    }

    // Adiciona um produto ao pedido

    public void adicionarItem(Produto produto, int quantidade) {

        if (situacao != SituacaoDoPedido.ABERTO) {
            throw new IllegalStateException(
                "Não é possível alterar um pedido que não está aberto"
            );
        }

        if (produto == null) {
            throw new IllegalArgumentException(
                "Produto é obrigatório"
            );
        }

        if (quantidade <= 0) {
            throw new IllegalArgumentException(
                "A quantidade deve ser maior que zero"
            );
        }

        if (!produto.temEstoqueDisponivel(quantidade)) {
            throw new IllegalStateException(
                "Estoque insuficiente ou produto indisponível: "
                + produto.getNome()
            );
        }

        // Verifica produtos duplicados pelo código

        for (ItemPedido item : itens) {
            if (item.getProduto().getCodigo()
                    .equals(produto.getCodigo())) {

                throw new IllegalStateException(
                    "Produto já está no pedido: " + produto.getNome()
                );
            }
        }

        itens.add(
            new ItemPedido(produto, quantidade, produto.getPreco())
        );
    }

    // Sobrecarga: adiciona uma unidade por padrão

    public void adicionarItem(Produto produto) {
        adicionarItem(produto, 1);
    }

    // Calcula o valor total do pedido

    public BigDecimal calcularValorTotal() {

        BigDecimal total = BigDecimal.ZERO;

        for (ItemPedido item : itens) {
            total = total.add(item.calcularSubtotal());
        }

        return total;
    }

    // Processamento polimórfico do pagamento

    public boolean pagar(ProcessadorPagamento processador) {

        if (processador == null) {
            throw new IllegalArgumentException(
                "Forma de pagamento é obrigatória"
            );
        }

        if (itens.isEmpty()) {
            throw new IllegalStateException(
                "Pedido sem itens não pode ser pago"
            );
        }

        if (situacao != SituacaoDoPedido.ABERTO) {
            throw new IllegalStateException(
                "Somente pedidos abertos podem ser pagos"
            );
        }

        // Verifica o estoque antes do pagamento

        for (ItemPedido item : itens) {

            if (!item.getProduto()
                    .temEstoqueDisponivel(item.getQuantidade())) {

                throw new IllegalStateException(
                    "Estoque insuficiente: "
                    + item.getProduto().getNome()
                );
            }
        }

        // Processa o pagamento utilizando polimorfismo

        boolean aprovado = processador.processar(
            calcularValorTotal()
        );

        if (aprovado) {

            // Baixa o estoque somente após a aprovação

            for (ItemPedido item : itens) {

                try {
                    item.getProduto().baixarEstoque(
                        item.getQuantidade()
                    );

                } catch (EstoqueInsuficienteException e) {

                    throw new IllegalStateException(
                        "Estoque insuficiente: "
                        + item.getProduto().getNome(), e
                    );
                }
            }

            this.situacao = SituacaoDoPedido.PAGO;
            this.formaPagamento = processador;
            this.comprovante = processador.getComprovante();
        }

        return aprovado;
    }

    // Representação textual do pedido

    @Override
    public String toString() {

        return String.format(
            "Pedido %s - Cliente: %s - Data: %s - "
            + "%d item(ns) - Total: R$ %s - Situação: %s",
            numero,
            cliente.getNome(),
            data,
            itens.size(),
            calcularValorTotal().toPlainString(),
            situacao
        );
    }
}