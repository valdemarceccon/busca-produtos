package pucpr.servidor;

import java.io.Serializable;

public class AdminRequest implements Serializable {

    public AdminOpcoes opcao;
    public String valor;

    public AdminRequest(AdminOpcoes opcao) {
        this.opcao = opcao;
    }

    public AdminRequest(AdminOpcoes opcao, String valor) {
        this.opcao = opcao;
        this.valor = valor;
    }
}
