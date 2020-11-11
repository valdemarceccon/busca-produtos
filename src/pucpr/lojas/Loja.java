package pucpr.lojas;

import static pucpr.Constantes.PORTA_GRUPO;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.List;

public class Loja extends Thread {

    private final String nome;
    private List<String> estoque;

    public Loja(String nome) {
        this.nome = nome;
        this.estoque = new ArrayList<>();
    }

    public void adicionarProduto(String produto) {
        this.estoque.add(produto);
    }

    @Override
    public void run() {
        try {
            final InetAddress byName = InetAddress.getByName("224.0.0.1");
            MulticastSocket socket = new MulticastSocket(PORTA_GRUPO);
            socket.joinGroup(byName);

            DatagramPacket datagramPacket = new DatagramPacket(new byte[4096], 4096);

            while (true) {
                socket.receive(datagramPacket);
                System.out.printf("%s recebeu %s%n", nome, new String(datagramPacket.getData(), datagramPacket.getOffset(), datagramPacket.getLength()));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Loja l1 = new Loja("Loja 1");
        l1.adicionarProduto("Picole");
        l1.adicionarProduto("Bolo");
        l1.adicionarProduto("Vassoura");
        l1.adicionarProduto("Teste");

        Loja l2 = new Loja("Loja 2");
        l2.adicionarProduto("Picole 2");
        l2.adicionarProduto("Bolo 2");
        l2.adicionarProduto("Vassoura 2");
        l2.adicionarProduto("Teste 2");

        l2.start();
        l1.start();
        l2.join();
        l1.join();
    }
}

