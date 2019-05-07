package com.csc495.backend.networking;

import com.csc495.backend.game.Game;
import com.csc495.backend.game.Player;
import com.csc495.backend.game.Spot;
import com.csc495.backend.packets.ErrorPacket;
import com.csc495.backend.packets.JoinPacket;
import com.csc495.backend.packets.PlayPacket;
import com.csc495.backend.packets.StatePacket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

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

    private void sendState(Game game, Player player) {
        final Spot[][] spots = game.getSpots();
        List<Byte> data = new ArrayList<>();

        // Gettings all the bytes of the game and adding them to an arraylist
        for (Spot[] spot : spots) {
            for (int j = 0; j < spots[0].length; j++) {
                for (int k = 0; k < spot[j].getByteArray().length; k++) {
                    data.add(spot[j].getByteArray()[k]);
                }
            }
        }

        // Creating the state packets to send
        final short totalPacketsToSend = (short) Math.ceil((double) data.size() / 507);
        short i = 1;

        final List<StatePacket> statePackets = new ArrayList<>();
        while (true) {
            final StatePacket statePacket = new StatePacket(player, i, totalPacketsToSend);
            if (data.size() >= 507) {
                statePacket.addStateData(data.subList(0, 507));
                statePackets.add(statePacket);
                data = data.subList(507, data.size());
            } else {
                statePacket.addStateData(data);
                statePackets.add(statePacket);
                break;
            }
            i++;
        }

        // Sending the state packets
        for (StatePacket statePacket : statePackets) { // TODO: Timeout check
            sendPacket(statePacket.createUnicastPacket());
            // TODO: Receive the ACKS back from this specific client only somehow
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
            final DatagramPacket receivedPacket = receivePacket(); // we actually receive the data here

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

                    sendState(game, newPlayer);
                    break;
                case 3: // Play packet
                    System.out.println("Play packet");
                    final PlayPacket p = new PlayPacket();
                    p.parseSocketData(receivedPacket);

                    sendPacket(p.createMulticastPacket(group, 4446));
                    break;
                case 5: // Heartbeat packet
                    System.out.println("Heartbeat packet");
                    final boolean heartbeatUpdated = game.updatePlayerHeartbeat(receivedPacket.getAddress());

                    if (!heartbeatUpdated) {
                        final ErrorPacket e = new ErrorPacket("User is not properly connected.  Please exit and try again to authenticate.", receivedPacket);
                        sendPacket(e.createUnicastPacket());
                    }
                    break;
                default:
                    System.out.println("Received unknown code: " + receivedPacket.getData()[0]);
                    final ErrorPacket e = new ErrorPacket("Unknown packet code: " + receivedPacket.getData()[0], receivedPacket);
                    sendPacket(e.createUnicastPacket());
            }
        }
    }
}
