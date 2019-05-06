package com.csc495.backend.utils;

import com.csc495.backend.game.Spot;

import java.net.DatagramPacket;

public class PlayPacket extends Packet {

    private final Spot spot;

    private short sequenceNumber;

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
        addByteArray(shortToByteArray(sequenceNumber));

        for (byte b : spot.getByteArray()) {
            addData(b);
        }
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(short sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public Spot getSpot() {
        return spot;
    }
}
