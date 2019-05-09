package com.csc445.shared.packets;

import java.net.DatagramPacket;

public class StartPacket extends Packet {

    public StartPacket() {
        super(Type.START);
    }

    @Override
    public void parseSocketData(DatagramPacket packet) {
        // no need to parse, just check the first byte
    }

    @Override
    protected void createPacketData() {
        addData(getType().getValue());
    }
}
