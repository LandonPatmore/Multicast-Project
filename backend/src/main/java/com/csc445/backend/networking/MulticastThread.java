package com.csc445.backend.networking;

import com.csc445.backend.game.Game;
import com.csc445.shared.game.Player;
import com.csc445.shared.game.Spot;
import com.csc445.shared.packets.*;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MulticastThread implements Runnable {

    private static final int PORT = 4445;

    private final InetAddress group;
    private final DatagramSocket socket;

    private final Game game;
    private final HashMap<Integer, PlayPacket> plays;

    private int currentPlayNumber = 1;

    public MulticastThread() throws IOException {
        this.group = InetAddress.getByName("224.0.0.192");
        this.socket = new DatagramSocket(PORT);
        this.game = new Game();
        this.plays = new HashMap<>();
    }

    private void sendPacket(DatagramPacket packet) {
        new Thread(() -> {
            try {
                socket.send(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private DatagramPacket receivePacket() {
        try {
            final byte[] buf = new byte[512];
            final DatagramPacket packet = new DatagramPacket(buf, buf.length);
            socket.receive(packet); // we actually receive the data here
            return packet;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void processJoinPacket(DatagramPacket packet) {
        System.out.println("Join packet");
        final JoinPacket j = new JoinPacket();
        j.parseSocketData(packet);
        final Player newPlayer = new Player(packet.getAddress(), packet.getPort(), j.getName());
        final boolean didAddToGame = game.addPlayerToGame(newPlayer);

        if (!didAddToGame) {
            final ErrorPacket e = new ErrorPacket(newPlayer.getName() + " | " + newPlayer.getAddress() + " already exists in the game.", packet);
            sendPacket(e.createUnicastPacket());
        } else {
            final MessagePacket m = new MessagePacket(newPlayer.getName() + " has joined the game.");
            sendPacket(m.createMulticastPacket(group, 4446));
        }
    }

    private void processPlayPacket(DatagramPacket packet) {
        System.out.println("Play packet");
        final PlayPacket p = new PlayPacket();
        p.parseSocketData(packet);
        addPlay(p);

        sendPacket(p.createMulticastPacket(group, 4446));
    }

    private void processHeartbeatPacket(DatagramPacket packet) {
        System.out.println("Heartbeat packet");
        final boolean heartbeatUpdated = game.updatePlayerHeartbeat(packet.getAddress());

        if (!heartbeatUpdated) {
            final ErrorPacket e = new ErrorPacket("User is not properly connected.  Please exit and try again to authenticate.", packet);
            sendPacket(e.createUnicastPacket());
        }
    }

    private void processStateRequestPacket(DatagramPacket packet) {
        System.out.println("State request packet");
        final StateRequestPacket s = new StateRequestPacket();
        s.parseSocketData(packet);

        final PlayPacket playPacket = getPlay(s.getPlayNumber());

        sendPacket(playPacket.createUnicastPacket(packet.getAddress(), packet.getPort()));
    }

    private void processInvalidPacket(DatagramPacket packet) {
        System.out.println("Received unknown code: " + packet.getData()[0]);
        final ErrorPacket e = new ErrorPacket("Unknown packet code: " + packet.getData()[0], packet);
        sendPacket(e.createUnicastPacket());
    }

    private void addPlay(PlayPacket p) {
        plays.put(currentPlayNumber, p);
        currentPlayNumber++;
    }

    private PlayPacket getPlay(int playNumber) {
        return plays.get(playNumber);
    }

    @Override
    public void run() {

        System.out.println("Server started...");

        while (true) {
            final DatagramPacket receivedPacket = receivePacket(); // we actually receive the data here

            if (receivedPacket != null) {
                switch (receivedPacket.getData()[0]) {
                    case 1: // Join packet
                        processJoinPacket(receivedPacket);
                        break;
                    case 2: // Play packet
                        processPlayPacket(receivedPacket);
                        break;
                    case 3: // Heartbeat packet
                        processHeartbeatPacket(receivedPacket);
                        break;
                    case 6: // State request packet
                        processStateRequestPacket(receivedPacket);
                    default:
                        processInvalidPacket(receivedPacket);
                }
            }
        }
    }
}
