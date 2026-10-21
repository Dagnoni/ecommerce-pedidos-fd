package com.ecommerce_pedidos.modelo;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Validações de Pessoa, Cliente, Funcionario, Produto e item de pedido, uma classe por vez. */
class RegrasDeCadastroTest {

    private Cliente umCliente() {
        return new Cliente("Ana", "12345678900", "ana@email.com", null, null, null, null, null, null, null);
    }

    @Test
    @DisplayName("Pessoa: nome e documento obrigatórios; documento só com dígitos")
    void pessoaDeveValidarNomeEDocumento() {
        assertAll(
            () -> assertThrows(IllegalArgumentException.class,
                    () -> new Cliente(" ", "123", "a@b.com", null, null, null, null, null, null, null)),
            () -> assertThrows(IllegalArgumentException.class,
                    () -> new Cliente("Ana", null, "a@b.com", null, null, null, null, null, null, null)),
            () -> assertThrows(IllegalArgumentException.class,
                    () -> new Cliente("Ana", " ", "a@b.com", null, null, null, null, null, null, null)),
            () -> assertThrows(IllegalArgumentException.class,
                    () -> new Cliente("Ana", "123.456", "a@b.com", null, null, null, null, null, null, null))
        );
    }

    @Test
    @DisplayName("Cliente: troca de CPF e e-mail passa pela mesma validação do construtor")
    void clienteDeveRevalidarAoAlterarCpfEEmail() {
        Cliente cliente = umCliente();
        cliente.setCpf("98765432100");
        cliente.setEmail("  nova@email.com ");
        cliente.setTelefone("11988887777");

        assertAll(
            () -> assertEquals("98765432100", cliente.getCpf()),
            () -> assertEquals("nova@email.com", cliente.getEmail()),
            () -> assertEquals("11988887777", cliente.getTelefone()),
            () -> assertThrows(IllegalArgumentException.class, () -> cliente.setCpf("abc")),
            () -> assertThrows(IllegalArgumentException.class, () -> cliente.setEmail(null)),
            () -> assertThrows(IllegalArgumentException.class, () -> cliente.setEmail("sem-arroba"))
        );
    }

    @Test
    @DisplayName("Funcionario: matrícula e cargo obrigatórios, salário nunca negativo")
    void funcionarioDeveValidarMatriculaCargoESalario() {
        Funcionario f = new Funcionario("Bruno", "98765432100", " F-1 ", " Analista ", new BigDecimal("5000.00"));
        f.setCargo("Gerente");
        f.setSalario(BigDecimal.ZERO);

        assertAll(
            () -> assertEquals("F-1", f.getMatricula()),
            () -> assertEquals("Gerente", f.getCargo()),
            () -> assertEquals(0, BigDecimal.ZERO.compareTo(f.getSalario())),
            () -> assertThrows(IllegalArgumentException.class,
                    () -> new Funcionario("B", "1", "", "Analista", BigDecimal.TEN)),
            () -> assertThrows(IllegalArgumentException.class,
                    () -> new Funcionario("B", "1", "F-1", null, BigDecimal.TEN)),
            () -> assertThrows(IllegalArgumentException.class,
                    () -> new Funcionario("B", "1", "F-1", "Analista", new BigDecimal("-1"))),
            () -> assertThrows(IllegalArgumentException.class, () -> f.setSalario(null))
        );
    }

