import networking.MulticastThread;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        new Thread(new MulticastThread()).start();
    }
}
