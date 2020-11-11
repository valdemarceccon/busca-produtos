package pucpr.lojas;

import static pucpr.Constantes.PORTA_GRUPO;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

import pucpr.Constantes;
import pucpr.servidor.Busca;
import pucpr.servidor.ResultadoBusca;

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
                final ObjectInputStream inputStream = new ObjectInputStream(new ByteArrayInputStream(datagramPacket.getData()));
                final Busca busca = (Busca) inputStream.readObject();
                System.out.printf("%s recebeu %s do usuário %s%n", nome, busca.getTermo(), busca.getUsuario());
                enviarParaServer(busca);
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void enviarParaServer(Busca busca) {
        StringBuilder r = new StringBuilder();
        final ResultadoBusca resultadoBusca = new ResultadoBusca(busca.getUsuario());
        for (String s : estoque) {
            if (s.toUpperCase().contains(busca.getTermo().toUpperCase()))
            resultadoBusca.addResultado(s);
        }
        try {
            final ByteArrayOutputStream out = new ByteArrayOutputStream();
            final ObjectOutputStream outputStream = new ObjectOutputStream(out);
            outputStream.writeObject(resultadoBusca);
            DatagramSocket socket = new DatagramSocket();
            DatagramPacket packet = new DatagramPacket(out.toByteArray(), out.size(), new InetSocketAddress(Constantes.PORTA_UPD));
//            packet.setData(r.toString().getBytes(), 0, r.length());
            socket.send(packet);
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

