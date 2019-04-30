package com.csc495.backend.game;

import com.csc495.backend.utils.Utils;

import java.util.LinkedList;
import java.util.List;

public class Player {
    private final String name;
    private final int[] color;
    private final List<Coordinates> snake;

    private boolean isAlive;

    public Player(String name) {
        this.name = name;
        this.color = Utils.generateColor();
        this.snake = new LinkedList<>();
        this.isAlive = true;
    }

    public String getName() {
        return name;
    }

    public int[] getColor() {
        return color;
    }

    public List<Coordinates> getSnake() {
        return snake;
    }

    public void addToSnake(int x, int y) {
        final Coordinates part = new Coordinates(x, y);
        snake.add(part);
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }
}
