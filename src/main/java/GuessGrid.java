import java.util.Arrays;

public class GuessGrid extends PrintableGrid {

    private final CellState[][] guesses = new CellState[HEIGHT][WIDTH];

    public GuessGrid() {
        // Fill each cell with BLANK initially.
        for (var row : guesses) {
            Arrays.fill(row, CellState.BLANK);
        }
    }


    public void addHit(char row, int col) {
        guesses[resolveIndexByChar(row)][col-1] = CellState.HIT;
    }

    public void addMiss(char row, int col) {
        guesses[resolveIndexByChar(row)][col-1] = CellState.MISS;
    }

    @Override
    protected String getCellString(int row, int col) {
        // Used in the AbstractGrid toString method.
        // Resolves a character based what guess is recorded in the coordinates.
        return switch (guesses[row][col]) {
            case BLANK -> " ";
            case MISS -> "~";
            case HIT -> "X";
        };
    }
    
    private enum CellState {
        BLANK,
        MISS,
        HIT
    }
}
