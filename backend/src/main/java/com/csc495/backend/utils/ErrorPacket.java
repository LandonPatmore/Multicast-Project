package com.csc495.backend.utils;

import java.util.Arrays;

public class ErrorPacket extends Packet {

    private String errorMessage;

    public ErrorPacket() {
        super(PacketType.ERROR);
    }

    @Override
    public void parsePacket(byte[] data) {
        byte[] errorData = Arrays.copyOfRange(data, 1, data.length);
        errorMessage = new String(errorData);
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
