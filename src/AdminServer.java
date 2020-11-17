import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class AdminServer extends Thread {

    @Override
    public void run() {
        try {
            ServerSocket s = new ServerSocket();
            s.bind(new InetSocketAddress(Constantes.PORTA_ADMIN));
            System.out.println("Servidor admin inciado");
            while (true) {
                final Socket socket = s.accept();
                System.out.println("Recebida conexão com admin");
                new AdminOperationsServer(socket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
