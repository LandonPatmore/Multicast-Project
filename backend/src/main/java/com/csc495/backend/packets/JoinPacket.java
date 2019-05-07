package com.csc495.backend.packets;

import java.net.DatagramPacket;

public class JoinPacket extends Packet {

    private String name;

    public JoinPacket() {
        super(Type.JOIN);
    }

    @Override
    public void parseSocketData(DatagramPacket packet) {
        name = new String(packet.getData(), 1, packet.getLength() - 1);
        setSenderAddress(packet.getAddress());
        setSenderPort(packet.getPort());
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
