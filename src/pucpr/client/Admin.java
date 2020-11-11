package pucpr.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

import pucpr.Constantes;
import pucpr.servidor.*;

public class Admin {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try (Socket socket = connectar()) {
            System.out.println("Bem vindo ao terminal de admin");
            while (socket.isConnected()) {
                String opts = menu(scanner);
                AdminOperations operations = new AdminOperations(socket, scanner);
                boolean opaoValida = execOpcao(opts, operations);

                while (!opaoValida) {
                    System.out.println("Općão invalida. Pressione qualquer tecla para continuar");
                    scanner.nextLine();
                    opts = menu(scanner);
                    opaoValida = execOpcao(opts, operations);
                }
            }
        } catch (IOException e) {
            System.out.println("Não foi possível conectar ao servidor");
        }
    }

    private static String menu(Scanner scanner) {

        System.out.println("Por favor, seleciona uma općão: ");
        System.out.println("1 - Histórico buscas");
        System.out.println("2 - Visualizar Tmax");
        System.out.println("3 - Update Tmax");
        System.out.println("0 - Sair");
        System.out.println("");
        System.out.print("Općão: ");

        return scanner.nextLine();
    }

    private static boolean execOpcao(String opts, AdminOperations admOp) {
        if (opts == null || opts.trim().isEmpty()) {
            return false;
        }

        if (opts.trim().equals("1")) {
            admOp.historicoBusca();
            return true;
        }

        if (opts.trim().equals("2")) {
            admOp.verTMax();
            return true;
        }

        if (opts.trim().equals("3")) {
            admOp.updateTMax();
            return true;
        }

        if (opts.trim().equals("0")) {
            System.out.println("Até mais");
            System.exit(0);
        }

        return false;
    }

    private static Socket connectar() throws IOException {
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress(Constantes.PORTA_ADMIN));
        return socket;
    }

}
