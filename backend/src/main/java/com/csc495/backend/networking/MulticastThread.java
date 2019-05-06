package com.csc495.backend.networking;

import com.csc495.backend.game.Game;
import com.csc495.backend.utils.EncryptionPacket;
import com.csc495.backend.utils.JoinPacket;
import com.csc495.backend.utils.Packet;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class MulticastThread implements Runnable {

    private static final int PORT = 4445;

    private final InetAddress group;
    private final DatagramSocket socket;

    public MulticastThread() throws IOException {
        this.group = InetAddress.getByName("224.0.0.192");
        this.socket = new DatagramSocket(PORT);
    }

    private boolean sendPacket(DatagramPacket packet) {
        try {
            socket.send(packet);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public void run() {
        final Game game = new Game();

        while (true) {
            final byte[] buf = new byte[Packet.SIZE];
            final DatagramPacket receivedPacket = new DatagramPacket(buf, buf.length);
            try {
                socket.receive(receivedPacket); // we actually receive the data here
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            switch (receivedPacket.getData()[0]) {
                case 1: // Encryption packet | Unicast
                    System.out.println("Encryption packet");
                    break;
                case 2: // Join packet | Multicast
                    System.out.println("Join packet");
                    final JoinPacket j = new JoinPacket();
                    j.parseSocketData(receivedPacket);
                    System.out.println(j.getName());
                    break;
                case 3: // Play packet | Multicast
                    System.out.println("Play packet");
                    break;
                case 4: // State packet | Unicast
                    System.out.println("State packet");
                    break;
                case 5: // Heartbeat packet | Unicast
                    System.out.println("Heartbeat packet");
                    break;
                case 6: // ACK packet | Unicast
                    System.out.println("ACK packet");
                    break;
                case 7: // Error packet | Unicast/Mutlicast
                    System.out.println("Error packet");
                    break;
                default:
                    System.out.println("Unknown code: " + receivedPacket.getData()[0]);
                    return;
            }


        }

        // TODO: Encryption

        // TODO: When packets for games come in, do something with them, then multicast to all clients what has happened

        // TODO: Deal with disconnections (timeout somehow)

        // TODO: Other small things that we think of along the way
    }
}
