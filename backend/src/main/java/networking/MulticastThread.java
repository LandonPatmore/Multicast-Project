package networking;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MulticastThread implements Runnable {

    private static final int PORT = 4445;

    private final InetAddress group;
    private final MulticastSocket socket;

    public MulticastThread() throws IOException {
        this.group = InetAddress.getByName("224.0.0.192");
        this.socket = new MulticastSocket(PORT);
    }

    @Override
    public void run() {
        // TODO: Need to create the game state

        // TODO: Encryption

        // TODO: Start listening for packets, have a way to distinguish different types of packets from each other

        // TODO: When packets for games come in, do something with them, then multicast to all clients what has happened

        // TODO: Deal with disconnections

        // TODO: Other small things that we think of along the way
    }
}
