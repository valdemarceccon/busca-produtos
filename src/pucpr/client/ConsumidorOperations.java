package pucpr.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

import pucpr.servidor.Busca;
import pucpr.servidor.ConsumidorRequest;

public class ConsumidorOperations {

    private final Socket socket;
    private final String nome;

    public ConsumidorOperations(Socket socket, String nome) {
        this.socket = socket;
        this.nome = nome;
    }

    public List<String> buscar(String termo) throws IOException, ClassNotFoundException {
        write(new ConsumidorRequest(nome, termo));
        return (List<String>) read();
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
