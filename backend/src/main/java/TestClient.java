import java.io.IOException;
import java.net.*;
import java.util.Arrays;

public class TestClient {

    public static void main(String[] args) throws IOException {
        System.setProperty("java.net.preferIPv4Stack", "true"); // This is needed because OSX seems to return an IPv6 address and it does not work with IPv6 for some reason
        final MulticastSocket socket = new MulticastSocket(4446);
        final InetAddress group = InetAddress.getByName("224.0.0.192");
        socket.joinGroup(group);
        final InetAddress server = InetAddress.getByName("129.3.218.36");

        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(2000);
                    byte[] hBuf = new byte[1];
                    hBuf[0] = 3;

                    socket.send(new DatagramPacket(hBuf, hBuf.length, server, 4445));
                } catch (InterruptedException | IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        final String test = "Ye";

        byte[] buf = new byte[test.length() + 1];

        buf[0] = 1;

        for (int i = 0; i < test.length(); i++) {
            buf[i + 1] = test.getBytes()[i];
        }

        socket.send(new DatagramPacket(buf, buf.length, server, 4445));

        while (true) {
            byte[] errBuf = new byte[512];
            final DatagramPacket packet = new DatagramPacket(errBuf, errBuf.length);
            socket.receive(packet);
            System.out.println(Arrays.toString(packet.getData()));
        }
    }
}
