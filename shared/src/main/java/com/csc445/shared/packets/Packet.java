package com.csc445.shared.packets;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public abstract class Packet {
    protected enum Type {
        JOIN((byte) 1),
        PlAY((byte) 2),
        START((byte) 4),
        MESSAGE((byte) 5),
        STATE_REQ((byte) 6),
        ERROR((byte) 7);

        private final byte value;

        Type(byte value) {
            this.value = value;
        }

        public byte getValue() {
            return this.value;
        }
    }

    public static final int SIZE = 512;

    private List<Byte> data = new ArrayList<>(SIZE);
    private final Type type;

    private InetAddress senderAddress;
    private int senderPort;

    public Packet(Type type) {
        this.type = type;
    }

    public abstract void parseSocketData(DatagramPacket packet); // always start at 1 because we aready determined what type of packet it was

    protected abstract void createPacketData();

    public DatagramPacket createUnicastPacket() {
        return createUnicastPacket(senderAddress, senderPort);
    }

    public DatagramPacket createUnicastPacket(InetAddress address, int port) {
        createPacketData();

        final byte[] data = arrayListToArrayHelper();

        return new DatagramPacket(data, data.length, address, port);
    }

    public DatagramPacket createMulticastPacket(InetAddress address, int port) {
        createPacketData();

        final byte[] data = arrayListToArrayHelper();

        return new DatagramPacket(data, data.length, address, port);
    }

    byte[] arrayListToArrayHelper() {
        final byte[] dataArray = new byte[data.size()];

        for (int i = 0; i < dataArray.length; i++) {
            dataArray[i] = data.get(i);
        }

        return dataArray;
    }

    byte[] shortToByteArray(short number) {
        return ByteBuffer.allocate(2).putShort(number).array();
    }

    void addByteArray(byte[] data) {
        for (byte b : data) {
            addData(b);
        }
    }

    List<Byte> getData() {
        return data;
    }

    void addData(byte data) {
        this.data.add(data);
    }

    Type getType() {
        return type;
    }

    InetAddress getSenderAddress() {
        return senderAddress;
    }

    void setSenderAddress(InetAddress senderAddress) {
        this.senderAddress = senderAddress;
    }

    int getSenderPort() {
        return senderPort;
    }

    void setSenderPort(int senderPort) {
        this.senderPort = senderPort;
    }
}
