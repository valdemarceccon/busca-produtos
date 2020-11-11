package pucpr.servidor;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class Servidor {
    public static long TMAX = 1000;
    public static List<Busca> historico;
    public static Semaphore mutexHist = new Semaphore(1);
    public static void main(String[] args) throws InterruptedException {
        historico = new ArrayList<>();
        final ServerAdmin serverAdmin = new ServerAdmin();
        final ServerConsumidor serverConsumidor = new ServerConsumidor();
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
}
