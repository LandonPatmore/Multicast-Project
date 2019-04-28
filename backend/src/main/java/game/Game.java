package game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game {
    private static final int HEIGHT = 20;
    private static final int WIDTH = 20;
    private static final int MAX_PLAYERS = 4;
    private final List<Player> playersList = new ArrayList<>();

    public Game() {
    }

    public boolean isFull() {
        return playersList.size() == MAX_PLAYERS;
    }

    public void addPlayerToGame(Player player) {
        playersList.add(player);
    }

    public void removePlayerFromGame() { // TODO: Figure out how to distinguish players from each other (probably their IP, etc.)

    }

    public Coordinates generateFood() {
        final Random r = new Random();
        final int x = r.nextInt(WIDTH);
        final int y = r.nextInt(HEIGHT);

        return new Coordinates(x, y);
    }
}
