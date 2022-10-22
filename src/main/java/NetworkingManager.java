import java.io.Closeable;
import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class NetworkingManager implements Closeable {

    private final InetAddress broadcastAddress;
    private final int broadcastPort;
    private boolean connected = false;
    private boolean isClient = false;
    private DatagramSocket udpSocket;
    private Socket tcpSocket;
    private ServerSocket serverSocket;

    public NetworkingManager(InetAddress address, int port) {
        this.broadcastAddress = address;
        this.broadcastPort = port;
    }

    private static int generateRandomPort() {
        Random rand = new Random();
        int MIN_PORT = 5000;
        int MAX_PORT = 6000;
        return rand.nextInt(MAX_PORT - MIN_PORT) + MIN_PORT;
    }

    public Socket createConnection() {
        int gameCommandPort = generateRandomPort();

        var udpThread = new Thread(() -> broadcastGame(gameCommandPort));
        udpThread.start();

        try {
            ServerSocket ss = new ServerSocket(gameCommandPort);
            serverSocket = ss;
            Socket currentConnection = ss.accept();
            connected = true;
            this.tcpSocket = currentConnection;
            isClient = false;

            // Close the connection early so socket doesn't need to timeout.
            // As a result the "udpThread" runs to completion and the thread is killed.
            udpSocket.close();
        } catch (IOException ex) {
            // Ignore
        }
        return tcpSocket;
    }

    private void broadcastGame(int gameCommandPort) {
        var BROADCAST_TIMEOUT = 30000;

        try (DatagramSocket udpSocket = new DatagramSocket(null);) {
            udpSocket.setReuseAddress(true); // Setting reuseAddress here to allow communication on the same host
            udpSocket.setSoTimeout(BROADCAST_TIMEOUT);
            udpSocket.bind(new InetSocketAddress(broadcastAddress, broadcastPort));
            this.udpSocket = udpSocket;

            var request = String.format("NEW PLAYER:%d", gameCommandPort);
            var requestBytes = request.getBytes(StandardCharsets.UTF_8);

            while (!connected) {
                byte[] buf = new byte[32];
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                try {
                    // Listen for UDP broadcasts
                    udpSocket.receive(packet);
                } catch (SocketTimeoutException e) {
                    // After the socket times out, broadcast our own "NEW PLAYER" packet
                    if (!connected) {
                        DatagramPacket outPacket = new DatagramPacket(requestBytes, requestBytes.length,
                                broadcastAddress, broadcastPort);
                        try (DatagramSocket sendSocket = new DatagramSocket(generateRandomPort());) {
                            sendSocket.send(outPacket);
                        }
                    }
                    continue;
                } catch (SocketException e) {
                }

                // Trimming the "null" chars from the buffer.
                String s = new String(buf, StandardCharsets.UTF_8).replaceAll("\u0000.*", "");

                if (s.isEmpty()) {
                    continue;
                }

                // Pull the TCP port off the "NEW PLAYER" message
                if (s.split(":")[0].equals("NEW PLAYER")) {
                    var port = Integer.parseInt(s.split(":")[1]);
                    var addr = packet.getAddress();
                    joinExistingGame(port, addr);
                    serverSocket.close(); // As this is player 2, the ServerSocket doesn't need to be open anymore.
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void joinExistingGame(int port, InetAddress address) {
        try {
            Socket s = new Socket(address, port);
            connected = true;
            isClient = true;
            this.tcpSocket = s;
        } catch (IOException ex) {
            // Ignore
        }
    }

    public boolean isConnected() {
        return connected;
    }

    public boolean isClient() {
        return isClient;
    }

    // Overrides the Closable close implementation to close all sockets in the program.
    @Override
    public void close() throws IOException {
        if (tcpSocket != null)
            tcpSocket.close();

        if (serverSocket != null)
            serverSocket.close();

        if (udpSocket != null)
            udpSocket.close();
    }
}
