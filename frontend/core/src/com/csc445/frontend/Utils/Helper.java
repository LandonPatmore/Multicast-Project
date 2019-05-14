package com.csc445.frontend.Utils;

import com.badlogic.gdx.graphics.Color;
import com.csc445.shared.utils.AES;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.security.InvalidKeyException;

public class Helper {
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

    public static void setSelectedColor(Color color) {
        Helper.selectedColor = color;
        if (color.equals(Color.RED)) {
            Helper.selectedColorByte = 0;
        } else if (color.equals(Color.ORANGE)) {
            Helper.selectedColorByte = 1;
        } else if (color.equals(Color.YELLOW)) {
            Helper.selectedColorByte = 2;
        } else if (color.equals(Color.GREEN)) {
            Helper.selectedColorByte = 3;
        } else if (color.equals(Color.BLUE)) {
            Helper.selectedColorByte = 4;
        } else if (color.equals(Color.PURPLE)) {
            Helper.selectedColorByte = 5;
        } else if (color.equals(Color.WHITE)) {
            Helper.selectedColorByte = 6;
        } else if (color.equals(Color.BLACK)) {
            Helper.selectedColorByte = 7;
        } else if (color.equals(Color.PINK)) {
            Helper.selectedColorByte = 8;
        } else if (color.equals(Color.TEAL)) {
            Helper.selectedColorByte = 9;
        }

    }

    public static Color getSelectedColor() {
        return Helper.selectedColor;
    }

    public static byte getSelectedColorByte() {
        return Helper.selectedColorByte;
    }
}
