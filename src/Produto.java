import java.io.Serializable;

public class Produto implements Serializable {
    private String nomeLoja;
    private String nomeProduto;

    public Produto(String nomeLoja, String nomeProduto) {
        this.nomeLoja = nomeLoja;
        this.nomeProduto = nomeProduto;
    }

    public String getNomeLoja() {
        return nomeLoja;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }
}
