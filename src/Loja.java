
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class Loja extends Thread {

    private final String nome;
    private List<Produto> estoque;

    public Loja(String nome) {
        this.nome = nome;
        this.estoque = new ArrayList<>();
    }

    public void adicionarProduto(String produto, double preco) {
        this.estoque.add(new Produto(nome, produto, preco));
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
        for (Produto produto : estoque) {
            if (produto.getNomeProduto().toUpperCase().contains(busca.getTermo().toUpperCase()))
                resultadoBusca.addResultado(produto);
        }
        try {
            final ByteArrayOutputStream out = new ByteArrayOutputStream();
            final ObjectOutputStream outputStream = new ObjectOutputStream(out);
            outputStream.writeObject(resultadoBusca);
            DatagramSocket socket = new DatagramSocket();
            DatagramPacket packet = new DatagramPacket(out.toByteArray(), out.size(), new InetSocketAddress(Constantes.PORTA_UPD));
            if (nome.equals("Loja 2")) {
                sleep(10000);
            }
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Loja l1 = new Loja("Loja 1");
        l1.adicionarProduto("Picole", 1.50);
        l1.adicionarProduto("Bolo", 50.00);
        l1.adicionarProduto("Vassoura", 10.99);

        Loja l2 = new Loja("Loja 2");
        l2.adicionarProduto("Picole", 2.00);
        l2.adicionarProduto("Bolo", 3.00);
        l2.adicionarProduto("Pão de batata", 5.00);
        l2.adicionarProduto("Torresmo", 3.00);

        Loja l3 = new Loja("Loja 3");
        l3.adicionarProduto("Picole", 10.00);
        l3.adicionarProduto("Sabonete", 3.99);
        l3.adicionarProduto("Leite condensado", 5.00);
        l3.adicionarProduto("Esfregao", 10.00);
        l3.adicionarProduto("Bombril", 0.50);

        l1.start();
        l2.start();
        l3.start();
        l1.join();
        l2.join();
        l3.join();
    }
}

