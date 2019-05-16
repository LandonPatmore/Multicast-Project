package com.csc445.shared.packets;

import java.net.DatagramPacket;

public class JoinPacket extends Packet {

    private String name;

    public JoinPacket() { // used for incoming join
        super(Type.JOIN);
    }

    public JoinPacket(String name) {
        super(Type.JOIN);
        this.name = name;
    }

    @Override
    public void parseSocketData(DatagramPacket packet) {
        name = new String(packet.getData(), 1, packet.getLength() - 1);
    }

    @Override
    protected void createPacketData() {
        addData(getType().getValue());

        for (byte b : name.getBytes()) {
            addData(b);
        }
    }

    public String getName() {
        return name;
    }
}
