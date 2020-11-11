package pucpr.servidor;

import java.io.Serializable;

public class AtualizarTMaxRequest extends AdminRequest implements Serializable {
    private final long novoValor;

    public AtualizarTMaxRequest(long novoValor) {
        this.novoValor = novoValor;
    }

    public long getNovoValor() {
        return novoValor;
    }

    @Override
    public AdminOpcoes tipo() {
        return AdminOpcoes.AtualizarTMax;
    }
}
