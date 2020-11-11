package pucpr.servidor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class AdminOperations extends Thread {

    private final Socket socket;

    @Override
    public void run() {
        try {
            while (socket.isConnected()) {
                final ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                AdminRequest request = (AdminRequest) inputStream.readObject();
                switch (request.tipo()) {
                    case HistoricoBusca:
                        responderHistorico();
                        break;
                    case VerTMax:
                        responderVerTMax();
                        break;
                    case AtualizarTMax:
                        atualizarTMax((AtualizarTMaxRequest) request);
                        break;
                }
            }
        } catch (IOException e) {
            //faz nada, provalmente a conexão fechou
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("Conexão fechada com o admin");
    }

    private void responderHistorico() {
        HistoricoBuscasResponse histresp = new HistoricoBuscasResponse();
        histresp.historico = Servidor.historico;
        writeOutputStream(histresp);
    }

    private void responderVerTMax() {
        TMaxResponse tMaxResponse = new TMaxResponse();
        tMaxResponse.valorAtual = Servidor.TMAX;
        writeOutputStream(tMaxResponse);
    }

    private void atualizarTMax(AtualizarTMaxRequest request) {
        try {
            Servidor.TMAX = request.getNovoValor();
            TMaxResponse tMaxResponseUpdate = new TMaxResponse();
            tMaxResponseUpdate.valorAtual = Servidor.TMAX;
            writeOutputStream(tMaxResponseUpdate);
        } catch (Exception e) {
            TMaxResponse tMaxResponseUpdate = new TMaxResponse();
            tMaxResponseUpdate.status = RequestStatus.ERRO;
            tMaxResponseUpdate.mensagem = String.format("Valor para Tmax invalido: %s", e.getMessage());
            tMaxResponseUpdate.valorAtual = Servidor.TMAX;
            writeOutputStream(tMaxResponseUpdate);
        }
    }

    private void writeOutputStream(Object object) {
        try {
            final ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(object);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public AdminOperations(Socket socket) {
        this.socket = socket;
    }

    public void close() throws IOException {
        if (socket != null && !socket.isClosed())
            socket.close();
    }
}
