package com.csc495.backend.game;

public class Spot {
    private final String name;
    private final int x;
    private final int y;

    public Spot(String name, int x, int y) {
        this.name = name;
        this.x = x;
        this.y = y;
    }

    public String getName() {
        return name;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
