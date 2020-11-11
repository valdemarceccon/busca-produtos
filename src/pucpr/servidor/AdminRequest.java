package pucpr.servidor;

import java.io.Serializable;

public abstract class AdminRequest implements Serializable {

    public abstract AdminOpcoes tipo();
}
