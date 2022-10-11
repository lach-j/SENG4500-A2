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

    private static int BROADCAST_TIMEOUT = 30000;

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

            // Close the connection early so socket doesnt need to timeout.
            udpSocket.close();
        } catch (IOException e) {
        }
        return tcpSocket;
    }

    private void broadcastGame(int gameCommandPort) {
        try (DatagramSocket udpSocket = new DatagramSocket(null);) {
            // udpSocket.setOption(StandardSocketOptions.SO_REUSEPORT, true);
            udpSocket.setReuseAddress(true);
            udpSocket.setSoTimeout(BROADCAST_TIMEOUT);
            udpSocket.bind(new InetSocketAddress(broadcastAddress, broadcastPort));
            this.udpSocket = udpSocket;
            var request = String.format("NEW PLAYER:%d", gameCommandPort);
            var requestBytes = request.getBytes(StandardCharsets.UTF_8);

            while (!connected) {
                byte[] buf = new byte[32];
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                try {
                    udpSocket.receive(packet);
                } catch (SocketTimeoutException e) {
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
                String s = new String(buf, StandardCharsets.UTF_8).replaceAll("\u0000.*", "");

                if (s.isEmpty()) {
                    continue;
                }

                if (s.split(":")[0].equals("NEW PLAYER") && gameCommandPort != Integer.parseInt(s.split(":")[1])) {
                    var port = Integer.parseInt(s.split(":")[1]);
                    var addr = packet.getAddress();
                    joinExistingGame(port, addr);
                    serverSocket.close();
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

        }
    }

    public boolean isConnected() {
        return connected;
    }

    public boolean isClient() {
        return isClient;
    }

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
