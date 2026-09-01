import com.ecommerce.modelo.Produto;

public class App {
    public static void main(String[] args) {
        Produto produto = new Produto("001", "Monitor", 76, true );
        Produto produto2 = new Produto("002", "Teclado", 50, true );


        System.out.println(produto);
        System.out.println(produto2);

        System.out.println(teclado.temEstoqueDisponivel(10)); // true
        System.out.println(monitor.temEstoqueDisponivel(60)); // false

        teclado.baixarEstoque(10);
        System.out.println(teclado); // 40 unidades em estoque
    }
}