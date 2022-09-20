import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class GameManager {

    private final InetAddress address;
    private final int port;
    private int gamePort;
    private boolean connected = false;

    public GameManager(InetAddress address, int port) {
        this.address = address;
        this.port = port;
    }

    public void run() {
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
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void broadcastGame(int gameCommandPort) {
        try {
            DatagramSocket socket = new DatagramSocket(port);
            socket.setSoTimeout(5000);
            var request = String.format("NEW PLAYER:%d", gameCommandPort);
            var requestBytes = request.getBytes(StandardCharsets.UTF_8);

            while (!connected) {
                System.out.println("test");
                byte[] buf = new byte[32];
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                try {
                    socket.receive(packet);
                } catch (SocketTimeoutException e) {
                    if (!connected) {
                        DatagramPacket outPacket = new DatagramPacket(requestBytes, requestBytes.length, address, port);
                        socket.send(outPacket);
                        System.out.printf("SENT: %s%n", request);
                    }
                    continue;
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
                String s = new String(buf, StandardCharsets.UTF_8).replaceAll("\u0000.*", "");

                System.out.println("RECEIVED: " + s);

                if (s.split(":")[0].equals("NEW PLAYER")) {
                    connected = true;
                    this.gamePort = Integer.parseInt(s.split(":")[1]);
                    joinExistingGame();
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("UDP thread ended");
    }

    private void joinExistingGame() {
        System.out.println("IDK");
        try (Socket s = new Socket(this.address, gamePort);
             BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
             PrintWriter out = new PrintWriter(s.getOutputStream(), true);) {
            System.out.println("JOINING SOCKET");
        } catch (IOException ex) {

        }
    }
}
