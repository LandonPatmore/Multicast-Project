package com.csc445.shared.packets;

import com.csc445.shared.utils.Constants;

import java.net.DatagramPacket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public abstract class Packet {

    private List<Byte> data = new ArrayList<>(Constants.PACKET_SIZE);
    private final Type type;

    Packet(Type type) {
        this.type = type;
    }

    /**
     * Function that parses data that comes in from a client.
     *
     * @param packet from client
     */
    public abstract void parseSocketData(DatagramPacket packet); // always start at 1 because we already determined what type of packet it was

    /**
     * Function to create data for a packet.
     */
    protected abstract void createPacketData();

    /**
     * Function to create a packet.
     *
     * @return byte array of data
     */
    public byte[] createPacket() {
        createPacketData();
        return arrayListToArrayHelper();
    }

    /**
     * Function to convert an ArrayList to an array.
     *
     * @return array of bytes
     */
    private byte[] arrayListToArrayHelper() {
        final byte[] dataArray = new byte[data.size()];

        for (int i = 0; i < dataArray.length; i++) {
            dataArray[i] = data.get(i);
        }

        return dataArray;
    }

    /**
     * Function to convert a short to a byte array.
     *
     * @param number short
     * @return byte array of the short
     */
    byte[] shortToByteArray(short number) {
        return ByteBuffer.allocate(2).putShort(number).array();
    }

    /**
     * Function to add a byte array to the the data ArrayList
     *
     * @param data byte array
     */
    void addByteArray(byte[] data) {
        for (byte b : data) {
            addData(b);
        }
    }

    /**
     * Function to add data to the data ArrayList.
     *
     * @param data data to be added
     */
    void addData(byte data) {
        this.data.add(data);
    }

    Type getType() {
        return type;
    }

    protected enum Type {
        JOIN((byte) 1),
        PLAY((byte) 2),
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
