package com.ecommerce_pedidos.fluxo;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.ecommerce_pedidos.modelo.Cliente;
import com.ecommerce_pedidos.modelo.Funcionario;
import com.ecommerce_pedidos.modelo.Pessoa;
import com.ecommerce_pedidos.modelo.Produto;

/**
 * Fluxo de cadastro: produto e cliente entrando no sistema.
 * Objetos reais, sem dublês.
 */
class CadastroFluxoIT {

    @Test
    @DisplayName("Deve cadastrar produto e cliente e recuperar os dados normalizados")
    void deveCadastrarProdutoEClienteERecuperarOsDados() {
        Produto notebook = new Produto("  NTB-001 ", "  Notebook ", new BigDecimal("3000.00"), 5);
        Cliente ana = new Cliente("  Ana Souza ", "12345678900", "ana@email.com",
                "11999990000", "Rua das Flores", "Centro", 100, "São Carlos", "SP", "Brasil");

        assertAll(
            () -> assertEquals("NTB-001", notebook.getCodigo()),
            () -> assertEquals("Notebook", notebook.getNome()),
            () -> assertEquals(0, new BigDecimal("3000.00").compareTo(notebook.getPreco())),
            () -> assertEquals(5, notebook.getQuantidadeEmEstoque()),
            () -> assertTrue(notebook.isAtivo(), "todo produto nasce ativo"),
            () -> assertEquals("Ana Souza", ana.getNome()),
            () -> assertEquals("Ana Souza (CPF 12345678900)", ana.getIdentificacao()),
            () -> assertEquals("ana@email.com", ana.getEmail())
        );
    }

    @Test
    @DisplayName("Deve tratar Cliente e Funcionario como Pessoa, cada um com a sua identificação")
    void deveTratarClienteEFuncionarioComoPessoa() {
        Pessoa cliente = new Cliente("Ana", "12345678900", "ana@email.com",
                null, null, null, null, null, null, null);
        Pessoa funcionario = new Funcionario("Bruno", "98765432100", "F-001",
                "Analista", new BigDecimal("5500.00"));

        assertAll(
            () -> assertEquals("Ana (CPF 12345678900)", cliente.getIdentificacao()),
            () -> assertEquals("Bruno (Matrícula F-001)", funcionario.getIdentificacao())
        );
    }

    @Test
    @DisplayName("Deve recusar cadastros inválidos sem criar objeto pela metade")
    void deveRecusarCadastrosInvalidos() {
        assertAll(
            () -> assertThrows(IllegalArgumentException.class,
                    () -> new Produto("", "Mouse", new BigDecimal("50.00"), 1)),
            () -> assertThrows(IllegalArgumentException.class,
                    () -> new Produto("MOU-1", "Mouse", new BigDecimal("-1.00"), 1)),
            () -> assertThrows(IllegalArgumentException.class,
                    () -> new Produto("MOU-1", "Mouse", new BigDecimal("50.00"), -1)),
            () -> assertThrows(IllegalArgumentException.class,
                    () -> new Cliente("Ana", "abc", "ana@email.com",
                            null, null, null, null, null, null, null)),
            () -> assertThrows(IllegalArgumentException.class,
                    () -> new Cliente("Ana", "12345678900", "sem-arroba",
                            null, null, null, null, null, null, null))
        );
    }
}
