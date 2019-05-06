package com.csc495.backend.game;

public class Spot {
    private String name;
    private int color;
    private int x;
    private int y;

    public Spot() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getColor() {
        return color;
    }

    /**
     * Colors:
     * 0. RED,
     * 1. ORANGE,
     * 2. YELLOW,
     * 3. GREEN,
     * 4. BLUE,
     * 5. PURPLE,
     * 6. WHITE,
     * 7. BLACK,
     * 8. PINK,
     * 9. TEAL
     *
     * @param color the number of the color
     */
    public void setColor(int color) {
        this.color = color;
    }
}
