import com.csc445.shared.utils.Constants;

import java.io.IOException;
import java.net.*;

public class TestClient {

    public static void main(String[] args) throws IOException {
        System.setProperty("java.net.preferIPv4Stack", "true"); // This is needed because OSX seems to return an IPv6 address and it does not work with IPv6 for some reason
        final MulticastSocket socket = new MulticastSocket(Constants.GROUP_PORT);
        final InetAddress group = InetAddress.getByName(Constants.GROUP_ADDRESS);
        socket.joinGroup(group);
        final InetAddress server = InetAddress.getByName("127.0.0.1");

        System.out.println("Test client started...");
        System.out.println(group);

//        new Thread(() -> {
//            while (true) {
//                try {
//                    Thread.sleep(2000);
//                    byte[] hBuf = new byte[1];
//                    hBuf[0] = 3;
//
//                    socket.send(new DatagramPacket(hBuf, hBuf.length, server, Constants.SERVER_PORT));
//                } catch (InterruptedException | IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();

        final String name = "Test";

        byte[] buf = new byte[name.length() + 1];

        buf[0] = 1;

        for (int i = 0; i < name.length(); i++) {
            buf[i + 1] = name.getBytes()[i];
        }

        socket.send(new DatagramPacket(buf, buf.length, server, Constants.SERVER_PORT));

        while (true) {
            final byte[] rBuf = new byte[Constants.PACKET_SIZE];
            final DatagramPacket receivedPacket = new DatagramPacket(rBuf, rBuf.length);
            socket.receive(receivedPacket);
            System.out.println(new String(receivedPacket.getData(), 0, receivedPacket.getLength()));
        }
    }
}
