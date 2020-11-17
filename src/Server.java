import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

public class Server {
    public static long TMAX = 1000;
    public static List<Busca> historico;
    public static Semaphore mutexHist = new Semaphore(1);

    public static Map<String, ResultadoBusca> pollResultados = new HashMap<>();
    public static Semaphore mutexPollResultados = new Semaphore(1);

    public static void main(String[] args) throws InterruptedException {
        historico = new ArrayList<>();
        final AdminServer serverAdmin = new AdminServer();
        final ConsumidorServer serverConsumidor = new ConsumidorServer();
        final UPDServer updServer = new UPDServer();
        serverAdmin.start();
        serverConsumidor.start();
        updServer.start();
        updServer.join();
        serverAdmin.join();
        serverConsumidor.join();
    }

    public static void salvarTermoNoHistorico(String user, String termo) {
        try {
            mutexHist.acquire();
            historico.add(new Busca(LocalDateTime.now(), termo, user));
            mutexHist.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public static void limparPoll(String user) {
        try {
            mutexPollResultados.acquire();
            pollResultados.remove(user);
            mutexPollResultados.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void adicionar(ResultadoBusca resp) {
        try {
            mutexPollResultados.acquire();
            if (pollResultados.containsKey(resp.getIdentificacao())) {
                final ResultadoBusca resultadoBusca = pollResultados.get(resp.getIdentificacao());
                resultadoBusca.getResultado().addAll(resp.getResultado());
            } else {
                pollResultados.put(resp.getIdentificacao(), resp);
            }
            mutexPollResultados.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
