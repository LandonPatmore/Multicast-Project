import com.csc495.backend.utils.Packet;

import java.io.IOException;
import java.net.*;

public class TestClient {

    public static void main(String[] args) throws IOException {
        System.setProperty("java.net.preferIPv4Stack", "true"); // This is needed because OSX seems to return an IPv6 address and it does not work with IPv6 for some reason
        final MulticastSocket socket = new MulticastSocket(4446);
        final InetAddress group = InetAddress.getByName("224.0.0.192");
        socket.joinGroup(group);
        final InetAddress address = InetAddress.getByName("127.0.0.1");

        final String test = "Bob";

        byte[] buf = new byte[test.length() + 1];

        buf[0] = 2;

        for (int i = 0; i < test.length(); i++) {
            buf[i + 1] = test.getBytes()[i];
        }

        socket.send(new DatagramPacket(buf, buf.length, address, 4445));

        byte[] errBuf = new byte[512];
        final DatagramPacket packet = new DatagramPacket(errBuf, errBuf.length);
        socket.receive(packet);
        System.out.println(new String(packet.getData(), 0, packet.getLength()));
    }
}
