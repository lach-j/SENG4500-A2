import java.util.Arrays;

public class Ship {
    private final ShipCell[] cells;

    public Ship(int length) {
        cells = new ShipCell[length];
    }

    public int length() {
        return cells.length;
    }

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
