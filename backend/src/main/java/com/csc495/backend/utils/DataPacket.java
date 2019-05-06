package com.csc495.backend.utils;

import java.util.ArrayList;
import java.util.List;

public class DataPacket extends Packet {

    private int playerNumber;
    private boolean isPlayerAlive;
    private List<Integer> coordinates;

    public DataPacket() {
        super(PacketType.DATA);
        coordinates = new ArrayList<>();
    }

    @Override
    public void parsePacket(byte[] data) {
        playerNumber = data[1];
        isPlayerAlive = data[2] != 0;

        if (!isPlayerAlive) {
            return;
        }

        for (int i = 3; i < data.length; i++) {
            coordinates.add((int) data[i]);
        }
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public boolean isPlayerAlive() {
        return isPlayerAlive;
    }

    public List<Integer> getCoordinates() {
        return coordinates;
    }
}
