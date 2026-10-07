package com.ecommerce_pedidos.excecao;
import com.senai.ecommerce.modelo.Pedido;

public class PedidoInvalidoException extends ECommerceException {

    public PedidoInvalidoException(String mensagem) {
        super(mensagem);
    }
    public PedidoInvalidoException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }

}