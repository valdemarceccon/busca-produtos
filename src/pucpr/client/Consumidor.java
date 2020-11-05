package pucpr.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import pucpr.Constantes;
import pucpr.Util;

public class Consumidor {
    public static void main(String[] args) throws IOException, InterruptedException {

    }

    private Socket socket;

    public Consumidor() throws IOException {
        this.socket = new Socket();
        socket.connect(new InetSocketAddress(Constantes.PORTA));
    }

    public String pesquisar(String termo) throws IOException {
        try (final OutputStream outputStream = socket.getOutputStream(); final InputStream inputStream = socket.getInputStream()) {
            outputStream.write(termo.getBytes());

            return Util.read(inputStream);
        }
    }


}
