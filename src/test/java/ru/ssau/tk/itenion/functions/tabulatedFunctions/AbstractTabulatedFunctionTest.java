package ru.ssau.tk.itenion.functions.tabulatedFunctions;

import org.testng.annotations.Test;
import ru.ssau.tk.itenion.functions.powerFunctions.PowFunction;

import static org.testng.Assert.assertEquals;

public class AbstractTabulatedFunctionTest {

    private MockTabulatedFunction mockObj = new MockTabulatedFunction();
    private double ACCURACY = 0.00001;

    @Test
    public void testInterpolate() {
        assertEquals(mockObj.interpolate(-0.5, 0, 1, 5, 10), 2.5, ACCURACY);
        assertEquals(mockObj.interpolate(0, 0, 1, 5, 10), 5, ACCURACY);
        assertEquals(mockObj.interpolate(0.5, 0, 1, 5, 10), 7.5, ACCURACY);
        assertEquals(mockObj.interpolate(1, 0, 1, 5, 10), 10, ACCURACY);
        assertEquals(mockObj.interpolate(1.5, 0, 1, 5, 10), 12.5, ACCURACY);
    }

    @Test
    public void testApply() {
        assertEquals(mockObj.apply(-0.5), 2.5, ACCURACY);
        assertEquals(mockObj.apply(0.), 5, ACCURACY);
        assertEquals(mockObj.apply(0.5), 7.5, ACCURACY);
        assertEquals(mockObj.apply(1.), 10, ACCURACY);
        assertEquals(mockObj.apply(1.5), 12.5, ACCURACY);
    }

    @Test
    public void testToString() {
        TabulatedFunction function = new LinkedListTabulatedFunction(new PowFunction(2.), 0, 1, 3);
        assertEquals(function.toString(), "LinkedListTabulatedFunction size = 3\n[0.0; 0.0]\n[0.5; 0.25]\n[1.0; 1.0]");
    }
}