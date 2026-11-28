package com.ecommerce_pedidos.modelo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class ClienteTest {

    private Cliente ana;

    private static Cliente criarCliente(String cpf, String email) {
        return new Cliente("Ana Souza", cpf, email, "11999990000",
                "Rua das Flores", "Centro", 100, "São Carlos", "SP", "Brasil");
    }

    @BeforeEach
    void prepararCenario() {
        ana = criarCliente("12345678900", "ana@exemplo.com");
    }

    // ---------- Caminho feliz ----------

    @Test
    @DisplayName("Deve criar cliente quando os dados são válidos")
    void deveCriarClienteQuandoDadosValidos() {
        assertEquals("Ana Souza", ana.getNome());
        assertEquals("12345678900", ana.getCpf());
        assertEquals("12345678900", ana.getDocumento());
        assertEquals("ana@exemplo.com", ana.getEmail());
        assertEquals("São Carlos", ana.getCidade());
    }

    @Test
    @DisplayName("Deve identificar o cliente pelo nome e CPF")
    void deveIdentificarClientePeloNomeECpf() {
        assertEquals(
            "Ana Souza (CPF 12345678900)",
            ana.getIdentificacao()
        );
    }

    @Test
    @DisplayName("Deve alterar o e-mail quando o novo e-mail é válido")
    void deveAlterarEmailQuandoNovoEmailValido() {
        ana.setEmail("  nova@exemplo.com ");
        assertEquals("nova@exemplo.com", ana.getEmail());
    }

    @Test
    @DisplayName("Deve alterar o CPF quando o novo CPF tem 11 dígitos")
    void deveAlterarCpfQuandoNovoCpfValido() {
        ana.setCpf("98765432100");
        assertEquals("98765432100", ana.getCpf());
    }

    @Test
    @DisplayName("Deve remover espaços do CPF quando informado com espaços")
    void deveRemoverEspacosDoCpf() {
        Cliente c = criarCliente(
            " 12345678900 ", "ana@exemplo.com"
        );

        assertEquals("12345678900", c.getCpf());
    }

    @Test
    @DisplayName("Deve alterar o CPF pelo método setDocumento")
    void deveAlterarCpfPeloSetDocumento() {
        ana.setDocumento("98765432100");

        assertAll(
            () -> assertEquals("98765432100", ana.getCpf()),
            () -> assertEquals("98765432100", ana.getDocumento())
        );
    }

    // ---------- Exceções ----------

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "semarroba"})
    @DisplayName("Deve recusar e-mail inválido e manter o anterior")
    void deveRecusarEmailInvalidoEManterAnterior(String email) {
        assertThrows(
            IllegalArgumentException.class,
            () -> ana.setEmail(email)
        );

        assertEquals("ana@exemplo.com", ana.getEmail());
    }

    @Test
    @DisplayName("Deve recusar cliente quando o e-mail não tem arroba")
    void deveRecusarClienteQuandoEmailSemArroba() {
        assertThrows(
            IllegalArgumentException.class,
            () -> criarCliente("12345678900", "email-sem-arroba")
        );
    }

    @Test
    @DisplayName("Deve recusar cliente quando o CPF tem letras")
    void deveRecusarClienteQuandoCpfTemLetras() {
        assertThrows(
            IllegalArgumentException.class,
            () -> criarCliente("abc123", "ana@exemplo.com")
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"123", "1234567890", "123456789012"})
    @DisplayName("Deve recusar cliente quando o CPF não tem 11 dígitos")
    void deveRecusarClienteQuandoCpfNaoTem11Digitos(String cpf) {
        assertThrows(
            IllegalArgumentException.class,
            () -> criarCliente(cpf, "ana@exemplo.com")
        );
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    @DisplayName("Deve recusar cliente quando o CPF está vazio")
    void deveRecusarClienteQuandoCpfVazio(String cpf) {
        assertThrows(
            IllegalArgumentException.class,
            () -> criarCliente(cpf, "ana@exemplo.com")
        );
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    @DisplayName("Deve recusar cliente quando o nome está vazio")
    void deveRecusarClienteQuandoNomeVazio(String nome) {
        assertThrows(
            IllegalArgumentException.class,
            () -> new Cliente(
                nome, "12345678900", "ana@exemplo.com",
                null, null, null, null, null, null, null
            )
        );
    }

    @Test
    @DisplayName("Deve manter o CPF original quando o novo CPF é inválido")
    void deveManterCpfOriginalQuandoNovoCpfInvalido() {
        assertThrows(
            IllegalArgumentException.class,
            () -> ana.setCpf("123")
        );

        assertEquals("12345678900", ana.getCpf());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {
        "   ",
        "123",
        "1234567890",
        "123456789012",
        "1234567890A"
    })
    @DisplayName("Deve recusar documento inválido pelo setDocumento")
    void deveRecusarDocumentoInvalido(String documento) {
        assertThrows(
            IllegalArgumentException.class,
            () -> ana.setDocumento(documento)
        );

        assertAll(
            () -> assertEquals("12345678900", ana.getCpf()),
            () -> assertEquals("12345678900", ana.getDocumento())
        );
    }

    @Test
    @DisplayName("Deve manter a validação do CPF através da classe Pessoa")
    void deveValidarCpfAtravesDaClassePessoa() {
        Pessoa pessoa = ana;

        assertThrows(
            IllegalArgumentException.class,
            () -> pessoa.setDocumento("123")
        );

        assertEquals("12345678900", pessoa.getDocumento());
    }
}