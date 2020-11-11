package pucpr.servidor;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalTime;
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
                new AdminOperations(socket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
