package com.ecommerce_pedidos.pagamento;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.ecommerce_pedidos.modelo.pagamento.Boleto;
import com.ecommerce_pedidos.modelo.pagamento.CartaoCredito;
import com.ecommerce_pedidos.modelo.pagamento.Dinheiro;
import com.ecommerce_pedidos.modelo.pagamento.Pix;

class RegrasDePagamentoTest {

    private static final BigDecimal CEM = new BigDecimal("100.00");

    // ---------- Boleto ----------

    @Test
    @DisplayName("Boleto: deve recusar vencimento inválido")
    void boletoDeveRecusarVencimentoInvalido() {
        assertAll(
            () -> assertThrows(IllegalArgumentException.class,
                () -> new Boleto(CEM, "B-1", LocalDate.now().minusDays(1))),

            () -> assertThrows(IllegalArgumentException.class,
                () -> new Boleto(CEM, "B-1", null)),

            () -> assertThrows(IllegalArgumentException.class,
                () -> new Boleto(CEM, " ", LocalDate.now().plusDays(1)))
        );
    }

    @Test
    @DisplayName("Boleto: deve aguardar compensação quando o valor é correto")
    void boletoVencendoHojeEAceitoEAguardaCompensacao() {
        Boleto boleto = new Boleto(CEM, "B-2", LocalDate.now());

        assertAll(
            () -> assertFalse(boleto.processar(CEM)),
            () -> assertEquals("BOL-B-2", boleto.getComprovante()),
            () -> assertTrue(boleto.getDescricao().contains("B-2"))
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"50.00", "150.00"})
    @DisplayName("Boleto: deve recusar valor divergente")
    void boletoDeveRecusarValorDivergente(String valor) {
        Boleto boleto = new Boleto(
            CEM, "B-3", LocalDate.now().plusDays(5)
        );

        assertThrows(
            IllegalArgumentException.class,
            () -> boleto.processar(new BigDecimal(valor))
        );
    }

    // ---------- Cartão de crédito ----------

    @Test
    @DisplayName("Cartão: deve respeitar o limite de parcelas")
    void cartaoDeveRespeitarLimiteDeParcelas() {
        assertAll(
            () -> assertEquals(1,
                new CartaoCredito(CEM, "4111111111111111", 1).getParcelas()),

            () -> assertEquals(12,
                new CartaoCredito(CEM, "4111111111111111", 12).getParcelas()),

            () -> assertThrows(IllegalArgumentException.class,
                () -> new CartaoCredito(CEM, "4111111111111111", 0)),

            () -> assertThrows(IllegalArgumentException.class,
                () -> new CartaoCredito(CEM, "4111111111111111", 13)),

            () -> assertThrows(IllegalArgumentException.class,
                () -> new CartaoCredito(CEM, "4111111111111111", 15))
        );
    }

    @Test
    @DisplayName("Cartão: deve proteger o número e permitir estorno")
    void cartaoDeveProtegerNumeroEPermitirEstorno() {
        CartaoCredito cartao = new CartaoCredito(
            CEM, "4111111111111234", 3
        );

        assertAll(
            () -> assertThrows(IllegalArgumentException.class,
                () -> new CartaoCredito(CEM, "", 1)),

            () -> assertEquals(
                "Cartão final 1234 em 3x", cartao.getDescricao()),

            () -> assertFalse(cartao.getDescricao().contains("4111")),

            () -> assertTrue(
                new CartaoCredito(CEM, "99", 1)
                    .getDescricao().contains("99")),

            () -> assertTrue(cartao.estornar())
        );
    }

    @Test
    @DisplayName("Cartão: deve aprovar pagamento com valor correto")
    void cartaoDeveAprovarValorCorreto() {
        CartaoCredito cartao = new CartaoCredito(
            CEM, "4111111111111234", 3
        );

        assertAll(
            () -> assertTrue(cartao.processar(CEM)),
            () -> assertTrue(cartao.getComprovante().startsWith("CARTAO-"))
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"50.00", "150.00"})
    @DisplayName("Cartão: deve recusar valor divergente")
    void cartaoDeveRecusarValorDivergente(String valor) {
        CartaoCredito cartao = new CartaoCredito(
            CEM, "4111111111111234", 3
        );

        assertThrows(
            IllegalArgumentException.class,
            () -> cartao.processar(new BigDecimal(valor))
        );
    }

    // ---------- Pix ----------

    @Test
    @DisplayName("Pix: deve exigir chave e aprovar imediatamente")
    void pixDeveExigirChaveEAprovarNaHora() {
        Pix pix = new Pix(CEM, "ana@email.com");

        assertAll(
            () -> assertThrows(IllegalArgumentException.class,
                () -> new Pix(CEM, null)),

            () -> assertThrows(IllegalArgumentException.class,
                () -> new Pix(CEM, "  ")),

            () -> assertTrue(pix.processar(CEM)),
            () -> assertTrue(pix.estornar()),
            () -> assertTrue(pix.getComprovante().startsWith("PIX-")),

            () -> assertEquals(
                "Pix - chave ana@email.com (aprovação imediata)",
                pix.getResumoPagamento())
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"50.00", "150.00"})
    @DisplayName("Pix: deve recusar valor divergente")
    void pixDeveRecusarValorDivergente(String valor) {
        Pix pix = new Pix(CEM, "ana@email.com");

        assertThrows(
            IllegalArgumentException.class,
            () -> pix.processar(new BigDecimal(valor))
        );
    }

