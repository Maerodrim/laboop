package ru.ssau.tk.sergunin.lab.functions.factory;

import org.testng.annotations.Test;
import ru.ssau.tk.sergunin.lab.functions.*;

import static org.testng.Assert.*;

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
        TabulatedFunction array = (new ArrayTabulatedFunctionFactory()).createStrict(xValues, yValues);
        TabulatedFunction list = (new LinkedListTabulatedFunctionFactory()).createStrict(xValues, yValues);
        assertTrue(array instanceof StrictTabulatedFunction);
        assertEquals(array.apply(2), 4);
        assertEquals(array.getCount(), 5);
        assertEquals(array.getX(0), 1);
        assertEquals(array.getY(0), 2);
        assertEquals(array.indexOfX(1), 0);
        assertEquals(array.indexOfY(2), 0);
        assertEquals(array.leftBound(), 1);
        assertEquals(array.rightBound(), 5);
        assertEquals(array.iterator().next().x, 1);
        array.setY(1, 0);
        assertEquals(array.getY(1), 0);
        assertThrows(UnsupportedOperationException.class, () -> array.apply(2.5));

        assertTrue(list instanceof StrictTabulatedFunction);
        assertEquals(list.apply(2), 4);
        assertEquals(list.getCount(), 5);
        assertEquals(list.getX(0), 1);
        assertEquals(list.getY(0), 2);
        assertEquals(list.indexOfX(1), 0);
        assertEquals(list.indexOfY(2), 0);
        assertEquals(list.leftBound(), 1);
        assertEquals(list.rightBound(), 5);
        assertEquals(list.iterator().next().x, 1);
        list.setY(1, 0);
        assertEquals(list.getY(1), 0);
        assertThrows(UnsupportedOperationException.class, () -> list.apply(2.5));
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

    @Test
    public void testCreateUnmodifiable() {
        TabulatedFunction array = (new ArrayTabulatedFunctionFactory()).createUnmodifiable(xValues, yValues);
        TabulatedFunction list = (new LinkedListTabulatedFunctionFactory()).createUnmodifiable(xValues, yValues);
        assertTrue(array instanceof UnmodifiableTabulatedFunction);
        assertEquals(array.apply(2), 4);
        assertEquals(array.getCount(), 5);
        assertEquals(array.getX(0), 1);
        assertEquals(array.getY(0), 2);
        assertEquals(array.indexOfX(1), 0);
        assertEquals(array.indexOfY(2), 0);
        assertEquals(array.leftBound(), 1);
        assertEquals(array.rightBound(), 5);
        assertEquals(array.iterator().next().x, 1);
        assertEquals(array.apply(2.5), 5);
        assertThrows(UnsupportedOperationException.class, () -> array.setY(1, 0));

        assertTrue(list instanceof UnmodifiableTabulatedFunction);
        assertEquals(list.apply(2), 4);
        assertEquals(list.getCount(), 5);
        assertEquals(list.getX(0), 1);
        assertEquals(list.getY(0), 2);
        assertEquals(list.indexOfX(1), 0);
        assertEquals(list.indexOfY(2), 0);
        assertEquals(list.leftBound(), 1);
        assertEquals(list.rightBound(), 5);
        assertEquals(list.iterator().next().x, 1);
        assertEquals(list.apply(2.5), 5);
        assertThrows(UnsupportedOperationException.class, () -> list.setY(1, 0));
    }
}