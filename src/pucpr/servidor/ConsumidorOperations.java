package pucpr.servidor;

import static pucpr.Constantes.PORTA_GRUPO;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ConsumidorOperations extends Thread {
    private final Socket socket;

    public ConsumidorOperations(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            while (socket.isConnected()) {

                ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                final ConsumidorRequest o = (ConsumidorRequest) inputStream.readObject();
                Servidor.salvarTermoNoHistorico(o.getUser(), o.getTermo());

                MulticastSocket multicastSocket = new MulticastSocket();
                final InetAddress byName = InetAddress.getByName("224.0.0.1");
                DatagramPacket busca = new DatagramPacket(o.getTermo().getBytes(), o.getTermo().length(), byName, PORTA_GRUPO);
                multicastSocket.send(busca);

                List<String> resultadoBusca = new ArrayList<>();
                Random random = new Random();
                int ini = random.nextInt(10) + 3;
                int fim = ini + random.nextInt(15) + ini;
                for (int i = ini; i < fim; i++) {
                    resultadoBusca.add(String.format("Exemplo %d %s", i, o.getTermo()));
                }
                new ObjectOutputStream(socket.getOutputStream()).writeObject(resultadoBusca);

            }
        } catch (IOException e) {
            // faz nada
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("Conexão fechada com o consumidor");
    }


}
