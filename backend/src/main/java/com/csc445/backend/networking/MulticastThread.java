package com.csc445.backend.networking;

import com.csc445.backend.game.Game;
import com.csc445.backend.utils.Utils;
import com.csc445.shared.game.Player;
import com.csc445.shared.packets.*;
import com.csc445.shared.utils.AES;
import com.csc445.shared.utils.Constants;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.security.InvalidKeyException;
import java.util.Arrays;
import java.util.HashMap;

public class MulticastThread implements Runnable {

    private final InetAddress group;
    private final DatagramSocket socket;

    private final Game game;
    private final HashMap<Integer, PlayPacket> plays;

    private int currentPlayNumber = 1;

    private final String secretKey;

    public MulticastThread() throws IOException {
        this.secretKey = AES.TEST_PASSWORD;

        this.group = InetAddress.getByName(Constants.GROUP_ADDRESS);
        this.socket = new DatagramSocket(Constants.SERVER_PORT);
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
            sendPacket(e.createPacket(secretKey));
        } else {
            final MessagePacket m = new MessagePacket(newPlayer.getName() + " has joined the game.");
            sendPacket(m.createPacket(group, 4446, secretKey));
        }
    }

    private void processPlayPacket(DatagramPacket packet) {
        System.out.println("Play packet");

        final PlayPacket p = new PlayPacket();
        p.parseSocketData(packet);
        addPlay(p);
        game.updateSpot(p.getSpot());


        sendPacket(p.createPacket(group, 4446, secretKey));
    }

    private void processHeartbeatPacket(DatagramPacket packet) {
        System.out.println("Heartbeat packet");

        final boolean heartbeatUpdated = game.updatePlayerHeartbeat(packet.getAddress());

        if (!heartbeatUpdated) {
            final ErrorPacket e = new ErrorPacket("User is not properly connected.  Please exit and try again to authenticate.", packet);
            sendPacket(e.createPacket(secretKey));
        }
    }

    private void processStateRequestPacket(DatagramPacket packet) {
        System.out.println("State request packet");

        final StateRequestPacket s = new StateRequestPacket();
        s.parseSocketData(packet);

        final PlayPacket playPacket = getPlay(s.getPlayNumber());

        sendPacket(playPacket.createPacket(packet.getAddress(), packet.getPort(), secretKey));
    }

    private void processInvalidPacket(DatagramPacket packet) {
        System.out.println("Received unknown code: " + packet.getData()[0]);

        final ErrorPacket e = new ErrorPacket("Unknown packet code: " + packet.getData()[0], packet);
        sendPacket(e.createPacket(secretKey));
    }

    private void processWrongPassword(DatagramPacket packet) {
        System.out.println("Cannot decrypt packet from " + packet.getAddress());

        final ErrorPacket e = new ErrorPacket("Cannot decrypt packet", packet);

        sendPacket(e.createPacket(secretKey));
    }

    private void addPlay(PlayPacket p) {
        plays.put(currentPlayNumber, p);
        currentPlayNumber++;
    }

    private PlayPacket getPlay(int playNumber) {
        return plays.get(playNumber);
    }

	/**
	 * Does what it says on the tin
	 *
	 * Payload of <code>packet</code> is trimmed from offset to length of data received
	 * because otherwise it would mess up the padding and make <code>AES.decryptByteArray</code>
	 * throw a hissy fit
	 *
	 * @param packet packet to be decrypted
	 * @return decrypted payload of <code>packet</code>, or <code>null</code> if decryption failed
	 */
	private byte[] decryptPacket(DatagramPacket packet) {
		try {
			return AES.decryptByteArray(Arrays.copyOfRange(packet.getData(),
					packet.getOffset(), packet.getLength()), secretKey);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		}
		return null;
	}

    @Override
    public void run() {

        System.out.println("Server started...");
        System.out.println("Mutlicast group: " + group);
        System.out.println("Password: " + secretKey);

        while (true) {
            final DatagramPacket receivedPacket = receivePacket(); // we actually receive the data here
            if (receivedPacket != null) {
                final byte[] decryptedData = decryptPacket(receivedPacket);

                if (decryptedData == null) {
                    processWrongPassword(receivedPacket);
                } else {
                	receivedPacket.setData(decryptedData);
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
                            break;
                        default:
                            processInvalidPacket(receivedPacket);
                    }
                }
            }
        }
    }
}
