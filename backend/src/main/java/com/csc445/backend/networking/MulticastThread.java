package com.csc445.backend.networking;

import com.csc445.backend.game.Game;
import com.csc445.shared.game.Player;
import com.csc445.shared.game.Spot;
import com.csc445.shared.packets.ErrorPacket;
import com.csc445.shared.packets.JoinPacket;
import com.csc445.shared.packets.PlayPacket;
import com.csc445.shared.packets.StatePacket;

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

    private void sendPacket(DatagramPacket packet) {
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
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

        // Getting all the bytes of the game and adding them to an ArrayList
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
            // TODO: Receive the ACKs back from this specific client only somehow
        }
    }

    private void processJoinPacket(Game game, DatagramPacket packet) {
        System.out.println("Join packet");
        final JoinPacket j = new JoinPacket();
        j.parseSocketData(packet);
        final Player newPlayer = new Player(packet.getAddress(), packet.getPort(), j.getName());
        final boolean didAddToGame = game.addPlayerToGame(newPlayer);

        if (!didAddToGame) {
            final ErrorPacket e = new ErrorPacket(newPlayer.getName() + " | " + newPlayer.getAddress() + " already exists in the game.", packet);
            sendPacket(e.createUnicastPacket());
        }

        sendState(game, newPlayer);
    }

    private void processPlayPacket(DatagramPacket packet) {
        System.out.println("Play packet");
        final PlayPacket p = new PlayPacket();
        p.parseSocketData(packet);

        sendPacket(p.createMulticastPacket(group, 4446));
    }

    private void processHeartbeatPacket(Game game, DatagramPacket packet) {
        System.out.println("Heartbeat packet");
        final boolean heartbeatUpdated = game.updatePlayerHeartbeat(packet.getAddress());

        if (!heartbeatUpdated) {
            final ErrorPacket e = new ErrorPacket("User is not properly connected.  Please exit and try again to authenticate.", packet);
            sendPacket(e.createUnicastPacket());
        }
    }

    private void processInvalidPacket(DatagramPacket packet) {
        System.out.println("Received unknown code: " + packet.getData()[0]);
        final ErrorPacket e = new ErrorPacket("Unknown packet code: " + packet.getData()[0], packet);
        sendPacket(e.createUnicastPacket());
    }

    @Override
    public void run() {
        final Game game = new Game();

        while (true) {
            final DatagramPacket receivedPacket = receivePacket(); // we actually receive the data here

            switch (receivedPacket.getData()[0]) {
                case 2: // Join packet
                    processJoinPacket(game, receivedPacket);
                    break;
                case 3: // Play packet
                    processPlayPacket(receivedPacket);
                    break;
                case 5: // Heartbeat packet
                    processHeartbeatPacket(game, receivedPacket);
                    break;
                default:
                    processInvalidPacket(receivedPacket);
            }
        }
    }
}
