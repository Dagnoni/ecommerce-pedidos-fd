package com.ecommerce_pedidos.pagamento;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.ecommerce_pedidos.modelo.pagamento.Boleto;
import com.ecommerce_pedidos.modelo.pagamento.CartaoCredito;
import com.ecommerce_pedidos.modelo.pagamento.Dinheiro;
import com.ecommerce_pedidos.modelo.pagamento.Pix;

/** Regras próprias de cada forma de pagamento (Aula 08), uma classe por vez. */
class RegrasDePagamentoTest {

    private static final BigDecimal CEM = new BigDecimal("100.00");

    @Test
    @DisplayName("Boleto: vencimento no passado ou ausente é erro de uso")
    void boletoDeveRecusarVencimentoInvalido() {
        assertAll(
            () -> assertThrows(IllegalArgumentException.class,
                    () -> new Boleto(CEM, "B-1", LocalDate.now().minusDays(1))),
            () -> assertThrows(IllegalArgumentException.class, () -> new Boleto(CEM, "B-1", null)),
            () -> assertThrows(IllegalArgumentException.class,
                    () -> new Boleto(CEM, " ", LocalDate.now().plusDays(1)))
        );
    }

    @Test
    @DisplayName("Boleto: vence hoje é aceito; processar devolve false até a compensação")
    void boletoVencendoHojeEAceitoEAguardaCompensacao() {
        Boleto boleto = new Boleto(CEM, "B-2", LocalDate.now());

        assertAll(
            () -> assertFalse(boleto.processar(CEM)),
            () -> assertEquals("BOL-B-2", boleto.getComprovante()),
            () -> assertTrue(boleto.getDescricao().contains("B-2"))
        );
    }

    @Test
    @DisplayName("Cartão: 1 a 12 parcelas é válido; 0, 13 e 15 são erro de uso")
    void cartaoDeveRespeitarLimiteDeParcelas() {
        assertAll(
            () -> assertEquals(1, new CartaoCredito(CEM, "4111111111111111", 1).getParcelas()),
            () -> assertEquals(12, new CartaoCredito(CEM, "4111111111111111", 12).getParcelas()),
            () -> assertThrows(IllegalArgumentException.class, () -> new CartaoCredito(CEM, "4111111111111111", 0)),
            () -> assertThrows(IllegalArgumentException.class, () -> new CartaoCredito(CEM, "4111111111111111", 13)),
            () -> assertThrows(IllegalArgumentException.class, () -> new CartaoCredito(CEM, "4111111111111111", 15))
        );
    }

    @Test
    @DisplayName("Cartão: número obrigatório, descrição mostra só os 4 últimos dígitos e estorno é aceito")
    void cartaoDeveProtegerNumeroEPermitirEstorno() {
        CartaoCredito cartao = new CartaoCredito(CEM, "4111111111111234", 3);

        assertAll(
            () -> assertThrows(IllegalArgumentException.class, () -> new CartaoCredito(CEM, "", 1)),
            () -> assertEquals("Cartão final 1234 em 3x", cartao.getDescricao()),
            () -> assertFalse(cartao.getDescricao().contains("4111")),
            () -> assertTrue(new CartaoCredito(CEM, "99", 1).getDescricao().contains("99")),
            () -> assertTrue(cartao.estornar())
        );
    }

    @Test
    @DisplayName("Pix: chave obrigatória, aprovação imediata, estorno e resumo próprio")
    void pixDeveExigirChaveEAprovarNaHora() {
        Pix pix = new Pix(CEM, "ana@email.com");

        assertAll(
            () -> assertThrows(IllegalArgumentException.class, () -> new Pix(CEM, null)),
            () -> assertThrows(IllegalArgumentException.class, () -> new Pix(CEM, "  ")),
            () -> assertTrue(pix.processar(CEM)),
            () -> assertTrue(pix.estornar()),
            () -> assertTrue(pix.getComprovante().startsWith("PIX-")),
            () -> assertEquals("Pix - chave ana@email.com (aprovação imediata)", pix.getResumoPagamento())
        );
    }

    @Test
    @DisplayName("Método default da interface: quem não sobrescreve usa a descrição")
    void metodoDefaultDeveUsarDescricaoQuandoNaoSobrescrito() {
        CartaoCredito cartao = new CartaoCredito(CEM, "4111111111111234", 2);

        assertEquals(cartao.getDescricao(), cartao.getResumoPagamento());
    }

    @Test
    @DisplayName("Forma de pagamento com valor zero, negativo ou nulo é erro de uso")
    void deveRecusarValorInvalidoNaFormaDePagamento() {
        assertAll(
            () -> assertThrows(IllegalArgumentException.class, () -> new Pix(BigDecimal.ZERO, "k")),
            () -> assertThrows(IllegalArgumentException.class, () -> new Pix(new BigDecimal("-1"), "k")),
            () -> assertThrows(IllegalArgumentException.class, () -> new Pix(null, "k"))
        );
    }

    @Test
    @DisplayName("Dinheiro: aprova valor exato ou maior, recusa menor, calcula troco e recusa negativo")
    void dinheiroDeveCalcularTrocoERecusarInsuficiente() {
        Dinheiro dinheiro = new Dinheiro(new BigDecimal("150.00"));

        assertAll(
            () -> assertTrue(dinheiro.processar(new BigDecimal("150.00"))),
            () -> assertTrue(dinheiro.processar(new BigDecimal("100.00"))),
            () -> assertFalse(dinheiro.processar(new BigDecimal("150.01"))),
            () -> assertEquals(0, new BigDecimal("50.00").compareTo(dinheiro.calcularTroco(CEM))),
            () -> assertEquals("Dinheiro", dinheiro.getDescricao()),
            () -> assertTrue(dinheiro.getComprovante().startsWith("RECIBO-")),
            () -> assertThrows(IllegalArgumentException.class, () -> new Dinheiro(new BigDecimal("-1"))),
            () -> assertThrows(IllegalArgumentException.class, () -> new Dinheiro(null))
        );
    }
}
