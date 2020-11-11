package pucpr.servidor;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import pucpr.Constantes;

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
                List<String> resp = (List<String>) inputStream.readObject();
                for (String s : resp) {
                    System.out.println("Recebeu da loja: " + s);
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


}
