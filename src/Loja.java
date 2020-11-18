
import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Loja {

    private final String nome;
    private List<Produto> estoque;

    public Loja(String nome) {
        this.nome = nome;
        this.estoque = new ArrayList<>();
    }

    public void adicionarProduto(String produto, double preco) {
        this.estoque.add(new Produto(nome, produto, preco));
    }

    public void iniciarServer() {
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
                Thread.sleep(10000);
            }
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException, IOException {

        if(args.length == 0) {
            System.out.println("Nome da loja obrigatório");
            System.exit(1);
        }

        final String arg = args[0];

        Loja loja = new Loja(arg);

        final Path produtos = Paths.get(String.format("%s.csv", loja.nome));
        if (!Files.exists(produtos)) {
            System.out.printf("Não existem produtos cadastrados para a loja %s%n", loja.nome);
        }

        loja.loadProdutos();

        loja.iniciarServer();
    }

    private void loadProdutos() throws IOException {
        final Path produtos = Paths.get(String.format("%s.csv", nome));
        for (String p : Files.readAllLines(produtos)) {
            if (p != null && !p.trim().isEmpty()) {
                final String[] split = p.split(",");
                String nome = split[0].trim();
                String valor = split[1].trim();
                adicionarProduto(nome, Double.parseDouble(valor));
            }
        }
    }
}

