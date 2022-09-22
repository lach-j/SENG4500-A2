import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

public class GuessGridTest {
    @Test
    public void GuessGrid_PrintsEmptyGrid() {
        String EMPTY_GRID_STRING = """
                  1   2   3   4   5   6   7   8   9   10
                +---+---+---+---+---+---+---+---+---+---+
              A |   |   |   |   |   |   |   |   |   |   |
                +---+---+---+---+---+---+---+---+---+---+
              B |   |   |   |   |   |   |   |   |   |   |
                +---+---+---+---+---+---+---+---+---+---+
              C |   |   |   |   |   |   |   |   |   |   |
                +---+---+---+---+---+---+---+---+---+---+
              D |   |   |   |   |   |   |   |   |   |   |
                +---+---+---+---+---+---+---+---+---+---+
              E |   |   |   |   |   |   |   |   |   |   |
                +---+---+---+---+---+---+---+---+---+---+
              F |   |   |   |   |   |   |   |   |   |   |
                +---+---+---+---+---+---+---+---+---+---+
              G |   |   |   |   |   |   |   |   |   |   |
                +---+---+---+---+---+---+---+---+---+---+
              H |   |   |   |   |   |   |   |   |   |   |
                +---+---+---+---+---+---+---+---+---+---+
              I |   |   |   |   |   |   |   |   |   |   |
                +---+---+---+---+---+---+---+---+---+---+
              J |   |   |   |   |   |   |   |   |   |   |
                +---+---+---+---+---+---+---+---+---+---+
                """;

        var grid = new GuessGrid();
        System.out.println(grid);
        Assert.assertEquals(EMPTY_GRID_STRING, grid.toString());
    }


    @Test
    public void GuessGrid_PrintsCorrectGuesses() {
        String EMPTY_GRID_STRING = """
                  1   2   3   4   5   6   7   8   9   10
                +---+---+---+---+---+---+---+---+---+---+
              A |   |   |   |   |   |   |   |   |   |   |
                +---+---+---+---+---+---+---+---+---+---+
              B |   |   |   |   |   |   |   |   |   |   |
                +---+---+---+---+---+---+---+---+---+---+
              C |   |   |   |   |   |   |   |   |   |   |
                +---+---+---+---+---+---+---+---+---+---+
              D |   |   |   |   |   |   |   | ~ |   |   |
                +---+---+---+---+---+---+---+---+---+---+
              E |   |   |   |   |   |   |   |   |   |   |
                +---+---+---+---+---+---+---+---+---+---+
              F |   |   | X |   |   |   |   |   |   |   |
                +---+---+---+---+---+---+---+---+---+---+
              G |   |   |   |   |   |   |   |   |   |   |
                +---+---+---+---+---+---+---+---+---+---+
              H |   |   |   |   |   |   |   |   |   |   |
                +---+---+---+---+---+---+---+---+---+---+
              I |   |   |   |   |   |   |   |   |   |   |
                +---+---+---+---+---+---+---+---+---+---+
              J |   |   |   |   |   |   |   |   |   |   |
                +---+---+---+---+---+---+---+---+---+---+
                """;

        var grid = new GuessGrid();
        grid.addHit(5, 2);
        grid.addMiss(3, 7);
        System.out.println(grid);
        Assert.assertEquals(EMPTY_GRID_STRING, grid.toString());
    }


    @Test
    public void WaitingText_WorksLocally() {
        try {
            for (int i = 0; i < 100; i++) {
                System.out.print("Waiting for other player.  \r");
                Thread.sleep(100);
                System.out.print("Waiting for other player.. \r");
                Thread.sleep(100);
                System.out.print("Waiting for other player...\r");
                Thread.sleep(100);
                System.out.print("Waiting for other player.. \r");
                Thread.sleep(100);
            }
        } catch (InterruptedException ex) {

        }
    }
}
