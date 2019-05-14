package com.csc445.shared.packets;

import com.csc445.shared.utils.AES;
import com.csc445.shared.utils.Constants;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.List;

public abstract class Packet {

    private List<Byte> data = new ArrayList<>(Constants.PACKET_SIZE);
    private final Type type;

    private InetAddress senderAddress;
    private int senderPort;

    Packet(Type type) {
        this.type = type;
    }

    public abstract void parseSocketData(DatagramPacket packet); // always start at 1 because we already determined what type of packet it was

    protected abstract void createPacketData();

    public DatagramPacket createPacket(String secretKey) {
        return createPacket(senderAddress, senderPort, secretKey);
    }

    public DatagramPacket createPacket(InetAddress address, int port, String secretKey) {
        createPacketData();


        try {
            final byte[] dataArray = arrayListToArrayHelper();
            final byte[] encryptedDataArray = AES.encryptByteArray(dataArray, dataArray.length, secretKey);

            if (encryptedDataArray != null) {
                return new DatagramPacket(encryptedDataArray, encryptedDataArray.length, address, port);
            }
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }

        return null;
    }

    private byte[] arrayListToArrayHelper() {
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

    void addData(byte data) {
        this.data.add(data);
    }

    Type getType() {
        return type;
    }

    void setSenderAddress(InetAddress senderAddress) {
        this.senderAddress = senderAddress;
    }

    void setSenderPort(int senderPort) {
        this.senderPort = senderPort;
    }

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
}
