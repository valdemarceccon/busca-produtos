package pucpr.servidor;

import static pucpr.Constantes.PORTA_GRUPO;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.time.LocalDateTime;
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
                final ConsumidorRequest consumidorRequest = (ConsumidorRequest) inputStream.readObject();
                Servidor.salvarTermoNoHistorico(consumidorRequest.getUser(), consumidorRequest.getTermo());

                final ByteArrayOutputStream out = new ByteArrayOutputStream();
                final ObjectOutputStream outputStream = new ObjectOutputStream(out);
                outputStream.writeObject(criarBusca(consumidorRequest));

                MulticastSocket multicastSocket = new MulticastSocket();
                final InetAddress byName = InetAddress.getByName("224.0.0.1");
                DatagramPacket busca = new DatagramPacket(out.toByteArray(), out.size(), byName, PORTA_GRUPO);
                Servidor.limparPoll(consumidorRequest.getUser());
                multicastSocket.send(busca);

                Thread.sleep(Servidor.TMAX);

                new ObjectOutputStream(socket.getOutputStream()).writeObject(Servidor.pollResultados.get(consumidorRequest.getUser()));

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
