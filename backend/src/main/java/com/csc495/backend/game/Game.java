package com.csc495.backend.game;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Game {
    private static final int WIDTH = 50;
    private static final int HEIGHT = 65;

    private final Spot[][] spaces = new Spot[WIDTH][HEIGHT];
    private final List<Player> playersList = new ArrayList<>();

    public Game() {
    }

    public synchronized boolean playerIsInGame(Player newPlayer) {
        for (Player p : playersList) {
            if (p.getAddress().equals(newPlayer.getAddress())) {
                return true;
            } else if (p.getName().equals(newPlayer.getName())) {
                return true;
            }
        }

        return false;
    }

    public synchronized boolean addPlayerToGame(Player newPlayer) {
        if (!playerIsInGame(newPlayer)) {
            playersList.add(newPlayer);
            System.out.println(newPlayer.getName() + " has connected.");

            return true;
        }

        return false;
    }

    public synchronized void updatePlayerHeartbeat(InetAddress address) {
        for (Player p : playersList) {
            if (p.getAddress().equals(address)) {
                p.setHasHeartbeat(true);
                System.out.println(p.getName() + " heartbeat detected.");

                return;
            }
        }
    }

    public synchronized void sweepPlayers() {
        Iterator<Player> playerIterator = playersList.iterator();
        while (playerIterator.hasNext()) {
            final Player player = playerIterator.next();
            if (player.hasHeartbeat()) {
                player.setHasHeartbeat(false);
            } else {
                System.out.println(player.getName() + " has disconnected.");
                playerIterator.remove();
            }
        }
    }
}
