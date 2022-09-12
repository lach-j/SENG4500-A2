public class ShipGrid {

    private final ShipCell[][] cells = new ShipCell[10][10];


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
                        sb.append(" ▣ ");
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
