package com.csc495.backend.utils;

import java.util.ArrayList;
import java.util.List;

public class EncryptionPacket extends Packet {

    private List<Byte> publicKey;

    public EncryptionPacket() {
        super(PacketType.ENCRYPTION);
        publicKey = new ArrayList<>();
    }

    @Override
    public void parsePacket(byte[] data) {
        for (int i = 1; i < data.length; i++) {
            publicKey.add(data[i]);
        }
    }

    public List<Byte> getPublicKey() {
        return publicKey;
    }
}
