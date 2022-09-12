import org.junit.Assert;
import org.junit.Test;

public class ShipGridTests {

    @Test
    public void ShipGrid_PrintsEmptyGrid() {

        String EMPTY_GRID_STRING = """
                +---+---+---+---+---+---+---+---+---+---+
                |   |   |   |   |   |   |   |   |   |   |
                +---+---+---+---+---+---+---+---+---+---+
                |   |   |   |   |   |   |   |   |   |   |
                +---+---+---+---+---+---+---+---+---+---+
                |   |   |   |   |   |   |   |   |   |   |
                +---+---+---+---+---+---+---+---+---+---+
                |   |   |   |   |   |   |   |   |   |   |
                +---+---+---+---+---+---+---+---+---+---+
                |   |   |   |   |   |   |   |   |   |   |
                +---+---+---+---+---+---+---+---+---+---+
                |   |   |   |   |   |   |   |   |   |   |
                +---+---+---+---+---+---+---+---+---+---+
                |   |   |   |   |   |   |   |   |   |   |
                +---+---+---+---+---+---+---+---+---+---+
                |   |   |   |   |   |   |   |   |   |   |
                +---+---+---+---+---+---+---+---+---+---+
                |   |   |   |   |   |   |   |   |   |   |
                +---+---+---+---+---+---+---+---+---+---+
                |   |   |   |   |   |   |   |   |   |   |
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
                +---+---+---+---+---+---+---+---+---+---+
                |   |   |   |   |   |   |   |   |   |   |
                +---+---+---+---+---+---+---+---+---+---+
                |   |   |   |   |   |   |   |   |   |   |
                +---+---+---+---+---+---+---+---+---+---+
                |   |   |   |   |   |   |   |   |   |   |
                +---+---+---+---+---+---+---+---+---+---+
                |   |   |   |   | □ | □ | □ | □ |   |   |
                +---+---+---+---+---+---+---+---+---+---+
                |   |   |   |   |   |   |   |   |   |   |
                +---+---+---+---+---+---+---+---+---+---+
                |   |   |   |   | □ | □ |   |   |   |   |
                +---+---+---+---+---+---+---+---+---+---+
                |   |   |   |   |   |   |   |   |   |   |
                +---+---+---+---+---+---+---+---+---+---+
                |   |   | □ | □ | □ |   |   |   |   |   |
                +---+---+---+---+---+---+---+---+---+---+
                |   |   |   |   |   |   |   |   |   |   |
                +---+---+---+---+---+---+---+---+---+---+
                | □ | □ | □ |   | □ | □ | □ | □ | □ |   |
                +---+---+---+---+---+---+---+---+---+---+
                  """;

        var grid = getPopulatedGrid();
        System.out.println(grid);
        Assert.assertEquals(POPULATED_GRID_STRING, grid.toString());
    }
}
