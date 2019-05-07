package com.csc495.backend.game;

import java.util.ArrayList;
import java.util.List;

public class Spot {
    private String name;
    private byte color;
    private int x;
    private int y;

    public Spot() {
    }

    public Spot(int x, int y) {
        this.x = x;
        this.y = y;
        this.color = 6; // white
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

    public void setX(byte x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(byte y) {
        this.y = y;
    }

    public byte getColor() {
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
    public void setColor(byte color) {
        this.color = color;
    }

    public byte[] getByteArray() {
        final List<Byte> data = new ArrayList<>();
        data.add((byte) x);
        data.add((byte) y);
        data.add(color);

        if (name != null) {
            for (byte b : name.getBytes()) {
                data.add(b);
            }
        } else {
            data.add((byte) 126); // ~
        }

        final byte[] dataArray = new byte[data.size()];

        for (int i = 0; i < dataArray.length; i++) {
            dataArray[i] = data.get(i);
        }

        return dataArray;
    }
}
