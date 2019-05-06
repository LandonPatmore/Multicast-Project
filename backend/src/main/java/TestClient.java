import java.io.IOException;
import java.net.*;

public class TestClient {

    public static void main(String[] args) throws IOException {
        System.setProperty("java.net.preferIPv4Stack", "true"); // This is needed because OSX seems to return an IPv6 address and it does not work with IPv6 for some reason
        final MulticastSocket socket = new MulticastSocket(4446);
        final InetAddress group = InetAddress.getByName("224.0.0.192");
        socket.joinGroup(group);
        final InetAddress address = InetAddress.getByName("127.0.0.1");

        final String test = "This is a test message";

        socket.send(new DatagramPacket(test.getBytes(), test.length(), address, 4445));
        byte[] buf = new byte[512];
        final DatagramPacket packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet);

        System.out.println(new String(packet.getData(), 0, packet.getLength()));
    }
}
