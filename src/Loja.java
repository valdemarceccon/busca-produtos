
import java.io.*;
import java.net.*;
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
            MulticastSocket socket = new MulticastSocket(Constantes.PORTA_GRUPO);
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
        final ResultadoBusca resultadoBusca = new ResultadoBusca(this.nome, busca.getUsuario());
        for (String s : estoque) {
            if (s.toUpperCase().contains(busca.getTermo().toUpperCase()))
                resultadoBusca.addResultado(new Produto(this.nome, s));
        }
        try {
            final ByteArrayOutputStream out = new ByteArrayOutputStream();
            final ObjectOutputStream outputStream = new ObjectOutputStream(out);
            outputStream.writeObject(resultadoBusca);
            DatagramSocket socket = new DatagramSocket();
            DatagramPacket packet = new DatagramPacket(out.toByteArray(), out.size(), new InetSocketAddress(Constantes.PORTA_UPD));
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

        Loja l2 = new Loja("Loja 2");
        l2.adicionarProduto("Picole");
        l2.adicionarProduto("Bolo");
        l2.adicionarProduto("Pão de batata");
        l2.adicionarProduto("Torresmo");

        Loja l3 = new Loja("Loja 3");
        l3.adicionarProduto("Picole");
        l3.adicionarProduto("Sabonete");
        l3.adicionarProduto("Leite condensado");
        l3.adicionarProduto("Esfregao");
        l3.adicionarProduto("Bombril");

        l1.start();
        l2.start();
        l3.start();
        l1.join();
        l2.join();
        l3.join();
    }
}
