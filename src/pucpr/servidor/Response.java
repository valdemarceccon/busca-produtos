package pucpr.servidor;

import java.io.Serializable;

public abstract class Response implements Serializable {
    public RequestStatus status = RequestStatus.SUCESSO;
    public String mensagem = "";
}
