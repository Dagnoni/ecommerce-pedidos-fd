package com.ecommerce_pedidos.modelo;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import com.ecommerce_pedidos.excecao.EstoqueInsuficienteException;

class ProdutoTest {

    private Produto notebook;

    @BeforeEach
    void prepararCenario() {
        notebook = new Produto("NOTE-001", "Notebook", new BigDecimal("3000.00"), 5);
    }

    // ---------- caminho feliz ----------

    @Test
    @DisplayName("Deve criar produto ativo quando os dados são válidos")
    void deveCriarProdutoAtivoQuandoDadosValidos() {
        assertEquals("NOTE-001", notebook.getCodigo());
        assertEquals("Notebook", notebook.getNome());
        assertEquals(0, new BigDecimal("3000.00").compareTo(notebook.getPreco()));
        assertEquals(5, notebook.getQuantidadeEmEstoque());
        assertTrue(notebook.isAtivo());
    }

    @Test
    @DisplayName("Deve remover espaços do código e do nome quando criado com espaços")
    void deveRemoverEspacosQuandoCriadoComEspacos() {
        Produto mouse = new Produto("  MOU-1 ", "  Mouse  ", new BigDecimal("50.00"), 1);
        assertEquals("MOU-1", mouse.getCodigo());
        assertEquals("Mouse", mouse.getNome());
    }

    @Test
    @DisplayName("Deve baixar o estoque quando há quantidade suficiente")
    void deveBaixarEstoqueQuandoHaQuantidadeSuficiente() throws Exception {
        notebook.baixarEstoque(2);
        assertEquals(3, notebook.getQuantidadeEmEstoque());
    }

    @Test
    @DisplayName("Deve zerar o estoque quando a baixa é exatamente a quantidade disponível")
    void deveZerarEstoqueQuandoBaixaIgualAoDisponivel() throws Exception {
        notebook.baixarEstoque(5);
        assertEquals(0, notebook.getQuantidadeEmEstoque());
    }

    @Test
    @DisplayName("Deve repor o estoque quando nova quantidade é informada")
    void deveReporEstoqueQuandoNovaQuantidadeInformada() {
        notebook.setQuantidadeEmEstoque(20);
        assertEquals(20, notebook.getQuantidadeEmEstoque());
    }

    @Test
    @DisplayName("Deve aceitar preço zero")
    void deveAceitarPrecoZero() {
        notebook.setPreco(BigDecimal.ZERO);
        assertEquals(0, BigDecimal.ZERO.compareTo(notebook.getPreco()));
    }

    @Test
    @DisplayName("Deve desativar e reativar o produto")
    void deveDesativarEReativarProduto() {
        notebook.desativar();
        assertFalse(notebook.isAtivo());
        notebook.ativar();
        assertTrue(notebook.isAtivo());
    }

    @Test
    @DisplayName("Deve informar disponibilidade quando produto ativo tem estoque suficiente")
    void deveTerEstoqueDisponivelQuandoAtivoComEstoque() {
        assertTrue(notebook.temEstoqueDisponivel(5));
    }

    @Test
    @DisplayName("Deve informar indisponibilidade quando a quantidade desejada excede o estoque")
    void deveNaoTerEstoqueDisponivelQuandoQuantidadeExcedeEstoque() {
        assertFalse(notebook.temEstoqueDisponivel(6));
    }

    @Test
    @DisplayName("Deve informar indisponibilidade quando o produto está desativado")
    void deveNaoTerEstoqueDisponivelQuandoProdutoDesativado() {
        notebook.desativar();
        assertFalse(notebook.temEstoqueDisponivel(1));
    }

    // ---------- Mapa de Exceções ----------

    @Test
    @DisplayName("Deve lançar exceção quando o estoque é insuficiente")
    void deveLancarExcecaoQuandoEstoqueInsuficiente() {
        EstoqueInsuficienteException erro = assertThrows(
                EstoqueInsuficienteException.class,
                () -> notebook.baixarEstoque(50));

        assertEquals(50, erro.getQuantidadeSolicitada());
        assertSame(notebook, erro.getProduto());
        assertTrue(erro.getMessage().contains("Notebook"));
        assertEquals(5, notebook.getQuantidadeEmEstoque()); // não mexeu no estoque
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -50})
    @DisplayName("Deve recusar baixa de estoque quando a quantidade não é positiva")
    void deveRecusarBaixaQuandoQuantidadeNaoPositiva(int quantidade) {
        assertThrows(IllegalArgumentException.class, () -> notebook.baixarEstoque(quantidade));
        assertEquals(5, notebook.getQuantidadeEmEstoque());
    }

    @Test
    @DisplayName("Deve recusar produto quando o preço é negativo")
    void deveRecusarProdutoComPrecoNegativo() {
        assertThrows(IllegalArgumentException.class,
                () -> new Produto("P-1", "Cabo", new BigDecimal("-0.01"), 1));
    }

    @Test
    @DisplayName("Deve manter o preço anterior quando o novo preço é negativo")
    void deveManterPrecoQuandoNovoPrecoNegativo() {
        assertThrows(IllegalArgumentException.class,
                () -> notebook.setPreco(new BigDecimal("-10.00")));
        assertEquals(0, new BigDecimal("3000.00").compareTo(notebook.getPreco()));
    }

    @Test
    @DisplayName("Deve recusar produto quando o preço é nulo")
    void deveRecusarProdutoQuandoPrecoNulo() {
        assertThrows(IllegalArgumentException.class,
                () -> new Produto("P-1", "Cabo", null, 1));
    }

    @Test
    @DisplayName("Deve recusar produto quando o estoque inicial é negativo")
    void deveRecusarProdutoQuandoEstoqueInicialNegativo() {
        assertThrows(IllegalArgumentException.class,
                () -> new Produto("P-1", "Cabo", BigDecimal.TEN, -1));
    }

    @Test
    @DisplayName("Deve recusar produto quando o estoque inicial é nulo")
    void deveRecusarProdutoQuandoEstoqueInicialNulo() {
        assertThrows(IllegalArgumentException.class,
                () -> new Produto("P-1", "Cabo", BigDecimal.TEN, null));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    @DisplayName("Deve recusar produto quando o nome está vazio")
    void deveRecusarProdutoQuandoNomeVazio(String nome) {
        assertThrows(IllegalArgumentException.class,
                () -> new Produto("P-1", nome, BigDecimal.TEN, 1));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    @DisplayName("Deve recusar produto quando o código está vazio")
    void deveRecusarProdutoQuandoCodigoVazio(String codigo) {
        assertThrows(IllegalArgumentException.class,
                () -> new Produto(codigo, "Cabo", BigDecimal.TEN, 1));
    }
}