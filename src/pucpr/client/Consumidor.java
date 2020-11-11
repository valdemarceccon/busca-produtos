package pucpr.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

import pucpr.Constantes;

public class Consumidor {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try (Socket socket = connectar()) {
            System.out.println("Bem vindo!");
            String nome = identificacao(scanner);
            System.out.println("Para sair, deixe a pesquisa em branco");
            while (socket.isConnected()) {
                String termo = readTermo(scanner);
                final ConsumidorOperations consumidorOperations = new ConsumidorOperations(socket, nome);
                final List<String> resultado = consumidorOperations.buscar(termo);
                System.out.println("Resultados da busca: ");
                for (String s : resultado) {
                    System.out.println("  - " + s);
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

    private static String identificacao(Scanner scanner) {
        System.out.print("Seu nome: ");
        final String nome = scanner.nextLine().trim();
        if (nome.isEmpty()) {
            System.out.println("Até mais");
            System.exit(0);
        }
        return nome;
    }

    private static Socket connectar() throws IOException {
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress(Constantes.PORTA_CLIENTE));
        return socket;
    }
}
