package com.csc495.backend.game;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private static final int WIDTH = 50;
    private static final int HEIGHT = 65;

    private final Spot[][] spaces = new Spot[WIDTH][HEIGHT];
    private final List<Player> playersList = new ArrayList<>();

    public Game() {
    }

    public void addPlayerToGame(Player player) {
        playersList.add(player);
    }

    public void removePlayerFromGame() { // TODO: Figure out how to distinguish players from each other (probably their IP, etc.)

    }
}