    @Test
    @DisplayName("Pix: deve aceitar valores iguais com escalas diferentes")
    void pixDeveAceitarValoresEquivalentes() {
        Pix pix = new Pix(CEM, "ana@email.com");

        assertTrue(pix.processar(new BigDecimal("100.0")));
    }

    // ---------- Regras compartilhadas ----------

    @Test
    @DisplayName("Método default deve utilizar a descrição")
    void metodoDefaultDeveUsarDescricaoQuandoNaoSobrescrito() {
        CartaoCredito cartao = new CartaoCredito(
            CEM, "4111111111111234", 2
        );

        assertEquals(
            cartao.getDescricao(),
            cartao.getResumoPagamento()
        );
    }

    @Test
    @DisplayName("Forma de pagamento deve recusar valor inválido")
    void deveRecusarValorInvalidoNaFormaDePagamento() {
        assertAll(
            () -> assertThrows(IllegalArgumentException.class,
                () -> new Pix(BigDecimal.ZERO, "k")),

            () -> assertThrows(IllegalArgumentException.class,
                () -> new Pix(new BigDecimal("-1"), "k")),

            () -> assertThrows(IllegalArgumentException.class,
                () -> new Pix(null, "k"))
        );
    }

    @Test
    @DisplayName("Deve recusar valor cobrado nulo")
    void deveRecusarValorCobradoNulo() {
        Pix pix = new Pix(CEM, "ana@email.com");

        Boleto boleto = new Boleto(
            CEM, "B-4", LocalDate.now().plusDays(5)
        );

        CartaoCredito cartao = new CartaoCredito(
            CEM, "4111111111111234", 1
        );

        Dinheiro dinheiro = new Dinheiro(CEM);

        assertAll(
            () -> assertThrows(IllegalArgumentException.class,
                () -> pix.processar(null)),

            () -> assertThrows(IllegalArgumentException.class,
                () -> boleto.processar(null)),

            () -> assertThrows(IllegalArgumentException.class,
                () -> cartao.processar(null)),

            () -> assertThrows(IllegalArgumentException.class,
                () -> dinheiro.processar(null))
        );
    }

    @Test
    @DisplayName("Deve permitir processamento sem informar novamente o valor")
    void deveProcessarPagamentoSemParametro() {
        Pix pix = new Pix(CEM, "ana@email.com");

        Boleto boleto = new Boleto(
            CEM, "B-5", LocalDate.now().plusDays(5)
        );

        CartaoCredito cartao = new CartaoCredito(
            CEM, "4111111111111234", 1
        );

        Dinheiro dinheiro = new Dinheiro(CEM);

        assertAll(
            () -> assertTrue(pix.processar()),
            () -> assertFalse(boleto.processar()),
            () -> assertTrue(cartao.processar()),
            () -> assertTrue(dinheiro.processar())
        );
    }

    // ---------- Dinheiro ----------

    @Test
    @DisplayName("Dinheiro: deve calcular troco e recusar valor insuficiente")
    void dinheiroDeveCalcularTrocoERecusarInsuficiente() {
        Dinheiro dinheiro = new Dinheiro(new BigDecimal("150.00"));

        assertAll(
            () -> assertTrue(
                dinheiro.processar(new BigDecimal("150.00"))),

            () -> assertTrue(
                dinheiro.processar(new BigDecimal("100.00"))),

            () -> assertFalse(
                dinheiro.processar(new BigDecimal("150.01"))),

            () -> assertEquals(0,
                new BigDecimal("50.00")
                    .compareTo(dinheiro.calcularTroco(CEM))),

            () -> assertEquals("Dinheiro", dinheiro.getDescricao()),

            () -> assertTrue(
                dinheiro.getComprovante().startsWith("RECIBO-")),

            () -> assertThrows(IllegalArgumentException.class,
                () -> new Dinheiro(new BigDecimal("-1"))),

            () -> assertThrows(IllegalArgumentException.class,
                () -> new Dinheiro(null))
        );
    }

    @Test
    @DisplayName("Dinheiro: deve retornar troco zero no pagamento exato")
    void dinheiroDeveRetornarTrocoZero() {
        Dinheiro dinheiro = new Dinheiro(CEM);

        assertEquals(
            0, BigDecimal.ZERO.compareTo(dinheiro.calcularTroco(CEM))
        );
    }

    @Test
    @DisplayName("Dinheiro: deve impedir cálculo de troco negativo")
    void dinheiroDeveImpedirTrocoNegativo() {
        Dinheiro dinheiro = new Dinheiro(CEM);

        assertThrows(
            IllegalArgumentException.class,
            () -> dinheiro.calcularTroco(new BigDecimal("150.00"))
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"0", "-100.00"})
    @DisplayName("Dinheiro: deve recusar valor cobrado inválido")
    void dinheiroDeveRecusarValorCobradoInvalido(String valor) {
        Dinheiro dinheiro = new Dinheiro(CEM);

        assertThrows(
            IllegalArgumentException.class,
            () -> dinheiro.processar(new BigDecimal(valor))
        );
    }
}