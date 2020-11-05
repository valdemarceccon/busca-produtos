package pucpr.servidor;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import pucpr.Util;

public class Servidor {
    public static void main(String[] args) throws IOException {
        final ServerSocket server = new ServerSocket();
        server.bind(new InetSocketAddress(56789));
        System.out.println("Escutando por novas conexoes");
        int n = 0;
        while (true) {
            final Socket client = server.accept();
            n++;
            System.out.printf("Cliente %d conectado", n);
            new Thread(() -> read(client)).start();
        }
    }

    public static void read(Socket client) {
        System.out.println("Nova conecao aceita");
        try (InputStream inputStream = client.getInputStream()) {
            String msg = Util.read(inputStream);

            System.out.printf("Mensagem recebida: %s", msg);
//            System.out.printf("Mensagem recebida: %s", msg);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
