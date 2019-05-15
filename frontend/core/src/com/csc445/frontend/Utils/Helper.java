package com.csc445.frontend.Utils;

import com.badlogic.gdx.graphics.Color;
import com.csc445.shared.utils.AES;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.security.InvalidKeyException;

public class Helper {

    private static final Color[] COLORS = {Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.BLUE, Color.PURPLE, Color.WHITE, Color.BLACK, Color.PINK, Color.TEAL};

    private static Color selectedColor = Color.BLACK;
    private static byte selectedColorByte = 7; // black

    private static MulticastSocket socket;

    static {
        try {
            socket = new MulticastSocket(4446);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public static void sendPacket(byte[] data, InetAddress address, int port) {
        new Thread(() -> {
            try {
                final byte[] encryptedDataArray = AES.encryptByteArray(data, data.length, State.getSecretKey());
                if (encryptedDataArray != null) {
                    socket.send(new DatagramPacket(encryptedDataArray, encryptedDataArray.length, address, port));
                }
            } catch (IOException | InvalidKeyException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static DatagramPacket receivePacket() {
        try {
            final byte[] buf = new byte[512];
            final DatagramPacket packet = new DatagramPacket(buf, buf.length);
            socket.receive(packet); // we actually receive the data here
            return packet;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Color convertByteToColor(byte color) {
        if (color == 0) {
            return Color.RED;
        } else if (color == 1) {
            return Color.ORANGE;
        } else if (color == 2) {
            return Color.YELLOW;
        } else if (color == 3) {
            return Color.GREEN;
        } else if (color == 4) {
            return Color.BLUE;
        } else if (color == 5) {
            return Color.PURPLE;
        } else if (color == 6) {
            return Color.WHITE;
        } else if (color == 7) {
            return Color.BLACK;
        } else if (color == 8) {
            return Color.PINK;
        } else if (color == 9) {
            return Color.TEAL;
        }

        return null;
    }

    private static byte convertColorToByte(Color color) {
        if (color.equals(Color.RED)) {
            return 0;
        } else if (color.equals(Color.ORANGE)) {
            return 1;
        } else if (color.equals(Color.YELLOW)) {
            return 2;
        } else if (color.equals(Color.GREEN)) {
            return 3;
        } else if (color.equals(Color.BLUE)) {
            return 4;
        } else if (color.equals(Color.PURPLE)) {
            return 5;
        } else if (color.equals(Color.WHITE)) {
            return 6;
        } else if (color.equals(Color.BLACK)) {
            return 7;
        } else if (color.equals(Color.PINK)) {
            return 8;
        } else if (color.equals(Color.TEAL)) {
            return 9;
        }

        return 7;
    }

    public static void setSelectedColor(Color color) {
        Helper.selectedColor = color;
        Helper.selectedColorByte = convertColorToByte(color);
    }

    public static Color getSelectedColor() {
        return Helper.selectedColor;
    }

    public static byte getSelectedColorByte() {
        return Helper.selectedColorByte;
    }

    public static Color[] getColors() {
        return COLORS;
    }
}
