import java.util.Random;

public class ShipGrid {

    private static final int WIDTH = 10;
    private static final int HEIGHT = 10;

    private final ShipCell[][] cells = new ShipCell[HEIGHT][WIDTH];
    private final boolean[][] guesses = new boolean[HEIGHT][WIDTH];
    private final Random rand;

    public ShipGrid() {
        rand = new Random();
    }

    public ShipGrid(long seed) {
        rand = new Random(seed);
    }

    public boolean sendTorpedo(int x, int y) {
        guesses[y][x] = true;
        if (cells[y][x] == null) return false;
        cells[y][x].setIsHit(true);
        return true;
    }

    public void addShip(Ship ship) {
        var isInserted = false;

        while (!isInserted) {

            var isVertical = rand.nextInt(2) == 0;
            var xMax = isVertical ? WIDTH : WIDTH + 1 - ship.length();
            var yMax = !isVertical ? HEIGHT : HEIGHT + 1 - ship.length();

            var startX = rand.nextInt(xMax);
            var startY = rand.nextInt(yMax);

            var x = startX;
            var y = startY;

            var isFree = false;
            for (int i = 0; i < ship.length(); i++) {
                if (cells[y][x] != null) break;
                if (i == ship.length() - 1) {
                    isFree = true;
                    break;
                }
                if (isVertical) {
                    y++;
                } else {
                    x++;
                }
            }
            if (!isFree) continue;

            for (int i = (isVertical ? startY : startX); i < (isVertical ? startY : startX) + ship.length(); i++) {
                var newCell = new ShipCell(ship);
                cells[isVertical ? i : startY][isVertical ? startX : i] = newCell;
                ship.addCell(newCell);
            }
            isInserted = true;
        }
    }


    @Override
    public String toString() {

        var sb = new StringBuilder();
        sb.append("+---".repeat(WIDTH));
        sb.append("+");
        sb.append("\n");
        for (var row = 0; row < HEIGHT; row++) {
            sb.append("|");
            for (var col = 0; col < WIDTH; col++) {

                var cell = cells[row][col];
                var guess = guesses[row][col];

                if (cell == null) {
                    if (guess)
                        sb.append(" ~ ");
                    else
                        sb.append("   ");
                } else {
                    if (cell.getIsHit()) {
                        sb.append(" x ");
                    } else {
                        sb.append(" □ ");
                    }
                }
                sb.append("|");
            }
            sb.append("\n");
            sb.append("+---".repeat(WIDTH));
            sb.append("+");
            sb.append("\n");
        }
        return sb.toString();
    }

}
