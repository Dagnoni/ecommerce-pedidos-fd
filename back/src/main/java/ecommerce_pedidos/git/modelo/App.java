package ecommerce_pedidos.git.modelo;

import java.math.BigDecimal;

public class App {
    public static void main(String[] args) {
        Produto teclado = new Produto("TEC-001", "Teclado", new BigDecimal("150.00"), 8);
        Produto monitor = new Produto("MON-001", "Monitor", new BigDecimal("899.90"), 76);

        System.out.println(teclado);
        System.out.println(monitor);

        System.out.println(teclado.temEstoqueDisponivel(5));  // true
        System.out.println(monitor.temEstoqueDisponivel(100)); // false

        teclado.baixarEstoque(3);
        System.out.println(teclado); 

        Cliente cliente = new Cliente(
            "Ana Souza", "12345678900", "ana@exemplo.com",
            "11999990000", "Rua das Flores", "Centro", 100,
            "São Carlos", "SP", "Brasil"
        );
        System.out.println(cliente.getIdentificacao());

        try {
            teclado.setPreco(new BigDecimal("-10.00"));
            System.out.println("FALHOU: aceitou preço negativo");
        } catch (IllegalArgumentException e) {
            System.out.println("OK: recusou preço negativo -> " + e.getMessage());
        }

        try {
            new Cliente("Bruno", "abc123", "bruno@exemplo.com",
                null, null, null, null, null, null, null);
            System.out.println("FALHOU: aceitou documento com letras");
        } catch (IllegalArgumentException e) {
            System.out.println("OK: recusou documento inválido -> " + e.getMessage());
        }

        try {
            new Cliente("Carla", "98765432100", "email-sem-arroba",
                null, null, null, null, null, null, null);
            System.out.println("FALHOU: aceitou e-mail sem @");
        } catch (IllegalArgumentException e) {
            System.out.println("OK: recusou e-mail inválido -> " + e.getMessage());
        }
    }
}
