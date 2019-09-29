package ru.ssau.tk.sergunin.lab.functions.factory;

import org.testng.annotations.Test;
import ru.ssau.tk.sergunin.lab.functions.*;

import static org.testng.Assert.*;
import static org.testng.Assert.assertThrows;

public class TabulatedFunctionFactoryTest {
    private final double[] xValues = new double[]{1., 2., 3., 4., 5.};
    private final double[] yValues = new double[]{2., 4., 6., 8., 10.};

    @Test
    public void testCreate() {
        TabulatedFunctionFactory arrayTabulatedFunctionFactory = new ArrayTabulatedFunctionFactory();
        TabulatedFunctionFactory linkedListTabulatedFunctionFactory = new LinkedListTabulatedFunctionFactory();

        TabulatedFunction func = arrayTabulatedFunctionFactory.create(xValues, yValues);
        assertTrue(func instanceof ArrayTabulatedFunction);
        func = linkedListTabulatedFunctionFactory.create(xValues, yValues);
        assertTrue(func instanceof LinkedListTabulatedFunction);
    }

    @Test
    public void testCreateStrict() {
        TabulatedFunctionFactory arrayTabulatedFunctionFactory = new ArrayTabulatedFunctionFactory();
        TabulatedFunctionFactory linkedListTabulatedFunctionFactory = new LinkedListTabulatedFunctionFactory();

        TabulatedFunction func = arrayTabulatedFunctionFactory.createStrict(xValues, yValues);
        assertTrue(func instanceof StrictTabulatedFunction);
        func = linkedListTabulatedFunctionFactory.createStrict(xValues, yValues);
        assertTrue(func instanceof StrictTabulatedFunction);
    }

    @Test
    public void testCreateStrictUnmodifiable() {
        TabulatedFunctionFactory arrayTabulatedFunctionFactory = new ArrayTabulatedFunctionFactory();
        TabulatedFunction func = arrayTabulatedFunctionFactory.createStrictUnmodifiable(xValues, yValues);
        assertTrue(func instanceof UnmodifiableTabulatedFunction);
        assertEquals(func.apply(2), 4);
        assertEquals(func.getCount(), 5);
        assertEquals(func.getX(0), 1);
        assertEquals(func.getY(0), 2);
        assertEquals(func.indexOfX(1), 0);
        assertEquals(func.indexOfY(2), 0);
        assertEquals(func.leftBound(), 1);
        assertEquals(func.rightBound(), 5);
        assertEquals(func.iterator().next().x, 1);
        assertThrows(UnsupportedOperationException.class, () -> func.setY(1, 0));
        assertThrows(UnsupportedOperationException.class, () -> func.apply(2.5));
    }
}