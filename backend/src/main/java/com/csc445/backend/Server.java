package com.csc445.backend;

import com.csc445.backend.networking.MulticastThread;

import java.io.IOException;

public class Server {

    public static void main(String[] args) throws IOException {
        new Thread(new MulticastThread()).start();
    }
}
