import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.time.LocalDateTime;

public class ConsumidorOperationsServer extends Thread {
    private final Socket socket;

    public ConsumidorOperationsServer(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            while (socket.isConnected()) {

                ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                final ConsumidorRequest consumidorRequest = (ConsumidorRequest) inputStream.readObject();
                Server.salvarTermoNoHistorico(consumidorRequest.getUser(), consumidorRequest.getTermo());

                final ByteArrayOutputStream out = new ByteArrayOutputStream();
                final ObjectOutputStream outputStream = new ObjectOutputStream(out);
                outputStream.writeObject(criarBusca(consumidorRequest));

                MulticastSocket multicastSocket = new MulticastSocket();
                final InetAddress byName = InetAddress.getByName("224.0.0.1");
                DatagramPacket busca = new DatagramPacket(out.toByteArray(), out.size(), byName, Constantes.PORTA_GRUPO);
                Server.limparPoll(consumidorRequest.getUser());
                multicastSocket.send(busca);

                Thread.sleep(Server.TMAX);

                new ObjectOutputStream(socket.getOutputStream()).writeObject(Server.pollResultados.get(consumidorRequest.getUser()));

            }
        } catch (IOException e) {
            // faz nada
        } catch (ClassNotFoundException | InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Conexão fechada com o consumidor");
    }

    private Busca criarBusca(ConsumidorRequest o) {
        return new Busca(LocalDateTime.now(), o.getTermo(), o.getUser());
    }


}
