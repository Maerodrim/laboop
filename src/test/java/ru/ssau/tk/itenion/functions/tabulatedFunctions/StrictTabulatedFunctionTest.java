package ru.ssau.tk.itenion.functions.tabulatedFunctions;

import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertThrows;

public class StrictTabulatedFunctionTest {

    @Test
    public void test() {
        TabulatedFunction array = new StrictTabulatedFunction(new ArrayTabulatedFunction(new double[]{1., 2., 3., 4., 5.}, new double[]{2., 4., 6., 8., 10.}));
        Assert.assertEquals(array.apply(2.), 4);
        assertEquals(array.getCount(), 5);
        assertEquals(array.getX(0), 1);
        array.setY(1, 0);
        assertEquals(array.getY(1), 0);
        assertEquals(array.indexOfX(1), 0);
        assertEquals(array.indexOfY(2), 0);
        assertEquals(array.leftBound(), 1);
        assertEquals(array.rightBound(), 5);
        Assert.assertEquals(array.iterator().next().x, 1);
        assertThrows(UnsupportedOperationException.class, () -> array.apply(2.5));
    }

    @Test
    public void test2() {
        TabulatedFunction array = new StrictTabulatedFunction(new UnmodifiableTabulatedFunction(new ArrayTabulatedFunction(new double[]{1., 2., 3., 4., 5.}, new double[]{2., 4., 6., 8., 10.})));
        Assert.assertEquals(array.apply(2.), 4);
        assertEquals(array.getCount(), 5);
        assertEquals(array.getX(0), 1);
        assertEquals(array.getY(0), 2);
        assertEquals(array.indexOfX(1), 0);
        assertEquals(array.indexOfY(2), 0);
        assertEquals(array.leftBound(), 1);
        assertEquals(array.rightBound(), 5);
        Assert.assertEquals(array.iterator().next().x, 1);
        assertThrows(UnsupportedOperationException.class, () -> array.setY(1, 0));
        assertThrows(UnsupportedOperationException.class, () -> array.apply(2.5));
    }

}