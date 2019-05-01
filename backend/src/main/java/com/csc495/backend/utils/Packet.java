package com.csc495.backend.utils;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public abstract class Packet {

    public static final int SIZE = 512;

    private final List<Byte> sendableData;

    public enum PacketType {
        ENCRYPTION(1),
        DATA(2),
        ERROR(3);

        private final int value;

        PacketType(int value) {
            this.value = value;
        }

        public byte getValue() {
            return (byte) this.value;
        }
    }

    Packet(PacketType packetType) {
        this.sendableData = new ArrayList<>();

        this.sendableData.add(packetType.getValue());
    }

    // we already know that the first index is the code, we don't need to look at it anymore after we know the type of packet
    public abstract void parsePacket(byte[] data);

    public boolean addData(int... toAdd) {
        for (int d : toAdd) {
            if (sendableData.size() >= 512) {
                return false;
            }
            sendableData.add((byte) d);
        }

        return true;
    }

    private byte[] getDataBytes() {
        final byte[] bytes = new byte[sendableData.size()];

        for (int i = 0; i < sendableData.size(); i++) {
            bytes[i] = sendableData.get(i);
        }

        return bytes;
    }

    public DatagramPacket getDatagramPacket(InetAddress address, int port) {
        final byte[] dataBytes = getDataBytes();
        return new DatagramPacket(dataBytes, dataBytes.length, address, port);
    }
}
