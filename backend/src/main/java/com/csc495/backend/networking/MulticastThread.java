package com.csc495.backend.networking;

import com.csc495.backend.game.Game;
import com.csc495.backend.game.Player;
import com.csc495.backend.utils.ErrorPacket;
import com.csc495.backend.utils.JoinPacket;
import com.csc495.backend.utils.Packet;
import com.csc495.backend.utils.PlayPacket;

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

    private void receivePacket(DatagramPacket packet) {
        try {
            socket.receive(packet); // we actually receive the data here
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        final Game game = new Game();
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(10000);
                    game.sweepPlayers();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        while (true) {
            final byte[] buf = new byte[Packet.SIZE];
            final DatagramPacket receivedPacket = new DatagramPacket(buf, buf.length);
            receivePacket(receivedPacket); // we actually receive the data here

            // TODO: Will split all of these into functions
            switch (receivedPacket.getData()[0]) {
                case 1: // Encryption packet
                    System.out.println("Encryption packet");
                    break;
                case 2: // Join packet
                    System.out.println("Join packet");
                    final JoinPacket j = new JoinPacket();
                    j.parseSocketData(receivedPacket);
                    final Player newPlayer = new Player(receivedPacket.getAddress(), receivedPacket.getPort(), j.getName());
                    final boolean didAddToGame = game.addPlayerToGame(newPlayer);

                    if (!didAddToGame) {
                        final ErrorPacket e = new ErrorPacket(newPlayer.getName() + " | " + newPlayer.getAddress() + " already exists in the game.", receivedPacket);
                        sendPacket(e.createUnicastPacket());
                    }
                    break;
                case 3: // Play packet
                    System.out.println("Play packet");
                    final PlayPacket p = new PlayPacket();
                    p.parseSocketData(receivedPacket);
                    break;
                case 5: // Heartbeat packet
                    System.out.println("Heartbeat packet");
                    game.updatePlayerHeartbeat(receivedPacket.getAddress());
                    // TODO: Update user that they are still connected
                    break;
                case 6: // ACK packet
                    System.out.println("ACK packet");
                    break;
                default:
                    System.out.println("Received unknown code: " + receivedPacket.getData()[0]);
                    final ErrorPacket e = new ErrorPacket("Unknown packet code: " + receivedPacket.getData()[0], receivedPacket);
                    sendPacket(e.createUnicastPacket());
            }


        }

        // TODO: Encryption

        // TODO: Other small things that we think of along the way
    }
}
