package com.csc495.backend.packets;

import java.net.DatagramPacket;

public class ErrorPacket extends Packet {

    private final String errorMessage;

    public ErrorPacket(String errorMessage, DatagramPacket previousPacket) {
        super(Type.ERROR);
        this.errorMessage = errorMessage;
        setSenderAddress(previousPacket.getAddress());
        setSenderPort(previousPacket.getPort());
    }

    @Override
    public void parseSocketData(DatagramPacket packet) {

    }

    @Override
    protected void createPacketData() {
        addData(getType().getValue());

        for (byte b : errorMessage.getBytes()) {
            addData(b);
        }
    }
}
