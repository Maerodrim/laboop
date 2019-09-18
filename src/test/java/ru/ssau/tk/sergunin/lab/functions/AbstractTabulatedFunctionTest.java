package ru.ssau.tk.sergunin.lab.functions;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class AbstractTabulatedFunctionTest {

    private MockTabulatedFunction mockObj = new MockTabulatedFunction();
    private double accuracy = 0.00001;

    @Test
    public void testInterpolate() {
        assertEquals(mockObj.interpolate(-0.5, 0, 1, 5, 10), 2.5, accuracy);
        assertEquals(mockObj.interpolate(0, 0, 1, 5, 10), 5, accuracy);
        assertEquals(mockObj.interpolate(0.5, 0, 1, 5, 10), 7.5, accuracy);
        assertEquals(mockObj.interpolate(1, 0, 1, 5, 10), 10, accuracy);
        assertEquals(mockObj.interpolate(1.5, 0, 1, 5, 10), 12.5, accuracy);
    }

    @Test
    public void testApply() {
        assertEquals(mockObj.apply(-0.5), 2.5, accuracy);
        assertEquals(mockObj.apply(0), 5, accuracy);
        assertEquals(mockObj.apply(0.5), 7.5, accuracy);
        assertEquals(mockObj.apply(1), 10, accuracy);
        assertEquals(mockObj.apply(1.5), 12.5, accuracy);
    }
}