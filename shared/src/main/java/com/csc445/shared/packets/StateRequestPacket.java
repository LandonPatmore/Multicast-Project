package com.csc445.shared.packets;

import java.net.DatagramPacket;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class StateRequestPacket extends Packet {

    private short playNumber;

    public StateRequestPacket() { // used for incoming request
        super(Type.STATE_REQ);
    }

    public StateRequestPacket(short playNumber) {
        super(Type.STATE_REQ);
        this.playNumber = playNumber;
    }

    @Override
    public void parseSocketData(DatagramPacket packet) {
        final byte[] p = Arrays.copyOfRange(packet.getData(), 1, packet.getLength());
        playNumber = ByteBuffer.wrap(p).getShort();
    }

    @Override
    protected void createPacketData() {
        addData(getType().getValue());
        addByteArray(shortToByteArray(playNumber));
    }

    public short getPlayNumber() {
        return playNumber;
    }
}
