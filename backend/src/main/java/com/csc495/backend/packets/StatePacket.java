package com.csc495.backend.packets;

import java.net.DatagramPacket;

public class StatePacket extends Packet {

    private final short sequenceNumber;
    private final short totalSequenceNumber;

    public StatePacket(byte sequenceNumber, short totalNumber) {
        super(Type.STATE);
        this.sequenceNumber = sequenceNumber;
        this.totalSequenceNumber = totalNumber;
    }

    @Override
    public void parseSocketData(DatagramPacket packet) {

    }

    @Override
    protected void createPacketData() {
        addData(getType().getValue());

        addByteArray(shortToByteArray(sequenceNumber));
        addByteArray(shortToByteArray(totalSequenceNumber));

    }

    public void addStateData(byte[] data) {
        for (byte b : data) {
            addData(b);
        }
    }
}
