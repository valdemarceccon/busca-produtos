package pucpr.servidor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Servidor {
    public static long TMAX = 1000;
    public static List<String> historico;
    public static void main(String[] args) throws IOException, InterruptedException {
        historico = new ArrayList<>();
        final ServerAdmin serverAdmin = new ServerAdmin();
        serverAdmin.start();
        serverAdmin.join();
    }
}
