package ru.ssau.tk.sergunin.lab.functions;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertThrows;

public class UnmodifiableTabulatedFunctionTest {

    @Test
    public void test() {
        UnmodifiableTabulatedFunction list = new UnmodifiableTabulatedFunction(new LinkedListTabulatedFunction(new double[]{1., 2., 3., 4., 5.}, new double[]{2., 4., 6., 8., 10.}));
        assertEquals(list.apply(2), 4);
        assertEquals(list.getCount(), 5);
        assertEquals(list.getX(0), 1);
        assertEquals(list.getY(0), 2);
        assertEquals(list.indexOfX(1), 0);
        assertEquals(list.indexOfY(2), 0);
        assertEquals(list.leftBound(), 1);
        assertEquals(list.rightBound(), 5);
        assertEquals(list.iterator().next().x, 1);
        assertThrows(UnsupportedOperationException.class, () -> list.setY(1, 0));
    }
}