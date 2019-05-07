package com.csc495.backend.packets;

import com.csc495.backend.game.Player;

import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.List;

public class StatePacket extends Packet {

    private final short sequenceNumber;
    private final short totalSequenceNumber;
    private final List<Byte> stateData;

    public StatePacket(Player player, short sequenceNumber, short totalNumber) {
        super(Type.STATE);
        setSenderAddress(player.getAddress());
        setSenderPort(player.getPort());
        this.sequenceNumber = sequenceNumber;
        this.totalSequenceNumber = totalNumber;
        this.stateData = new ArrayList<>();
    }

    @Override
    public void parseSocketData(DatagramPacket packet) {

    }

    @Override
    protected void createPacketData() {
        addData(getType().getValue());

        addByteArray(shortToByteArray(sequenceNumber));
        addByteArray(shortToByteArray(totalSequenceNumber));

        for (byte b : stateData) {
            addData(b);
        }
    }

    public void addStateData(List<Byte> data) {
        stateData.addAll(data);
    }
}
