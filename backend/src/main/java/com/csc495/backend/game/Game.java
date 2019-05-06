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

    public synchronized void addPlayerToGame(Player player) {
        playersList.add(player);
        System.out.println(player.getName() + " has connected.");
    }

    public synchronized void updatePlayerHeartbeat(InetAddress address) {
        for (Player p : playersList) {
            if (p.getAddress().equals(address)) {
                p.setHasHeartbeat(true);
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
