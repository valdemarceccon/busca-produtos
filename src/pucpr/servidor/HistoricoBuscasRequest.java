package pucpr.servidor;

import java.io.Serializable;

public class HistoricoBuscasRequest extends AdminRequest implements Serializable {
    @Override
    public AdminOpcoes tipo() {
        return AdminOpcoes.HistoricoBusca;
    }
}
