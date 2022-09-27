import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class InputValidationTest {
    @Test
    public void CoordinateInput_MustBeRowColumn() {

        var coordsAreValid = A2.isValidCoords("5A");
        
        assertFalse(coordsAreValid);
    }

    @Test
    public void CoordinateInput_AcceptsCorrect() {
        
        var coordsAreValid = A2.isValidCoords("A5");
        
        assertTrue(coordsAreValid);
    }

    @Test
    public void CoordinateInput_Allows2DigitColumn() {
        
        var coordsAreValid = A2.isValidCoords("A10");
        
        assertTrue(coordsAreValid);
    }

    @Test
    public void CoordinateInput_FailsOutsideOfRange() {
        
        List<Boolean> validResults = new ArrayList<>();

        validResults.add(A2.isValidCoords("K5"));
        validResults.add(A2.isValidCoords("AA5"));
        validResults.add(A2.isValidCoords("A11"));
        validResults.add(A2.isValidCoords("a10"));
        
        assertTrue(validResults.stream().allMatch(result -> result == false));
    }
}
