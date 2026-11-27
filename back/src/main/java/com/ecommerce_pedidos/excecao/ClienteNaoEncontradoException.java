package com.ecommerce_pedidos.excecao;

public class ClienteNaoEncontradoException extends ECommerceException {

    private final String documento;

    public ClienteNaoEncontradoException(String documento) {
        this(documento, null);
    }

    public ClienteNaoEncontradoException(String documento, Throwable causa) {
        super("Cliente não encontrado para o documento: " + documento,
              "nenhum cliente cadastrado com o documento " + documento,
              causa);
        this.documento = documento;
    }

    public String getDocumento() { return documento; }
}