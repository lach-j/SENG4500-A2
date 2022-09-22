import java.util.Arrays;

public class GuessGrid extends AbstractGrid {

    private CellState[][] guesses = new CellState[HEIGHT][WIDTH];

    public GuessGrid() {
        for (var row : guesses) {
            Arrays.fill(row, CellState.BLANK);
        }
    }

    public void addHit(int row, int col) {
        guesses[row][col] = CellState.HIT;
    }

    public void addMiss(int row, int col) {
        guesses[row][col] = CellState.MISS;
    }

    @Override
    protected String getCellString(int row, int col) {
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
