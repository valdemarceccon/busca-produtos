package pucpr.servidor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ResultadoBusca implements Serializable {
    private String identificacao;
    private List<String> resultado;

    public ResultadoBusca(String identificacao) {
        this.identificacao = identificacao;
        this.resultado = new ArrayList<>();
    }

    public String getIdentificacao() {
        return identificacao;
    }

    public void setIdentificacao(String identificacao) {
        this.identificacao = identificacao;
    }

    public List<String> getResultado() {
        return resultado;
    }

    public void addResultado(String s) {
        this.resultado.add(s);
    }
}
