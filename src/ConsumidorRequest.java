import java.io.Serializable;

public class ConsumidorRequest implements Serializable {
    private final String user;
    private final String termo;

    public ConsumidorRequest(String user, String termo) {
        this.user = user;
        this.termo = termo;
    }

    public String getTermo() {
        return termo;
    }

    public String getUser() {
        return user;
    }
}