    @Test
    @DisplayName("Produto: setters com regra e produto inativo não vende")
    void produtoDeveValidarSettersEControlarAtivo() {
        Produto p = new Produto("P-1", "Mouse", new BigDecimal("50.00"), 3);
        p.setNome("  Mouse Gamer ");
        p.setDescricao("Sem fio");
        p.setPreco(new BigDecimal("60.00"));
        p.setQuantidadeEmEstoque(4);

        assertAll(
            () -> assertEquals("Mouse Gamer", p.getNome()),
            () -> assertEquals("Sem fio", p.getDescricao()),
            () -> assertTrue(p.temEstoqueDisponivel(4)),
            () -> assertFalse(p.temEstoqueDisponivel(5)),
            () -> assertThrows(IllegalArgumentException.class, () -> p.setNome(" ")),
            () -> assertThrows(IllegalArgumentException.class, () -> p.setPreco(null)),
            () -> assertThrows(IllegalArgumentException.class, () -> p.setPreco(new BigDecimal("-0.01"))),
            () -> assertThrows(IllegalArgumentException.class, () -> p.setQuantidadeEmEstoque(null)),
            () -> assertThrows(IllegalArgumentException.class, () -> p.setQuantidadeEmEstoque(-1))
        );

        p.desativar();
        assertFalse(p.isAtivo());
        assertFalse(p.temEstoqueDisponivel(1), "produto inativo não vende, mesmo com estoque");
        p.ativar();
        assertTrue(p.temEstoqueDisponivel(1));
    }

    @Test
    @DisplayName("Produto: baixar estoque recusa quantidade inválida ou maior que o disponível")
    void produtoDeveProtegerBaixaDeEstoque() {
        Produto p = new Produto("P-2", "Teclado", new BigDecimal("150.00"), 5);
        p.baixarEstoque(5);

        assertAll(
            () -> assertEquals(0, p.getQuantidadeEmEstoque()),
            () -> assertThrows(IllegalArgumentException.class, () -> p.baixarEstoque(0)),
            () -> assertThrows(IllegalArgumentException.class, () -> p.baixarEstoque(-2)),
            () -> assertThrows(IllegalArgumentException.class, () -> p.baixarEstoque(1))
        );
    }

    @Test
    @DisplayName("Pedido: item com quantidade zero, item de produto inativo e pedido sem número válido")
    void pedidoDeveRecusarItensInvalidos() {
        Pedido pedido = new Pedido("PED-1", umCliente());
        Produto ativo = new Produto("P-3", "Cabo", new BigDecimal("10.00"), 10);
        Produto inativo = new Produto("P-4", "Fone", new BigDecimal("80.00"), 10);
        inativo.desativar();

        assertAll(
            () -> assertThrows(IllegalArgumentException.class, () -> pedido.adicionarItem(ativo, 0)),
            () -> assertThrows(IllegalStateException.class, () -> pedido.adicionarItem(inativo, 1)),
            () -> assertEquals(0, pedido.getItens().size()),
            () -> assertEquals("PED-1", pedido.getNumero()),
            () -> assertEquals("Ana", pedido.getCliente().getNome())
        );
    }

    @Test
    @DisplayName("Pedido: toString resume número, cliente, itens, total e situação")
    void pedidoDeveResumirEmTexto() {
        Pedido pedido = new Pedido("PED-9", umCliente());
        pedido.adicionarItem(new Produto("P-5", "Cabo", new BigDecimal("10.00"), 10), 2);

        String texto = pedido.toString();
        String item = pedido.getItens().get(0).toString();

        assertAll(
            () -> assertTrue(texto.contains("PED-9") && texto.contains("Ana") && texto.contains("20.00")
                    && texto.contains("ABERTO")),
            () -> assertTrue(item.contains("Cabo") && item.contains("20.00")),
            () -> assertEquals(2, pedido.getItens().get(0).getQuantidade()),
            () -> assertEquals(0, new BigDecimal("10.00").compareTo(pedido.getItens().get(0).getPrecoPraticado()))
        );
    }

    @Test
    @DisplayName("Preço praticado fica congelado: mudar o preço do catálogo não muda o pedido")
    void precoPraticadoDeveFicarCongelado() {
        Produto cabo = new Produto("P-6", "Cabo", new BigDecimal("10.00"), 10);
        Pedido pedido = new Pedido("PED-10", umCliente());
        pedido.adicionarItem(cabo, 2);

        cabo.setPreco(new BigDecimal("99.00"));

        assertEquals(0, new BigDecimal("20.00").compareTo(pedido.calcularValorTotal()));
    }
}
