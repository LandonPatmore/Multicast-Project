package com.csc495.backend.game;

import java.net.InetAddress;

public class Player {
    private final InetAddress address;
    private final int port;
    private final String name;

    public Player(InetAddress address, int port, String name) {
        this.address = address;
        this.port = port;
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
