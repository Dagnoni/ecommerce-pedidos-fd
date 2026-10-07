package com.ecommerce_pedidos.excecao;


public class PedidoInvalidoException extends ECommerceException {

    public PedidoInvalidoException(String mensagem) {
        super(mensagem);
    }
    public PedidoInvalidoException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }

}