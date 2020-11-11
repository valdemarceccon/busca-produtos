package pucpr.servidor;

import java.io.Serializable;

public class VerTMaxRequest extends AdminRequest implements Serializable {
    @Override
    public AdminOpcoes tipo() {
        return AdminOpcoes.VerTMax;
    }
}
