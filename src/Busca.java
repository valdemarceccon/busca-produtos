import java.io.Serializable;
import java.time.LocalDateTime;

public class Busca implements Serializable {
    private final LocalDateTime time;
    private final String termo;
    private final String usuario;

    public Busca(LocalDateTime time, String termo, String usuario) {
        this.time = time;
        this.termo = termo;
        this.usuario = usuario;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public String getTermo() {
        return termo;
    }

    public String getUsuario() {
        return usuario;
    }
}
