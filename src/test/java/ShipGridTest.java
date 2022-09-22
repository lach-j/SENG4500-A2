import org.junit.Assert;
import org.junit.Test;

public class ShipGridTest {

    @Test
    public void ShipGrid_PrintsEmptyGrid() {

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

        var grid = new ShipGrid();
        System.out.println(grid);
        Assert.assertEquals(EMPTY_GRID_STRING, grid.toString());
    }

    private ShipGrid getPopulatedGrid() {
        var grid = new ShipGrid(0L);

        var ship = new Ship(5);
        var ship2 = new Ship(4);
        var ship3 = new Ship(3);
        var ship4 = new Ship(3);
        var ship5 = new Ship(2);

        grid.addShip(ship);
        grid.addShip(ship2);
        grid.addShip(ship3);
        grid.addShip(ship4);
        grid.addShip(ship5);

        return grid;
    }

    @Test
    public void ShipGrid_PopulatesGrid() {

        String POPULATED_GRID_STRING = """
                  1   2   3   4   5   6   7   8   9   10
                +---+---+---+---+---+---+---+---+---+---+
              A |   |   |   |   |   |   |   |   |   |   |
                +---+---+---+---+---+---+---+---+---+---+
              B |   |   |   |   |   |   |   |   |   |   |
                +---+---+---+---+---+---+---+---+---+---+
              C |   |   |   |   |   |   |   |   |   |   |
                +---+---+---+---+---+---+---+---+---+---+
              D |   |   |   |   | □ | □ | □ | □ |   |   |
                +---+---+---+---+---+---+---+---+---+---+
              E |   |   |   |   |   |   |   |   |   |   |
                +---+---+---+---+---+---+---+---+---+---+
              F |   |   |   |   | □ | □ |   |   |   |   |
                +---+---+---+---+---+---+---+---+---+---+
              G |   |   |   |   |   |   |   |   |   |   |
                +---+---+---+---+---+---+---+---+---+---+
              H |   |   | □ | □ | □ |   |   |   |   |   |
                +---+---+---+---+---+---+---+---+---+---+
              I |   |   |   |   |   |   |   |   |   |   |
                +---+---+---+---+---+---+---+---+---+---+
              J | □ | □ | □ |   | □ | □ | □ | □ | □ |   |
                +---+---+---+---+---+---+---+---+---+---+
                  """;

        var grid = getPopulatedGrid();
        System.out.println(grid);
        Assert.assertEquals(POPULATED_GRID_STRING, grid.toString());
    }

    @Test
    public void ShipGrid_HitsAreCorrect() {

        String POPULATED_GRID_STRING = """
                  1   2   3   4   5   6   7   8   9   10
                +---+---+---+---+---+---+---+---+---+---+
              A |   |   |   |   |   |   |   |   |   |   |
                +---+---+---+---+---+---+---+---+---+---+
              B |   |   |   |   |   |   |   |   |   |   |
                +---+---+---+---+---+---+---+---+---+---+
              C |   |   | ~ |   |   |   |   |   |   |   |
                +---+---+---+---+---+---+---+---+---+---+
              D |   |   |   |   | X | □ | □ | □ |   |   |
                +---+---+---+---+---+---+---+---+---+---+
              E |   |   |   |   |   |   |   |   |   |   |
                +---+---+---+---+---+---+---+---+---+---+
              F |   |   |   |   | □ | □ |   |   |   |   |
                +---+---+---+---+---+---+---+---+---+---+
              G |   |   |   |   |   |   |   |   |   |   |
                +---+---+---+---+---+---+---+---+---+---+
              H |   |   | □ | □ | □ |   |   |   |   |   |
                +---+---+---+---+---+---+---+---+---+---+
              I |   |   |   |   |   |   |   |   |   |   |
                +---+---+---+---+---+---+---+---+---+---+
              J | □ | □ | □ |   | □ | □ | □ | □ | □ |   |
                +---+---+---+---+---+---+---+---+---+---+
                """;

        var grid = getPopulatedGrid();
        var hitShip = grid.sendTorpedo('D', 5);
        var hitWater = grid.sendTorpedo('C', 3);

        Assert.assertTrue(hitShip);
        Assert.assertFalse(hitWater);
        System.out.println(grid);
        Assert.assertEquals(POPULATED_GRID_STRING, grid.toString());
    }

    @Test
    public void ShipGrid_ShipIsSunk() {

        var grid = new ShipGrid(0L);

        var ship = new Ship(5);
        var ship2 = new Ship(4);
        var ship3 = new Ship(3);
        var ship4 = new Ship(3);
        var ship5 = new Ship(2);

        grid.addShip(ship);
        grid.addShip(ship2);
        grid.addShip(ship3);
        grid.addShip(ship4);
        grid.addShip(ship5);

        grid.sendTorpedo('D', 5);
        grid.sendTorpedo('D', 6);
        grid.sendTorpedo('D', 7);
        grid.sendTorpedo('D', 8);

        Assert.assertTrue(ship2.isSunk());

        System.out.println(grid);
    }

}
