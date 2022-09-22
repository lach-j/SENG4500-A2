import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class NetworkingManager {

    private final InetAddress broadcastAddress;
    private final int broadcastPort;
    private boolean connected = false;
    private DatagramSocket udpSocket;

    public NetworkingManager(InetAddress address, int port) {
        this.broadcastAddress = address;
        this.broadcastPort = port;
    }

    public void createConnection() {
        Random rand = new Random();
        int gameCommandPort = rand.nextInt(1000) + 5000;

        var udpThread = new Thread(() -> broadcastGame(gameCommandPort));
        udpThread.start();

        try (ServerSocket ss = new ServerSocket(gameCommandPort);
                Socket currentConnection = ss.accept();
                BufferedReader in = new BufferedReader(new InputStreamReader(currentConnection.getInputStream()));
                PrintWriter out = new PrintWriter(currentConnection.getOutputStream(), true)) {
            System.out.println("CLIENT HAS CONNECTED!");
            connected = true;

            // Close the connection early so socket doesnt need to timeout.
            udpSocket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void broadcastGame(int gameCommandPort) {
        try (DatagramSocket udpSocket = new DatagramSocket(broadcastPort)) {
            this.udpSocket = udpSocket;
            udpSocket.setSoTimeout(3000);
            var request = String.format("NEW PLAYER:%d", gameCommandPort);
            var requestBytes = request.getBytes(StandardCharsets.UTF_8);

            while (!connected) {
                byte[] buf = new byte[32];
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                try {
                    udpSocket.receive(packet);
                } catch (SocketTimeoutException e) {
                    if (!connected) {
                        DatagramPacket outPacket = new DatagramPacket(requestBytes, requestBytes.length, broadcastAddress, broadcastPort);
                        udpSocket.send(outPacket);
                        System.out.printf("SENT: %s%n", request);
                    }
                    continue;
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
                String s = new String(buf, StandardCharsets.UTF_8).replaceAll("\u0000.*", "");

                if (s.isEmpty())
                    continue;

                if (s.split(":")[0].equals("NEW PLAYER") && gameCommandPort != Integer.parseInt(s.split(":")[1])) {
                    System.out.println("RECEIVED: " + s);
                    connected = true;
                    var port = Integer.parseInt(s.split(":")[1]);
                    var addr = packet.getAddress();
                    joinExistingGame(port, addr);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("UDP thread ended");
    }

    private void joinExistingGame(int port, InetAddress address) {
        try (Socket s = new Socket(address, port);
                BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
                PrintWriter out = new PrintWriter(s.getOutputStream(), true);) {
            System.out.println("JOINING SOCKET ON PORT " + port);
        } catch (IOException ex) {

        }
    }
}
