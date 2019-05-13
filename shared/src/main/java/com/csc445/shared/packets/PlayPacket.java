package com.csc445.shared.packets;

import com.csc445.shared.game.Spot;

import java.net.DatagramPacket;

public class PlayPacket extends Packet {

    private final Spot spot;

//    private short sequenceNumber;

    public PlayPacket() { // used for incoming play
        super(Type.PlAY);
        spot = new Spot();
    }

    public PlayPacket(Spot spot) {
        super(Type.PlAY);
        this.spot = spot;
    }

    @Override
    public void parseSocketData(DatagramPacket packet) {
        final byte[] data = packet.getData();

        spot.setX(data[1]);
        spot.setY(data[2]);
        spot.setColor(data[3]);
        spot.setName(new String(data, 4, packet.getLength() - 5));
    }

    @Override
    protected void createPacketData() {
        addData(getType().getValue());
//        addByteArray(shortToByteArray(sequenceNumber));

        for (byte b : spot.getByteArray()) {
            addData(b);
        }
    }

//    public int getSequenceNumber() {
//        return sequenceNumber;
//    }

//    public void setSequenceNumber(short sequenceNumber) {
//        this.sequenceNumber = sequenceNumber;
//    }

    public Spot getSpot() {
        return spot;
    }
}
