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

}
