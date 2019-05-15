package com.csc445.frontend.Utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class State {

    private static String userName;
    private static InetAddress serverName;
    private static String secretKey;

    public static String getUserName() {
        return userName;
    }

    public static void setUserName(String userName) {
        State.userName = userName;
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
