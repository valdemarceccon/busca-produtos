import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.List;

public class UPDServer extends Thread {

    private List<Busca> buscas = new ArrayList<>();

    @Override
    public void run() {
        try {
            DatagramSocket socket = new DatagramSocket(Constantes.PORTA_UPD);

            while (true) {
                DatagramPacket packet = new DatagramPacket(new byte[4096], 4096);
                socket.receive(packet);
                final ObjectInputStream inputStream = new ObjectInputStream(new ByteArrayInputStream(packet.getData()));
                ResultadoBusca resp = (ResultadoBusca) inputStream.readObject();

                Server.adicionar(resp);
                for (Produto p: resp.getResultado()) {
                    System.out.printf("Recebeu %s da %s: %n", p.getNomeProduto(), p.getNomeLoja());
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


}
