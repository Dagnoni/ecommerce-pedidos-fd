package ecommerce_pedidos.git.modelo;

public class Cliente {

// Atributos privados

    private String nome;
    private String cpf;
    private String email;
    private String telefone;
    private String rua;
    private String bairro;
    private Integer numeroCasa;
    private String cidade;
    private String estado;
    private String paisNacao;

    

// Construtor
    public Cliente( String nome, String cpf, String email, String telefone, String rua, String bairro, Integer numeroCasa, String cidade, String estado, String paisNacao){
    
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.telefone = telefone;
        this.rua = rua;
        this.bairro = bairro;
        this.numeroCasa = numeroCasa;
        this.cidade = cidade;
        this.estado = estado;
        this.paisNacao = paisNacao;
    }

    // Getters e Setters

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
}
