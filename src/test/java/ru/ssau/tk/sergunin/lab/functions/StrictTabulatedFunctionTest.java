package ru.ssau.tk.sergunin.lab.functions;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class StrictTabulatedFunctionTest {

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void test() {
        StrictTabulatedFunction array = new StrictTabulatedFunction(new ArrayTabulatedFunction(new double[]{1., 2., 3., 4., 5.}, new double[]{2., 4., 6., 8., 10.}));
        assertEquals(array.apply(2), 4);
        assertEquals(array.getCount(), 5);
        assertEquals(array.getX(0), 1);
        array.setY(1, 0);
        assertEquals(array.getY(1), 0);
        assertEquals(array.indexOfX(1), 0);
        assertEquals(array.indexOfY(2), 0);
        assertEquals(array.leftBound(), 1);
        assertEquals(array.rightBound(), 5);
        assertEquals(array.iterator().next().x, 1);
        array.apply(2.5);
    }

}