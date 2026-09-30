package com.senai.ecommerce.excecao;
import com.senai.ecommerce.modelo.Cliente;

public class ClienteNaoEncontradoException extends ECommerceException {

    private final String documento;

    public ClienteNaoEncontradoException(String documento) {
        super("Cliente não encontrado para o documento: " + documento);
        this.documento = documento;
    }

    public String getDocumento() {
        return documento;
    }
}