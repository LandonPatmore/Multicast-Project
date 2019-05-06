package com.csc495.backend.utils;

import com.csc495.backend.game.Spot;

import java.net.DatagramPacket;

public class PlayPacket extends Packet {

    private final Spot spot;

    private int sequenceNumber;

    public PlayPacket() {
        super(Type.PlAY);
        spot = new Spot();
    }

    @Override
    public void parseSocketData(DatagramPacket packet) {
        final byte[] data = packet.getData();

        spot.setX(data[1]);
        spot.setY(data[2]);
        spot.setColor(data[3]);
        spot.setName(new String(data, 4, packet.getLength() - 4));
    }

    @Override
    protected void createPacketData() {
        addData(getType().getValue());
        addData((byte) sequenceNumber);
        addData((byte) spot.getX());
        addData((byte) spot.getY());
        addData((byte) spot.getColor());

        for (byte b : spot.getName().getBytes()) {
            addData(b);
        }
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public Spot getSpot() {
        return spot;
    }
}
