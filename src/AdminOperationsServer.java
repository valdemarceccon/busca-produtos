import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class AdminOperationsServer extends Thread {

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
        histresp.historico = Server.historico;
        writeOutputStream(histresp);
    }

    private void responderVerTMax() {
        TMaxResponse tMaxResponse = new TMaxResponse();
        tMaxResponse.valorAtual = Server.TMAX;
        writeOutputStream(tMaxResponse);
    }

    private void atualizarTMax(AtualizarTMaxRequest request) {
        try {
            Server.TMAX = request.getNovoValor();
            TMaxResponse tMaxResponseUpdate = new TMaxResponse();
            tMaxResponseUpdate.valorAtual = Server.TMAX;
            writeOutputStream(tMaxResponseUpdate);
        } catch (Exception e) {
            TMaxResponse tMaxResponseUpdate = new TMaxResponse();
            tMaxResponseUpdate.status = RequestStatus.ERRO;
            tMaxResponseUpdate.mensagem = String.format("Valor para Tmax invalido: %s", e.getMessage());
            tMaxResponseUpdate.valorAtual = Server.TMAX;
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

    public AdminOperationsServer(Socket socket) {
        this.socket = socket;
    }

    public void close() throws IOException {
        if (socket != null && !socket.isClosed())
            socket.close();
    }
}
