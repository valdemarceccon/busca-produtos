import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ResultadoBusca implements Serializable {
    private String identificacao;
    private List<Produto> resultado;

    public ResultadoBusca(String nomeLoja, String identificacao) {
        this.identificacao = identificacao;
        this.resultado = new ArrayList<>();
    }

    public String getIdentificacao() {
        return identificacao;
    }

    public void setIdentificacao(String identificacao) {
        this.identificacao = identificacao;
    }

    public List<Produto> getResultado() {
        return resultado;
    }

    public void addResultado(Produto p) {
        this.resultado.add(p);
    }

}
