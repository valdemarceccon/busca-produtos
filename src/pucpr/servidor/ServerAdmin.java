package pucpr.servidor;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import pucpr.Constantes;

public class ServerAdmin extends Thread {

    @Override
    public void run() {
        try {
            ServerSocket s = new ServerSocket();
            s.bind(new InetSocketAddress(Constantes.PORTA_ADMIN));
            System.out.println("Servidor admin inciado");
            while (true) {
                final Socket socket = s.accept();
                System.out.println("Recebida conexão com admin");
                new Thread(() -> conversar(socket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void conversar(Socket s) {

        try (final ObjectInputStream inputStream = new ObjectInputStream(s.getInputStream());
             final ObjectOutputStream outputStream = new ObjectOutputStream(s.getOutputStream());) {
            AdminRequest request = (AdminRequest) inputStream.readObject();
            System.out.println(request);
            System.out.printf("ADMIN: (%s) %s%n", request.opcao.toString(), request.valor);
            List<String> hist = null;
            Response resp = null;
            switch (request.opcao) {
                case HistoricoBusca:
                    hist = new ArrayList<>();
                    hist.add("Teste 1");
                    hist.add("Teste 2");
                    hist.add("Teste 3");
                    hist.add("Teste 4");

                    resp = new Response();
                    resp.valor = hist;
                    outputStream.writeObject(resp);
                    outputStream.flush();
                    break;
                case VerTMax:
                    hist = new ArrayList<>();
                    hist.add(String.format("Tmax: %d", Servidor.TMAX));
                    resp = new Response();
                    resp.valor = hist;
                    outputStream.writeObject(resp);
                    outputStream.flush();
                    break;
                case AtualizarTMax:
                    try {
                        Servidor.TMAX = Long.parseLong(request.valor);
                        hist = new ArrayList<>();
                        hist.add(String.format("Tmax atualizado para: %d", Servidor.TMAX));
                        resp = new Response();
                        resp.valor = hist;
                        outputStream.writeObject(resp);
                        outputStream.flush();
                    } catch (Exception e) {
                        System.out.println();
                        hist = new ArrayList<>();
                        hist.add(String.format("Valor para Tmax invalido: %s", e.getMessage()));
                        resp = new Response();
                        resp.valor = hist;
                        outputStream.writeObject(resp);
                        outputStream.flush();
                    }
                    break;

            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
