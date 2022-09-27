import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;

public class A2 {

    private static boolean isInRange(String coordinates) {
        var row = coordinates.charAt(0);
        try {
            
            var col = Integer.parseInt(coordinates.substring(1));
            if (row < 'A' || row > 'J')
                return false;

            if (col < 1 || col > 10)
                return false;

        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }

    public static void main(String[] args) throws UnknownHostException, InterruptedException {

        if (args.length != 2) {
            System.err.printf(
                    "Invalid number of arguments. Received %d, expected 2%n%nUsage: java A2 <broadcast_address> <broadcast_port>%n",
                    args.length);
            System.exit(1);
        }

        InetAddress dAdd = InetAddress.getByName(args[0]);
        int dPort = Integer.parseInt(args[1]);

        try (var nm = new NetworkingManager(dAdd, dPort);
                var s = nm.createConnection();
                BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
                PrintWriter out = new PrintWriter(s.getOutputStream(), true);
                Scanner userIn = new Scanner(System.in);) {

            System.out.println(nm.isClient() ? "CONNECTED AS PLAYER 2" : "CONNECTED AS PLAYER 1");

            var shipList = new ArrayList<Ship>();

            shipList.add(new Ship(5, "Carrier"));
            shipList.add(new Ship(4, "Battleship"));
            shipList.add(new Ship(3, "Cruiser"));
            shipList.add(new Ship(3, "Submarine"));
            shipList.add(new Ship(2, "Destroyer"));

            var myGrid = new ShipGrid();

            shipList.forEach(myGrid::addShip);

            var guesses = new GuessGrid();

            System.out.println(guesses);
            System.out.println(myGrid);

            var myTurn = !nm.isClient();

            while (true) {
                if (myTurn) {
                    System.out.println("ENTER COORDINATES:");
                    var coordinates = userIn.nextLine();
                    while (!isInRange(coordinates)) {
                        System.out.println("Coordinates not valid, please try again using the format <ROW><COLUMN>.");
                        coordinates = userIn.nextLine();
                    }
                    var message = String.format("FIRE:%s", coordinates);
                    out.println(message);
                    var msg = in.readLine();
                    var response = msg.split(":");
                    var row = response[1].charAt(0);
                    var col = Integer.parseInt(response[1].substring(1));
                    if (response[0].equals("MISS")) {
                        System.out.println("Torpedo Missed");
                        guesses.addMiss(row, col);
                    } else {
                        if (response[0].equals("HIT"))
                            System.out.println("Torpedo Hit");
                        else if (response[0].equals("GAME OVER")) {
                            System.out.println("Torpedo Hit and Sunk " + response[2] + ", ALL SHIPS SUNK - YOU WIN!");
                            nm.close();
                            System.exit(1);
                        } else
                            System.out.println("Torpedo Hit and Sunk " + response[2]);

                        guesses.addHit(row, col);
                    }
                    System.out.println(guesses);
                    System.out.println(myGrid);
                } else {
                    System.out.println("Waiting for other player...");
                    var message = in.readLine().split(":");
                    if (message[0].equals("FIRE")) {
                        var row = message[1].charAt(0);
                        var col = Integer.parseInt(message[1].substring(1));
                        var hitShip = myGrid.sendTorpedo(row, col);
                        var response = "";
                        if (hitShip == null) {
                            response = String.format("MISS:%s", message[1]);
                        } else {
                            if (!hitShip.isSunk()) {
                                response = String.format("HIT:%s", message[1]);
                            } else {
                                if (shipList.stream().allMatch(Ship::isSunk)) {
                                    response = String.format("GAME OVER:%s:%s", message[1], hitShip.getName());
                                    System.out.println("ALL YOUR SHIPS HAVE BEEN SUNK - YOU LOSE!");
                                    out.println(response);
                                    nm.close();
                                    System.exit(1);
                                } else
                                    response = String.format("SUNK:%s:%s", message[1], hitShip.getName());
                            }
                        }
                        out.println(response);
                        System.out.println(guesses);
                        System.out.println(myGrid);
                    }
                }
                myTurn = !myTurn;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
