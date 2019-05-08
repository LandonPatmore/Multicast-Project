package com.csc445.shared.game;

import java.net.InetAddress;

public class Player {
    private final InetAddress address;
    private final int port;
    private final String name;

    private boolean hasHeartbeat;

    public Player(InetAddress address, int port, String name) {
        this.address = address;
        this.port = port;
        this.name = name;
        this.hasHeartbeat = true;
    }

    public InetAddress getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    public String getName() {
        return name;
    }

    public boolean hasHeartbeat() {
        return hasHeartbeat;
    }

    public void setHasHeartbeat(boolean hasHeartbeat) {
        this.hasHeartbeat = hasHeartbeat;
    }
}
