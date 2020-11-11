package pucpr.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import pucpr.servidor.*;

public class AdminOperations {
    private final Socket socket;
    private final Scanner scanner;

    public AdminOperations(Socket socket, Scanner scanner) {
        this.socket = socket;
        this.scanner = scanner;
    }

    public void historicoBusca() {
        try {
            write(new HistoricoBuscasRequest());
            HistoricoBuscasResponse response = (HistoricoBuscasResponse) read();
            System.out.println("Histórico de buscas: ");
            for (Busca busca : response.historico) {
                System.out.printf("%s buscou por \"%s\" no dia %s%n", busca.getUsuario(), busca.getTermo(), busca.getTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void verTMax() {
        try {
            write(new VerTMaxRequest());
            TMaxResponse response = (TMaxResponse) read();
            System.out.printf("Valor atual do TMax: %d%n", response.valorAtual);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void updateTMax() {
        System.out.print("Novo valor para Tmax: ");
        String valor = scanner.nextLine();
        if (valor.trim().isEmpty()) {
            System.out.println("Tmax vazio.");
            return;
        }
        try {
            final long l = Long.parseLong(valor.trim());
            AdminRequest request = new AtualizarTMaxRequest(l);
            write(request);
            final TMaxResponse read = (TMaxResponse) read();
            if (read.status == RequestStatus.ERRO) {
                System.out.printf("Não foi possível atualizar TMax: %s%n", read.mensagem);
            } else if (read.status == RequestStatus.SUCESSO) {
                System.out.printf("Valor atualizado com sucesso TMax: %d%n", read.valorAtual);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void write(Object obj) throws IOException {
        final ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
        outputStream.writeObject(obj);
    }

    private Object read() throws IOException, ClassNotFoundException {
        final ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
        return inputStream.readObject();
    }
}
