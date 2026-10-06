package com.ecommerce_pedidos.excecao;

public class ECommerceException extends Exception {

    private final String motivo;

    public ECommerceException(String mensagem) {
        this(mensagem, mensagem, null);
    }

    // se houver causa, o motivo é a mensagem da causa
    public ECommerceException(String mensagem, Throwable causa) {
        this(mensagem,
             (causa != null && causa.getMessage() != null) ? causa.getMessage() : mensagem,
             causa);
    }

    public ECommerceException(String mensagem, String motivo, Throwable causa) {
        super(mensagem, causa);
        this.motivo = motivo;
    }

    public String getMotivo() {
        return motivo;
    }
}
