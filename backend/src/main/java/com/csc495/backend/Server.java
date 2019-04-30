package com.csc495.backend;

import com.csc495.backend.networking.MulticastThread;

import java.io.IOException;

public class Server {

    public static void main(String[] args) throws IOException {
        new Thread(new MulticastThread()).start();
    }
}
