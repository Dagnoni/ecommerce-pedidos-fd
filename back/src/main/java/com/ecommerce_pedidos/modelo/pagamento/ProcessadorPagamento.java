package com.ecommerce_pedidos.modelo.pagamento;

import java.math.BigDecimal;

public interface ProcessadorPagamento {
    boolean processar(BigDecimal valor);
    String getComprovante();
    String getDescricao();
}
