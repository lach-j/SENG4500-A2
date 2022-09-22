import java.net.InetAddress;

public class A2 {

    public static void main(String[] args) {

        try {
            if (args.length != 2) {
                System.err.printf("Invalid number of arguments. Received %d, expected 2%n%nUsage: java A2 <broadcast_address> <broadcast_port>%n", args.length);
                System.exit(1);
            }

            InetAddress dAdd = InetAddress.getByName(args[0]);
            int dPort = Integer.parseInt(args[1]);

            var nm = new GameManager(dAdd, dPort);
            nm.run();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
