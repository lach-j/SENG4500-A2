import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

public class A2 {

    public static boolean isValidCoords(String coordinates) {
        // Row must be in 'ABCDEFGHIJ'
        var row = coordinates.charAt(0);
        if (row < 'A' || row > 'J') {
            return false;
        }

        try {
            // Column must be between 1-10
            var col = Integer.parseInt(coordinates.substring(1));
            if (col < 1 || col > 10) {
                return false;
            }
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

        // Create all IO connections
        try (var nm = new NetworkingManager(dAdd, dPort);
             var s = nm.createConnection();
             BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
             PrintWriter out = new PrintWriter(s.getOutputStream(), true);
             Scanner userIn = new Scanner(System.in);) {

            // Client is Player 2, Server is Player 1
            System.out.println(nm.isClient() ? "CONNECTED AS PLAYER 2" : "CONNECTED AS PLAYER 1");

            // Build up our ships and add them to the grid.
            var shipList = new ArrayList<Ship>();
            shipList.add(new Ship(5, "Carrier"));
            shipList.add(new Ship(4, "Battleship"));
            shipList.add(new Ship(3, "Cruiser"));
            shipList.add(new Ship(3, "Submarine"));
            shipList.add(new Ship(2, "Destroyer"));

            var myGrid = new ShipGrid();

            shipList.forEach(myGrid::addShip);

            // Guess grid cannot show ships, but is used to show where we have already guessed
            var guesses = new GuessGrid();

            // Print out both grids (vertically stacked)
            System.out.println(guesses);
            System.out.println(myGrid);

            // Player 1 goes first
            var myTurn = !nm.isClient();

            while (true) {
                if (myTurn) {
                    System.out.println("ENTER COORDINATES:");
                    var coordinates = userIn.nextLine();

                    // Request coordinates until they are valid
                    while (!isValidCoords(coordinates)) {
                        System.out.println("Coordinates not valid, please try again using the format <ROW><COLUMN>.");
                        coordinates = userIn.nextLine();
                    }
                    // Send our FIRE command to the opposite player and wait for the response.
                    var message = String.format("FIRE:%s", coordinates);
                    out.println(message);
                    var msg = in.readLine();

                    // Pull the coordinates off the message
                    var response = msg.split(":");
                    var row = response[1].charAt(0);
                    var col = Integer.parseInt(response[1].substring(1));

                    if (response[0].equals("MISS")) {
                        System.out.println("Torpedo Missed");
                        guesses.addMiss(row, col);
                    } else {
                        if (response[0].equals("HIT")) {
                            System.out.println("Torpedo Hit");
                        } else if (response[0].equals("GAME OVER")) {
                            System.out.println("Torpedo Hit and Sunk " + response[2] + ", ALL SHIPS SUNK - YOU WIN!");
                            // If the game is over, first close the open connections and then exit
                            nm.close();
                            System.exit(1);
                        } else {
                            System.out.println("Torpedo Hit and Sunk " + response[2]);
                        }

                        // Anything that isn't a MISS must be a HIT
                        guesses.addHit(row, col);
                    }

                    // Display the updated grids
                    System.out.println(guesses);
                    System.out.println(myGrid);
                } else {
                    // When it isn't our turn, listen for the FIRE command
                    System.out.println("Waiting for other player...");
                    var message = in.readLine().split(":");
                    if (message[0].equals("FIRE")) {
                        // Pull the coordinates off the message
                        var row = message[1].charAt(0);
                        var col = Integer.parseInt(message[1].substring(1));

                        // Send a torpedo to the coordinates
                        var hitShip = myGrid.sendTorpedo(row, col);
                        var response = "";

                        // If sendTorpedo returns null, no ship was in those coordinates
                        if (hitShip == null) {
                            response = String.format("MISS:%s", message[1]);
                        } else {
                            if (!hitShip.isSunk()) {
                                response = String.format("HIT:%s", message[1]);
                            } else {
                                // If the ship was sunk, check if all our ships are sunk (game over)
                                if (shipList.stream().allMatch(Ship::isSunk)) {
                                    response = String.format("GAME OVER:%s:%s", message[1], hitShip.getName());
                                    System.out.println("ALL YOUR SHIPS HAVE BEEN SUNK - YOU LOSE!");

                                    // We need to make sure we tell the other player that the game is over before
                                    // closing our socket and exiting.
                                    out.println(response);
                                    nm.close();
                                    System.exit(1);
                                } else {
                                    response = String.format("SUNK:%s:%s", message[1], hitShip.getName());
                                }
                            }
                        }
                        // Send our response to the other player and print the updated grids.
                        out.println(response);
                        System.out.println(guesses);
                        System.out.println(myGrid);
                    }
                }
                // Change turn
                myTurn = !myTurn;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
