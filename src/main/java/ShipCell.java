// Each Ship is comprised of a number of ShipCells
// These cells have a reference to their parent Ship
public class ShipCell {
    private final Ship ship;
    private boolean isHit;

    public ShipCell(Ship ship) {
        this.ship = ship;
    }

    public boolean getIsHit() {
        return isHit;
    }

    public void setIsHit(boolean isHit) {
        this.isHit = isHit;
    }

    public Ship getShip() {
        return ship;
    }
}
