package ecommerce_pedidos.git.modelo;


public class Cliente extends Pessoa {

    private String email;
    private String telefone;
    private String rua;
    private String bairro;
    private Integer numeroCasa;
    private String cidade;
    private String estado;
    private String paisNacao;

    public Cliente(String nome, String cpf, String email, String telefone, String rua,
                    String bairro, Integer numeroCasa, String cidade, String estado, String paisNacao) {
        super(nome, cpf); 
        setEmail(email);
        this.telefone = telefone;
        this.rua = rua;
        this.bairro = bairro;
        this.numeroCasa = numeroCasa;
        this.cidade = cidade;
        this.estado = estado;
        this.paisNacao = paisNacao;
    }

    @Override
    public String getIdentificacao() {
        return getNome() + " (CPF " + getDocumento() + ")";
    }

    public String getCpf() {
        return getDocumento();
    }

    public void setCpf(String cpf) {
        setDocumento(cpf);
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null || email.isBlank() || !email.contains("@")) {
            throw new IllegalArgumentException("E-mail inválido: " + email);
        }
        this.email = email.trim();
    }


    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public Integer getNumeroCasa() {
        return numeroCasa;
    }

    public void setNumeroCasa(Integer numeroCasa) {
        this.numeroCasa = numeroCasa;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getPaisNacao() {
        return paisNacao;
    }

    public void setPaisNacao(String paisNacao) {
        this.paisNacao = paisNacao;
    }

    @Override
    public String toString() {
        return String.format("%s - %s", getIdentificacao(), email);
    }
}
