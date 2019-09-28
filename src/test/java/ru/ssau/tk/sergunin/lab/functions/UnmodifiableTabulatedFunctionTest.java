package ru.ssau.tk.sergunin.lab.functions;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class UnmodifiableTabulatedFunctionTest {

    // TODO

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void test() {
        TabulatedFunction list = new UnmodifiableTabulatedFunction(new LinkedListTabulatedFunction(new double[]{1., 2., 3., 4., 5.}, new double[]{2., 4., 6., 8., 10.}));
        assertEquals(list.apply(2), 4);
        assertEquals(list.getCount(), 5);
        assertEquals(list.getX(0), 1);
        assertEquals(list.getY(0), 2);
        assertEquals(list.indexOfX(1), 0);
        assertEquals(list.indexOfY(2), 0);
        assertEquals(list.leftBound(), 1);
        assertEquals(list.rightBound(), 5);
        assertEquals(list.iterator().next().x, 1);
        assertEquals(((UnmodifiableTabulatedFunction) list).floorIndexOfX(1.5), 0);
        assertEquals(((UnmodifiableTabulatedFunction) list).extrapolateLeft(0), 0);
        assertEquals(((UnmodifiableTabulatedFunction) list).extrapolateRight(6), 12);
        assertEquals(((UnmodifiableTabulatedFunction) list).interpolate(1.5, 0), 3);
        list.setY(1, 0);
    }
}