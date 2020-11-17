import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;
import java.util.UUID;

public class Consumidor {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try (Socket socket = connectar()) {
            System.out.println("Bem vindo!");
            System.out.println("Para sair, deixe a pesquisa em branco");
            final String identificacao = UUID.randomUUID().toString();
            while (socket.isConnected()) {
                String termo = readTermo(scanner);
                final ConsumidorOperationsClient consumidorOperations = new ConsumidorOperationsClient(socket, identificacao);
                final ResultadoBusca resultado = consumidorOperations.buscar(termo);
                System.out.println("Resultados da busca: ");
                for (Produto s : resultado.getResultado()) {
                    System.out.printf("%s - %s%n", s.getNomeLoja(), s.getNomeProduto());
                }
                System.out.println();
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Não foi possível conectar ao servidor");
        }
    }

    private static String readTermo(Scanner scanner) {
        System.out.print("Digite o termo para pesquisa: ");
        final String termo = scanner.nextLine().trim();
        if (termo.isEmpty()) {
            System.out.println("Até mais");
            System.exit(0);
        }
        return termo;
    }

    private static Socket connectar() throws IOException {
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress(Constantes.PORTA_CLIENTE));
        return socket;
    }
}
