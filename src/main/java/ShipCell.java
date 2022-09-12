public class ShipCell {
    private Ship ship;
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
}
