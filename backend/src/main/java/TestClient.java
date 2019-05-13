import com.csc445.shared.utils.AES;
import com.csc445.shared.utils.Constants;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.*;
import java.security.InvalidKeyException;
import java.util.Arrays;
import java.util.Base64;

public class TestClient {

    public static void main(String[] args) throws IOException {
        System.setProperty("java.net.preferIPv4Stack", "true"); // This is needed because OSX seems to return an IPv6 address and it does not work with IPv6 for some reason
        final MulticastSocket socket = new MulticastSocket(Constants.GROUP_PORT);
        final InetAddress group = InetAddress.getByName(Constants.GROUP_ADDRESS);
        socket.joinGroup(group);
        final InetAddress server = InetAddress.getByName("192.168.1.94");

        System.out.println("Test client started...");
        System.out.println(group);

        String secretKey = AES.TEST_PASSWORD;

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

		try {
			buf = AES.encryptByteArray(buf, secretKey);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		}

		socket.send(new DatagramPacket(buf, buf.length, server, Constants.SERVER_PORT));
        while (true) {
            final byte[] rBuf = new byte[Constants.PACKET_SIZE];
            final DatagramPacket receivedPacket = new DatagramPacket(rBuf, rBuf.length);
            socket.receive(receivedPacket);
            byte[] data = Arrays.copyOfRange(receivedPacket.getData(), 0, receivedPacket.getLength());
			try {
				data = AES.decryptByteArray(data, secretKey);
				System.out.println(new String(data, 1, data.length - 1));
			} catch (InvalidKeyException e) {
				e.printStackTrace();
				System.exit(1);
			}
        }
    }
}
