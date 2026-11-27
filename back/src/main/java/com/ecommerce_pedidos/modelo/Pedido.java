package com.ecommerce_pedidos.modelo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.ecommerce_pedidos.excecao.EstoqueInsuficienteException;
import com.ecommerce_pedidos.modelo.pagamento.ProcessadorPagamento;

public class Pedido {

    private final String numero;
    private final Cliente cliente; // associação 1 para 1, obrigatória
    private final List<ItemPedido> itens = new ArrayList<>(); // composição 1 para 1..*
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

    /**
     * Versão completa: valida estoque, decide quando o item nasce e
     * recusa duplicar o mesmo produto no pedido (decisão da equipe —
     * registrar no README: aqui optamos por recusar, não somar quantidades).
     */
    public void adicionarItem(Produto produto, int quantidade) {
        if (produto == null) {
            throw new IllegalArgumentException("Produto é obrigatório");
        }
        if (!produto.temEstoqueDisponivel(quantidade)) {
            throw new IllegalStateException("Estoque insuficiente: " + produto.getNome());
        }
        for (ItemPedido item : itens) {
            if (item.getProduto() == produto) {
                throw new IllegalStateException(
                    "Produto já está no pedido: " + produto.getNome() + ". Ajuste a quantidade em vez de adicionar de novo.");
            }
        }
        itens.add(new ItemPedido(produto, quantidade, produto.getPreco()));
    }

    /**
     * Sobrecarga (Aula 08): versão de conveniência para quantidade 1.
     * Delega à versão completa — nunca duplica a regra.
     */
    public void adicionarItem(Produto produto) {
        adicionarItem(produto, 1);
    }

    public List<ItemPedido> getItens() {
        return Collections.unmodifiableList(itens);
    }

    public BigDecimal calcularValorTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (ItemPedido item : itens) {
            total = total.add(item.calcularSubtotal());
        }
        return total;
    }

    /**
     * O Pedido conhece só o contrato ProcessadorPagamento — nenhuma classe
     * concreta é citada aqui. Zero if de tipo: quem decide como processar
     * é o próprio processador escolhido pelo chamador.
     */
    public boolean pagar(ProcessadorPagamento processador) {
        if (processador == null) {
            throw new IllegalArgumentException("Forma de pagamento é obrigatória");
        }
        if (itens.isEmpty()) {
            throw new IllegalStateException("Pedido sem itens não pode ser pago");
        }
        if (situacao == SituacaoDoPedido.PAGO) {
            // Decisão da equipe (registrar no README): pedido já pago recusa nova cobrança.
            throw new IllegalStateException("Pedido já está pago");
        }
        // Regra descoberta pelo teste de integração (Aula 11): adicionar o item NÃO
        // reserva estoque; ele só baixa quando o pagamento é aprovado. Por isso,
        // antes de cobrar, confere se o estoque ainda existe — senão o cliente
        // seria cobrado por algo que já não há para entregar.
        for (ItemPedido item : itens) {
            if (!item.getProduto().temEstoqueDisponivel(item.getQuantidade())) {
                throw new IllegalStateException("Estoque insuficiente: " + item.getProduto().getNome());
            }
        }
        boolean aprovado = processador.processar(calcularValorTotal());
        if (aprovado) {
            for (ItemPedido item : itens) {
                try {
                    item.getProduto().baixarEstoque(item.getQuantidade());
                } catch (EstoqueInsuficienteException e) {
                    // o estoque foi conferido antes de cobrar; se chegou aqui, o estado ficou inconsistente
                    throw new IllegalStateException("Estoque insuficiente: " + item.getProduto().getNome(), e);
                }
            }
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
