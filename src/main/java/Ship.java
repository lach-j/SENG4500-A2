import java.util.Arrays;

// Ship object can query all its cells to determine its overall state as well as store data such as its name.
public class Ship {
    private final ShipCell[] cells;
    private final String name;

    public Ship(int length, String name) {
        cells = new ShipCell[length];
        this.name = name;
    }

    public Ship(int length) {
        this(length, "");
    }

    public String getName() {
        return name;
    }

    public int length() {
        return cells.length;
    }

    // Check every cell in the ship, if they are all hit then the ship is sunk.
    public boolean isSunk() {
        return Arrays.stream(cells).allMatch(ShipCell::getIsHit);
    }

    public void addCell(ShipCell newCell) {
        for (int i = 0; i < cells.length; i++)
        {
            if (cells[i] == null) {
                cells[i] = newCell;
                return;
            }
        }
        throw new UnsupportedOperationException("All cells are full");
    }
}
