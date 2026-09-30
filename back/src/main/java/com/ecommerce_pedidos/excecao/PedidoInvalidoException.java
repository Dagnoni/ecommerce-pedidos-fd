package com.senai.ecommerce.excecao;
import com.senai.ecommerce.modelo.Pedido;

public class PedidoInvalidoException extends ECommerceException {

    public PedidoInvalidoException(String mensagem) {
        super(mensagem);
    }
}