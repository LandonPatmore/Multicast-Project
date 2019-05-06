package com.csc445.frontend.Utils;

import com.badlogic.gdx.graphics.Color;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import java.net.URISyntaxException;

public class Helper {
    private static Color selectedColor = Color.BLACK;
    public static Socket socket;

    static {
        try {
            socket = IO.socket("http://localhost:3000");
            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                }

            }).on("event", new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                }

            }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                }

            });
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static Socket getSocket() {
        return socket;
    }

    public static void setSelectedColor(Color color) {
        Helper.selectedColor = color;
    }

    public static Color getSelectedColor() {
        return Helper.selectedColor;
    }
}
