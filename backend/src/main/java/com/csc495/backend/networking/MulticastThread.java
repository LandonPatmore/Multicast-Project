package com.csc495.backend.networking;

import com.csc495.backend.game.Game;
import com.csc495.backend.utils.DataPacket;
import com.csc495.backend.utils.EncryptionPacket;
import com.csc495.backend.utils.ErrorPacket;
import com.csc495.backend.utils.Packet;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;

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

            Packet p;
            switch (receivedPacket.getData()[0]) {
                case 1:
                    p = new EncryptionPacket(); // we need to unicast this
                    p.parsePacket(receivedPacket.getData());
                    // TODO: Encryption method
                    break;
                case 2:
                    p = new DataPacket(); // we need to multicast this
                    p.parsePacket(receivedPacket.getData());
                    // TODO: Data method
                    break;
                case 3:
                    p = new ErrorPacket(); // we need to unicast this and multicast as well
                    p.parsePacket(receivedPacket.getData());
                    // TODO: Error method
                    break;
                default:
                    System.out.println(new String(receivedPacket.getData(), 0, receivedPacket.getLength()));
                    final String toClient = "Message from server";
                    try {
                        socket.send(new DatagramPacket(toClient.getBytes(), toClient.length(), group, 4446));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
//                    System.out.println("received packet");
//                    System.err.println("Packet could not be parsed properly");
//                    return;
            }


        }

        // TODO: Encryption

        // TODO: When packets for games come in, do something with them, then multicast to all clients what has happened

        // TODO: Deal with disconnections (timeout somehow)

        // TODO: Other small things that we think of along the way
    }
}
