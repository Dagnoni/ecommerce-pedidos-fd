package com.ecommerce_pedidos.utils;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import com.ecommerce_pedidos.modelo.FormaPagamento;

class PagamentoTest {

    private static final String NUMERO_BOLETO = "34191790010104351004791020150008196610000015000";

    // ---------- Pix ----------

    @Test
    @DisplayName("Deve processar Pix com sucesso quando os dados são válidos")
    void deveProcessarPixQuandoDadosValidos() {
        Pix pix = new Pix(new BigDecimal("150.00"), "ana@exemplo.com");
        assertTrue(pix.processar());
        assertEquals(0, new BigDecimal("150.00").compareTo(pix.getValor()));
    }

    @Test
    @DisplayName("Deve incluir a chave no resumo do Pix de forma legível")
    void deveIncluirChaveNoResumoDoPix() {
        Pix pix = new Pix(new BigDecimal("150.00"), "ana@exemplo.com");
        String resumo = pix.getResumo();
        assertTrue(resumo.startsWith("Pix no valor de R$ 150.00"));
        assertTrue(resumo.contains("chave ana@exemplo.com"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    @DisplayName("Deve recusar Pix quando a chave está vazia")
    void deveRecusarPixQuandoChaveVazia(String chave) {
        assertThrows(IllegalArgumentException.class,
                () -> new Pix(new BigDecimal("10.00"), chave));
    }

    // ---------- Cartão ----------

    @Test
    @DisplayName("Deve processar cartão com sucesso quando os dados são válidos")
    void deveProcessarCartaoQuandoDadosValidos() {
        CartaoCredito cartao = new CartaoCredito(new BigDecimal("899.90"), "4111111111111111", 3);
        assertTrue(cartao.processar());
        assertEquals(3, cartao.getParcelas());
    }

    @Test
    @DisplayName("Deve mostrar só os quatro últimos dígitos e as parcelas no resumo do cartão")
    void deveMostrarUltimosDigitosNoResumoDoCartao() {
        CartaoCredito cartao = new CartaoCredito(new BigDecimal("899.90"), "4111111111111111", 3);
        String resumo = cartao.getResumo();
        assertTrue(resumo.contains("final 1111"));
        assertTrue(resumo.contains("3x"));
        assertFalse(resumo.contains("4111111111111111"));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 12})
    @DisplayName("Deve aceitar parcelamento nos limites permitidos")
    void deveAceitarParcelamentoNosLimites(int parcelas) {
        CartaoCredito cartao = new CartaoCredito(new BigDecimal("100.00"), "4111111111111111", parcelas);
        assertEquals(parcelas, cartao.getParcelas());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, 13, 15})
    @DisplayName("Deve recusar cartão quando o parcelamento está fora de 1 a 12")
    void deveRecusarCartaoQuandoParcelamentoInvalido(int parcelas) {
        assertThrows(IllegalArgumentException.class,
                () -> new CartaoCredito(new BigDecimal("100.00"), "4111111111111111", parcelas));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    @DisplayName("Deve recusar cartão quando o número está vazio")
    void deveRecusarCartaoQuandoNumeroVazio(String numero) {
        assertThrows(IllegalArgumentException.class,
                () -> new CartaoCredito(new BigDecimal("100.00"), numero, 1));
    }

    // ---------- Boleto ----------

    @Test
    @DisplayName("Deve processar boleto com sucesso quando ainda não venceu")
    void deveProcessarBoletoQuandoNaoVenceu() {
        Boleto boleto = new Boleto(new BigDecimal("300.00"), NUMERO_BOLETO, LocalDate.now().plusDays(5));
        assertTrue(boleto.processar());
    }

    @Test
    @DisplayName("Deve processar boleto com sucesso quando vence hoje")
    void deveProcessarBoletoQuandoVenceHoje() {
        Boleto boleto = new Boleto(new BigDecimal("300.00"), NUMERO_BOLETO, LocalDate.now());
        assertTrue(boleto.processar());
    }

    @Test
    @DisplayName("Deve recusar o pagamento quando o boleto está vencido")
    void deveRecusarPagamentoQuandoBoletoVencido() {
        Boleto boleto = new Boleto(new BigDecimal("300.00"), NUMERO_BOLETO, LocalDate.now().minusDays(1));
        assertFalse(boleto.processar());
    }

    @Test
    @DisplayName("Deve incluir número e vencimento no resumo do boleto")
    void deveIncluirNumeroEVencimentoNoResumoDoBoleto() {
        LocalDate vencimento = LocalDate.now().plusDays(5);
        Boleto boleto = new Boleto(new BigDecimal("300.00"), NUMERO_BOLETO, vencimento);
        String resumo = boleto.getResumo();
        assertTrue(resumo.contains(NUMERO_BOLETO));
        assertTrue(resumo.contains(vencimento.toString()));
    }

    @Test
    @DisplayName("Deve recusar boleto quando o número está vazio")
    void deveRecusarBoletoQuandoNumeroVazio() {
        assertThrows(IllegalArgumentException.class,
                () -> new Boleto(new BigDecimal("300.00"), " ", LocalDate.now()));
    }

    @Test
    @DisplayName("Deve recusar boleto quando a data de vencimento é nula")
    void deveRecusarBoletoQuandoVencimentoNulo() {
        assertThrows(IllegalArgumentException.class,
                () -> new Boleto(new BigDecimal("300.00"), NUMERO_BOLETO, null));
    }

    // ---------- regra comum (FormaPagamento) ----------

    @ParameterizedTest
    @ValueSource(strings = {"0", "-1", "-0.01"})
    @DisplayName("Deve recusar pagamento quando o valor não é positivo")
    void deveRecusarPagamentoQuandoValorNaoPositivo(String valor) {
        assertThrows(IllegalArgumentException.class,
                () -> new Pix(new BigDecimal(valor), "ana@exemplo.com"));
    }

    @Test
    @DisplayName("Deve recusar pagamento quando o valor é nulo")
    void deveRecusarPagamentoQuandoValorNulo() {
        assertThrows(IllegalArgumentException.class, () -> new Pix(null, "ana@exemplo.com"));
    }

    // ---------- polimorfismo ----------

    @Test
    @DisplayName("Deve processar todas as formas de pagamento quando percorridas pelo tipo base")
    void deveProcessarTodasAsFormasPeloTipoBase() {
        FormaPagamento[] pagamentos = {
            new Pix(new BigDecimal("150.00"), "ana@exemplo.com"),
            new CartaoCredito(new BigDecimal("899.90"), "4111111111111111", 3),
            new Boleto(new BigDecimal("300.00"), NUMERO_BOLETO, LocalDate.now().plusDays(5))
        };

        for (FormaPagamento pagamento : pagamentos) {
            assertTrue(pagamento.processar(), pagamento.getClass().getSimpleName());
            assertFalse(pagamento.getResumo().isBlank());
        }
    }
}