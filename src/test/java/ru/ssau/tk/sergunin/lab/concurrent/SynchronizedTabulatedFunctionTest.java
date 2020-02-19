package ru.ssau.tk.sergunin.lab.concurrent;


import org.testng.Assert;
import org.testng.annotations.Test;
import ru.ssau.tk.sergunin.lab.functions.MathFunction;
import ru.ssau.tk.sergunin.lab.functions.Point;
import ru.ssau.tk.sergunin.lab.functions.powerFunctions.SqrFunction;
import ru.ssau.tk.sergunin.lab.functions.tabulatedFunctions.ArrayTabulatedFunction;
import ru.ssau.tk.sergunin.lab.functions.tabulatedFunctions.TabulatedFunction;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertThrows;

public class SynchronizedTabulatedFunctionTest {
    private final double[] xValues = new double[]{1., 2., 3., 4., 5.};
    private final double[] yValues = new double[]{2., 4., 6., 8., 10.};
    private final double ACCURACY = 0.00001;
    private MathFunction funcF = new SqrFunction();


    private SynchronizedTabulatedFunction initializeArrayThroughTwoArrays() {
        TabulatedFunction func = new ArrayTabulatedFunction(xValues, yValues);
        return new SynchronizedTabulatedFunction(func);
    }

    private SynchronizedTabulatedFunction initializeArrayThroughMathFunction() {
        TabulatedFunction func = new ArrayTabulatedFunction(funcF, 10, 0, 11);
        return new SynchronizedTabulatedFunction(func);
    }

    @Test
    public void testGetCount() {
        SynchronizedTabulatedFunction firstArray = initializeArrayThroughTwoArrays();
        SynchronizedTabulatedFunction secondArray = initializeArrayThroughMathFunction();
        assertEquals(firstArray.getCount(), 5, ACCURACY);
        assertEquals(secondArray.getCount(), 11, ACCURACY);
    }

    @Test
    public void testLeftBound() {
        SynchronizedTabulatedFunction firstArray = initializeArrayThroughTwoArrays();
        SynchronizedTabulatedFunction secondArray = initializeArrayThroughMathFunction();
        assertEquals(firstArray.leftBound(), 1, ACCURACY);
        assertEquals(secondArray.leftBound(), 0, ACCURACY);
    }

    @Test
    public void testRightBound() {
        SynchronizedTabulatedFunction firstArray = initializeArrayThroughTwoArrays();
        SynchronizedTabulatedFunction secondArray = initializeArrayThroughMathFunction();
        assertEquals(firstArray.rightBound(), 5, ACCURACY);
        assertEquals(secondArray.rightBound(), 10, ACCURACY);
    }


    @Test
    public void testGetX() {
        SynchronizedTabulatedFunction firstArray = initializeArrayThroughTwoArrays();
        SynchronizedTabulatedFunction secondArray = initializeArrayThroughMathFunction();
        for (int i = 0; i < firstArray.getCount(); ) {
            assertEquals(firstArray.getX(i), ++i, ACCURACY);
        }
        for (int i = 0; i < secondArray.getCount(); ) {
            assertEquals(secondArray.getX(i), i++, ACCURACY);
        }
        Assert.assertThrows(ArrayIndexOutOfBoundsException.class, () -> secondArray.getX(-1));
    }

    @Test
    public void testGetY() {
        SynchronizedTabulatedFunction firstArray = initializeArrayThroughTwoArrays();
        SynchronizedTabulatedFunction secondArray = initializeArrayThroughMathFunction();
        for (int i = 0; i < firstArray.getCount(); ) {
            assertEquals(firstArray.getY(i), 2 * (++i), ACCURACY);
        }
        for (int i = 0; i < secondArray.getCount(); ) {
            assertEquals(secondArray.getY(i), i * i++, ACCURACY);
        }
        Assert.assertThrows(ArrayIndexOutOfBoundsException.class, () -> secondArray.getY(-1));
    }

