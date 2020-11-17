import java.io.Serializable;

public class Produto implements Serializable {
    private String nomeLoja;
    private String nomeProduto;
    private final double preco;

    public Produto(String nomeLoja, String nomeProduto, double preco) {
        this.nomeLoja = nomeLoja;
        this.nomeProduto = nomeProduto;
        this.preco = preco;
    }

    public String getNomeLoja() {
        return nomeLoja;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public double getPreco() {
        return preco;
    }
}
