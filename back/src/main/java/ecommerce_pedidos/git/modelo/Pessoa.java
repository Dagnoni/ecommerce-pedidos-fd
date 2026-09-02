package ecommerce_pedidos.git.modelo;

public abstract class Pessoa {

    private String nome;
    private String documento;

    public Pessoa(String nome, String documento) {
        setNome(nome);
        setDocumento(documento);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome é obrigatório");
        }
        this.nome = nome.trim();
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        if (documento == null || documento.isBlank()) {
            throw new IllegalArgumentException("Documento é obrigatório");
        }
        String valor = documento.trim();
        if (!valor.matches("\\d+")) {
            throw new IllegalArgumentException("Documento deve conter apenas dígitos: " + documento);
        }
        this.documento = valor;
    }

    public abstract String getIdentificacao();
}
