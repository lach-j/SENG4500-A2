import java.util.Arrays;

public class GuessGrid extends AbstractGrid {

    private CellState[][] guesses = new CellState[HEIGHT][WIDTH];

    public GuessGrid() {
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
