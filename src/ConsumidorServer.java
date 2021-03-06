import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ConsumidorServer extends Thread {
    @Override
    public void run() {
        try {
            ServerSocket s = new ServerSocket();
            s.bind(new InetSocketAddress(Constantes.PORTA_CLIENTE));
            System.out.println("Servidor consumidor inciado");
            while (true) {
                final Socket socket = s.accept();
                System.out.println("Recebida conexão com admin");
                new ConsumidorOperationsServer(socket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
