import java.util.Random;

public class ShipGrid {

    private final ShipCell[][] cells = new ShipCell[10][10];
    private final Random rand;

    public ShipGrid() {
        rand = new Random();
    }

    public ShipGrid(long seed) {
        rand = new Random(seed);
    }

    public boolean sendTorpedo(int x, int y) {
        if (cells[y][x] == null) return false;
        cells[y][x].setIsHit(true);
        return true;
    }

    public void addShip(Ship ship) {
        var isInserted = false;

        while (!isInserted) {

            var isVertical = rand.nextInt(2) == 0;
            var xMax = isVertical ? 10 : 11 - ship.length();
            var yMax = !isVertical ? 10 : 11 - ship.length();

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

            for (int i = ( isVertical ? startY : startX ); i < ( isVertical ? startY : startX ) + ship.length(); i++ ) {
                var newCell = new ShipCell(ship);
                cells[isVertical ? i : startY][isVertical ? startX : i] = newCell;
                ship.addCell(newCell);
            }
            isInserted = true;
        }
    }


    @Override public String toString() {

        var sb = new StringBuilder();
        sb.append("+---".repeat(cells.length));
        sb.append("+");
        sb.append("\n");
        for (var row: cells) {
            sb.append("|");
            for (var cell : row) {
                if (cell == null) {
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
            sb.append("+---".repeat(row.length));
            sb.append("+");
            sb.append("\n");
        }
        return sb.toString();
    }

}
