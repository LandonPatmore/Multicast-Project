package com.csc445.frontend.Utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class State {

    private static String name;
    private static InetAddress serverName;
    private static String secretKey;

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        State.name = name;
    }

    public static InetAddress getServerName() {
        return serverName;
    }

    public static void setServerName(String serverName) throws UnknownHostException {
        State.serverName = InetAddress.getByName(serverName);
    }

    public static String getSecretKey() {
        return secretKey;
    }

    public static void setSecretKey(String secretKey) {
        State.secretKey = secretKey;
    }
}