    @Test
    public void testSetY() {
        SynchronizedTabulatedFunction firstArray = initializeArrayThroughTwoArrays();
        SynchronizedTabulatedFunction secondArray = initializeArrayThroughMathFunction();
        firstArray.setY(4, 13);
        assertEquals(firstArray.getY(4), 13, ACCURACY);
        firstArray.setY(2, 5);
        assertEquals(firstArray.getY(2), 5, ACCURACY);
        secondArray.setY(7, 25);
        assertEquals(secondArray.getY(7), 25, ACCURACY);
        secondArray.setY(5, 10);
        assertEquals(secondArray.getY(5), 10, ACCURACY);
        Assert.assertThrows(ArrayIndexOutOfBoundsException.class, () -> secondArray.setY(-1, 0));
    }

    @Test
    public void testIndexOfX() {
        SynchronizedTabulatedFunction firstArray = initializeArrayThroughTwoArrays();
        SynchronizedTabulatedFunction secondArray = initializeArrayThroughMathFunction();
        for (int i = 1; i <= firstArray.getCount(); i++) {
            assertEquals(firstArray.indexOfX(i), i - 1, ACCURACY);
        }
        for (int i = 0; i < secondArray.getCount(); i++) {
            assertEquals(secondArray.indexOfX(i), i, ACCURACY);
        }
    }

    @Test
    public void testIndexOfY() {
        SynchronizedTabulatedFunction firstArray = initializeArrayThroughTwoArrays();
        SynchronizedTabulatedFunction secondArray = initializeArrayThroughMathFunction();
        for (int i = 0; i < firstArray.getCount(); i++) {
            assertEquals(firstArray.indexOfY(2 * (i + 1)), i, ACCURACY);
        }
        for (int i = 0; i < secondArray.getCount(); i++) {
            assertEquals(secondArray.indexOfY(i * i), i, ACCURACY);
        }
        assertEquals(secondArray.indexOfY(50), -1);
    }

    @Test
    public void testApply() {
        SynchronizedTabulatedFunction firstArray = initializeArrayThroughTwoArrays();
        SynchronizedTabulatedFunction secondArray = initializeArrayThroughMathFunction();
        for (int i = -5; i < xValues.length + 5; i++) {
            assertEquals(firstArray.apply((i < 0 || i >= xValues.length) ? i : firstArray.getX(i)), (i < 0 || i >= xValues.length) ? i * 2 : yValues[i], ACCURACY);
        }
        for (int i = 0; i < secondArray.getCount(); i++) {
            assertEquals(secondArray.apply(secondArray.getX(i)), i * i, ACCURACY);
        }
    }

    @Test
    public void testIterator() {
        SynchronizedTabulatedFunction firstArray = initializeArrayThroughTwoArrays();
        Iterator<Point> iterator = firstArray.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            Point point = iterator.next();
            assertEquals(point.x, firstArray.getX(i), 0.0001);
            assertEquals(point.y, firstArray.getY(i++), 0.0001);
        }
        i = 0;
        for (Point point : firstArray) {
            assertEquals(point.x, firstArray.getX(i), 0.0001);
            assertEquals(point.y, firstArray.getY(i++), 0.0001);
        }
        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    public void testDoSynchronously() {
        SynchronizedTabulatedFunction firstFunction = initializeArrayThroughTwoArrays();
        Assert.assertEquals((int) firstFunction.doSynchronously(SynchronizedTabulatedFunction::getCount), 5);
        Assert.assertEquals((double) firstFunction.doSynchronously(SynchronizedTabulatedFunction::leftBound), 1.);
        Assert.assertEquals((double) firstFunction.doSynchronously(SynchronizedTabulatedFunction::rightBound), 5.);
        Assert.assertNull(firstFunction.doSynchronously(x -> {
            x.setY(1, 2);
            return null;
        }));
    }
}
