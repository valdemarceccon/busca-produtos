package pucpr;

import java.io.IOException;
import java.io.InputStream;

public class Util {
    public static String read(InputStream is) throws IOException {
        StringBuilder msg = new StringBuilder();
        byte[] buffer = new byte[1024];
        int n = is.read(buffer);
        while (n > 0) {
            msg.append(new String(buffer, 0, n));
            n = is.read(buffer);
        }

        return msg.toString();
    }
}
