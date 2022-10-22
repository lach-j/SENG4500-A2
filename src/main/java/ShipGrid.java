import java.util.Random;

// ShipGrid is the grid implementation for storing the current users ships and the opposing users guesses.
public class ShipGrid extends PrintableGrid {

    private final ShipCell[][] cells = new ShipCell[HEIGHT][WIDTH];
    private final boolean[][] guesses = new boolean[HEIGHT][WIDTH];
    private final Random rand;

    public ShipGrid() {
        rand = new Random();
    }

    // The seeded random constructor is only used for testing as it
    // ensures that ships will always be placed in the same location.
    public ShipGrid(long seed) {
        rand = new Random(seed);
    }

    // Checks if there is a Ship at the requested coordinates, if so the Ship is
    // returned and the cell is marked as a hit. Otherwise, the cell is marked as a miss.
    public Ship sendTorpedo(char row, int col) {

        var y = resolveIndexByChar(row);
        var x = col - 1;

        guesses[y][x] = true;
        if (cells[y][x] == null) return null;
        cells[y][x].setIsHit(true);
        return cells[y][x].getShip();
    }

    // Randomised insertion of the Ship within the grid avoiding clashes.
    public void addShip(Ship ship) {
        var isInserted = false;

        while (!isInserted) {
            // Randomly determine the orientation of the Ship.
            var isVertical = rand.nextInt(2) == 0;

            // Determine the max starting index that a ship can be placed without going
            // outside the grid range once every cell is inserted.
            var xMax = isVertical ? WIDTH : WIDTH + 1 - ship.length();
            var yMax = !isVertical ? HEIGHT : HEIGHT + 1 - ship.length();

            // Determine the start coordinates (between 0 and the previously determined max)
            var startX = rand.nextInt(xMax);
            var startY = rand.nextInt(yMax);

            var x = startX;
            var y = startY;

            // Check each cell in the direction of the ship for the length of the ship to
            // determine if there already ships in those cells.
            var isFree = false;
            for (int i = 0; i < ship.length(); i++) {
                if (cells[y][x] != null) break;
                if (i == ship.length() - 1) {
                    // If we have already checked all the cells then we are able to add our ship.
                    isFree = true;
                    break;
                }
                // Increase the appropriate index
                if (isVertical) {
                    y++;
                } else {
                    x++;
                }
            }
            // If the cells are not empty, loop until there is a valid spot found.
            if (!isFree) continue;

            // Since we know that the cells are free, we can insert our cells without checking.
            for (int i = (isVertical ? startY : startX); i < (isVertical ? startY : startX) + ship.length(); i++) {
                var newCell = new ShipCell(ship);
                cells[isVertical ? i : startY][isVertical ? startX : i] = newCell;
                ship.addCell(newCell); // Also add the cell to the ship so that it can reference its state.
            }
            isInserted = true;
        }
    }

    protected String getCellString(int row, int col) {
        var cell = cells[row][col];
        var guess = guesses[row][col];

        if (cell == null) {
            // If there isn't a ship in the cell, display miss if there is a guess. Otherwise its blank.
            return guess ? "~" : " ";
        } else {
            // If there is a ship, check if it has been hit or not.
            return cell.getIsHit() ? "X" : "□";
        }
    }
}
