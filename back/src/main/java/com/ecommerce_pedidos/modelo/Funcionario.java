package com.ecommerce_pedidos.modelo;

import java.math.BigDecimal;

public class Funcionario extends Pessoa {

    private String matricula;
    private String cargo;
    private BigDecimal salario;

    public Funcionario(String nome, String cpf, String matricula, String cargo, BigDecimal salario) {
        super(nome, cpf);
        setMatricula(matricula);
        setCargo(cargo);
        setSalario(salario);
    }

    @Override
    public String getIdentificacao() {
        return getNome() + " (Matrícula " + matricula + ")";
    }

    public String getMatricula() {
        return matricula;
    }

    private void setMatricula(String matricula) {
        if (matricula == null || matricula.isBlank()) {
            throw new IllegalArgumentException("Matrícula é obrigatória");
        }
        this.matricula = matricula.trim();
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        if (cargo == null || cargo.isBlank()) {
            throw new IllegalArgumentException("Cargo é obrigatório");
        }
        this.cargo = cargo.trim();
    }

    public BigDecimal getSalario() {
        return salario;
    }

    public void setSalario(BigDecimal salario) {
        if (salario == null || salario.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Salário não pode ser negativo: " + salario);
        }
        this.salario = salario;
    }

    @Override
    public String toString() {
        return String.format("%s - %s (R$ %s)", getIdentificacao(), cargo, salario.toPlainString());
    }
}