package pucpr.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

import pucpr.Constantes;
import pucpr.servidor.AdminOpcoes;
import pucpr.servidor.AdminRequest;
import pucpr.servidor.Response;

public class Admin {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Bem vindo ao terminal de admin");
        while (true) {
            String s = menu(scanner);
            Runnable op = getOpcao(s, scanner);

            while (op == null) {
                System.out.println("Općão invalida. Pressione qualquer tecla para continuar");
                scanner.nextLine();
                s = menu(scanner);
                op = getOpcao(s, scanner);
            }
            op.run();
        }
    }

    private static String menu(Scanner scanner) {

        System.out.println("Por favor, seleciona uma općão: ");
        System.out.println("1 - Histórico buscas");
        System.out.println("2 - Visualizar Tmax");
        System.out.println("3 - Update Tmax");
        System.out.println("");
        System.out.print("Općão: ");

        return scanner.nextLine();
    }

    private static Runnable getOpcao(String s, Scanner scanner) {
        if (s == null || s.trim().isEmpty()) {
            return null;
        }

        if (s.trim().equals("1")) {
            return () -> chamarServidor(new AdminRequest(AdminOpcoes.HistoricoBusca));
        }

        if (s.trim().equals("2")) {
            return () -> chamarServidor(new AdminRequest(AdminOpcoes.VerTMax));
        }

        if (s.trim().equals("3")) {
            System.out.print("Novo valor para Tmax: ");
            String valor = scanner.nextLine();
            if (valor.trim().isEmpty()) {
                System.out.println("Tmax vazio.");
                return null;
            }

            return () -> chamarServidor(new AdminRequest(AdminOpcoes.AtualizarTMax, valor));
        }

        if (s.trim().equals("0")) {
            System.out.println("Até mais");
            System.exit(0);
        }

        return null;
    }

    public static void chamarServidor(AdminRequest request) {
        try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(Constantes.PORTA_ADMIN));
            try (final ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream())) {
                objectOutputStream.writeObject(request);
                objectOutputStream.flush();
                try (final ObjectInputStream ois = new ObjectInputStream(socket.getInputStream())) {
                    final Response response = (Response) ois.readObject();
                    if (response.valor == null || response.valor.isEmpty()) {
                        System.out.println("Servidor respondeu nada");
                    } else {
                        System.out.println("Resultado: ");
                        for (String s1 : response.valor) {
                            System.out.println(s1);
                        }
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
