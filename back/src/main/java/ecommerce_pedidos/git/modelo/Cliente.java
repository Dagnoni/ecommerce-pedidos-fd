package ecommerce-pedidos-fd.git.modelo;

public class Cliente {


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

    
}

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
