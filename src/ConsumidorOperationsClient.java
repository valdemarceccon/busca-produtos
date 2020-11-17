import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ConsumidorOperationsClient {

    private final Socket socket;
    private final String nome;

    public ConsumidorOperationsClient(Socket socket, String nome) {
        this.socket = socket;
        this.nome = nome;
    }

    public ResultadoBusca buscar(String termo) throws IOException, ClassNotFoundException {
        write(new ConsumidorRequest(nome, termo));
        return (ResultadoBusca) read();
    }

    private void write(Object obj) throws IOException {
        final ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
        outputStream.writeObject(obj);
    }

    private Object read() throws IOException, ClassNotFoundException {
        final ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
        return inputStream.readObject();
    }
}
