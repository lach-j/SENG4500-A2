public abstract class AbstractGrid {

    protected abstract String getCellString(int row, int col);
    protected static final int WIDTH = 10;
    protected static final int HEIGHT = 10;


    protected int resolveIndexByChar(char row) {
        return row - 65;
    }

    @Override
    public String toString() {
        char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        var sb = new StringBuilder();

        sb.append(" ");
        for (int i = 0; i < WIDTH; i++)
            sb.append("   ").append(i+1);

        sb.append("\n  ");
        sb.append("+---".repeat(WIDTH));
        sb.append("+");
        sb.append("\n");
        for (var row = 0; row < HEIGHT; row++) {
            sb.append(alphabet[row]);
            sb.append(" |");
            for (var col = 0; col < WIDTH; col++) {
                sb.append(" ").append(getCellString(row, col)).append(" ");
                sb.append("|");
            }
            sb.append("\n  ");
            sb.append("+---".repeat(WIDTH));
            sb.append("+");
            sb.append("\n");
        }
        return sb.toString();
    }
}
