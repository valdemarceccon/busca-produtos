package pucpr;

import java.math.BigDecimal;

public class Produto {
    public final String titulo;
    public final BigDecimal preco;

    public Produto(String titulo, BigDecimal preco) {
        this.titulo = titulo;
        this.preco = preco;
    }
}
