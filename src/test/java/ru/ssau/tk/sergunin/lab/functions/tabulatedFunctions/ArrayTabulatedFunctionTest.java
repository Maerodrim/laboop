package ru.ssau.tk.sergunin.lab.functions.tabulatedFunctions;

import org.testng.Assert;
import org.testng.annotations.Test;
import ru.ssau.tk.sergunin.lab.exceptions.ArrayIsNotSortedException;
import ru.ssau.tk.sergunin.lab.exceptions.DifferentLengthOfArraysException;
import ru.ssau.tk.sergunin.lab.exceptions.InterpolationException;
import ru.ssau.tk.sergunin.lab.functions.MathFunction;
import ru.ssau.tk.sergunin.lab.functions.Point;
import ru.ssau.tk.sergunin.lab.functions.powerFunctions.SqrFunction;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertThrows;

public class ArrayTabulatedFunctionTest {
    private final double[] xValues = new double[]{1., 2., 3., 4., 5.};
    private final double[] yValues = new double[]{2., 4., 6., 8., 10.};
    private final double ACCURACY = 0.00001;
    private MathFunction funcF = new SqrFunction();

    private ArrayTabulatedFunction initializeArrayThroughTwoArrays() {
        return new ArrayTabulatedFunction(xValues, yValues);
    }

    private ArrayTabulatedFunction initializeArrayThroughMathFunction() {
        return new ArrayTabulatedFunction(funcF, 10, 0, 11);
    }

    @Test
    public void testConstructor() {
        ArrayTabulatedFunction firstArray = initializeArrayThroughTwoArrays();
        for (int i = 0; i < 5; i++) {
            assertEquals(firstArray.getY(i), 2 * (i + 1), ACCURACY);
        }
        ArrayTabulatedFunction secondArray = initializeArrayThroughMathFunction();
        for (int i = 0; i < 11; i++) {
            assertEquals(secondArray.getY(i), i * i, ACCURACY);
        }
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionInArrayInitializeThroughTwoArrays() {
        new ArrayTabulatedFunction(new double[]{1}, new double[]{2});
    }

    @Test(expectedExceptions = DifferentLengthOfArraysException.class)
    public void testDifferentLengthOfArraysExceptionInArrayInitializeThroughTwoArrays() {
        new ArrayTabulatedFunction(new double[]{1, 2}, new double[]{2});
    }

    @Test(expectedExceptions = ArrayIsNotSortedException.class)
    public void testArrayIsNotSortedExceptionInArrayInitializeThroughTwoArrays() {
        new ArrayTabulatedFunction(new double[]{2, 1}, new double[]{1, 1});
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionInArrayInitializeThroughMathFunction() {
        new ArrayTabulatedFunction(funcF, 0, 0, 1);
    }

    @Test
    public void testGetCount() {
        ArrayTabulatedFunction firstArray = initializeArrayThroughTwoArrays();
        ArrayTabulatedFunction secondArray = initializeArrayThroughMathFunction();
        assertEquals(firstArray.getCount(), 5, ACCURACY);
        assertEquals(secondArray.getCount(), 11, ACCURACY);
    }

    @Test
    public void testLeftBound() {
        ArrayTabulatedFunction firstArray = initializeArrayThroughTwoArrays();
        ArrayTabulatedFunction secondArray = initializeArrayThroughMathFunction();
        assertEquals(firstArray.leftBound(), 1, ACCURACY);
        assertEquals(secondArray.leftBound(), 0, ACCURACY);
    }

    @Test
    public void testRightBound() {
        ArrayTabulatedFunction firstArray = initializeArrayThroughTwoArrays();
        ArrayTabulatedFunction secondArray = initializeArrayThroughMathFunction();
        assertEquals(firstArray.rightBound(), 5, ACCURACY);
        assertEquals(secondArray.rightBound(), 10, ACCURACY);
    }


    @Test
    public void testGetX() {
        ArrayTabulatedFunction firstArray = initializeArrayThroughTwoArrays();
        ArrayTabulatedFunction secondArray = initializeArrayThroughMathFunction();
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
        ArrayTabulatedFunction firstArray = initializeArrayThroughTwoArrays();
        ArrayTabulatedFunction secondArray = initializeArrayThroughMathFunction();
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
        ArrayTabulatedFunction firstArray = initializeArrayThroughTwoArrays();
        ArrayTabulatedFunction secondArray = initializeArrayThroughMathFunction();
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
        ArrayTabulatedFunction firstArray = initializeArrayThroughTwoArrays();
        ArrayTabulatedFunction secondArray = initializeArrayThroughMathFunction();
        for (int i = 1; i <= firstArray.getCount(); i++) {
            assertEquals(firstArray.indexOfX(i), i - 1, ACCURACY);
        }
        for (int i = 0; i < secondArray.getCount(); i++) {
            assertEquals(secondArray.indexOfX(i), i, ACCURACY);
        }
    }

    @Test
    public void testIndexOfY() {
        ArrayTabulatedFunction firstArray = initializeArrayThroughTwoArrays();
        ArrayTabulatedFunction secondArray = initializeArrayThroughMathFunction();
        for (int i = 0; i < firstArray.getCount(); i++) {
            assertEquals(firstArray.indexOfY(2 * (i + 1)), i, ACCURACY);
        }
        for (int i = 0; i < secondArray.getCount(); i++) {
            assertEquals(secondArray.indexOfY(i * i), i, ACCURACY);
        }
        assertEquals(secondArray.indexOfY(50), -1);
    }

    @Test
    public void testFloorIndexOfX() {
        ArrayTabulatedFunction firstArray = initializeArrayThroughTwoArrays();
        ArrayTabulatedFunction secondArray = initializeArrayThroughMathFunction();
        assertEquals(firstArray.floorIndexOfX(3.5), 2, ACCURACY);
        assertEquals(firstArray.floorIndexOfX(6), 5, ACCURACY);
        assertEquals(secondArray.floorIndexOfX(5.5), 5, ACCURACY);
        assertEquals(secondArray.floorIndexOfX(11), 11, ACCURACY);
        Assert.assertThrows(IllegalArgumentException.class, () -> firstArray.floorIndexOfX(0));
    }

    @Test
    public void testExtrapolateLeft() {
        ArrayTabulatedFunction firstArray = initializeArrayThroughTwoArrays();
        ArrayTabulatedFunction secondArray = initializeArrayThroughMathFunction();
        assertEquals(firstArray.extrapolateLeft(0), 0, ACCURACY);
        assertEquals(secondArray.extrapolateLeft(0.5), 0.5, ACCURACY);
    }

    @Test
    public void testExtrapolateRight() {
        ArrayTabulatedFunction firstArray = initializeArrayThroughTwoArrays();
        ArrayTabulatedFunction secondArray = initializeArrayThroughMathFunction();
        assertEquals(firstArray.extrapolateRight(6), 12, ACCURACY);
        assertEquals(secondArray.extrapolateRight(11), 119, ACCURACY);
    }

    @Test
    public void testInterpolate() {
        ArrayTabulatedFunction firstArray = initializeArrayThroughTwoArrays();
        ArrayTabulatedFunction secondArray = initializeArrayThroughMathFunction();
        assertEquals(firstArray.interpolate(2.5, 1), 5, ACCURACY);
        assertEquals(secondArray.interpolate(1.5, 1), 2.5, ACCURACY);
        assertThrows(InterpolationException.class, () -> secondArray.interpolate(1.5, 2));
    }

    @Test
    public void testInsertWithInitializeArrayThroughTwoArrays() {
        ArrayTabulatedFunction firstArray = initializeArrayThroughTwoArrays();
        firstArray.insert(-2, 5);
        firstArray.insert(1, 10);
        firstArray.insert(3.5, 5);
        firstArray.insert(7, 5);
        assertEquals(firstArray.getY(0), 5, ACCURACY);
        assertEquals(firstArray.getY(1), 10, ACCURACY);
        assertEquals(firstArray.indexOfX(3.5), 4, ACCURACY);
        assertEquals(firstArray.indexOfX(7), 7, ACCURACY);
    }

    @Test
    public void testInsertWithInitializeArrayThroughMathFunction() {
        ArrayTabulatedFunction secondArray = initializeArrayThroughMathFunction();
        secondArray.insert(-2, 5);
        secondArray.insert(1, 10);
        secondArray.insert(3.5, 5);
        secondArray.insert(7, 5);
        assertEquals(secondArray.getY(0), 5, ACCURACY);
        assertEquals(secondArray.getY(2), 10, ACCURACY);
        assertEquals(secondArray.indexOfX(3.5), 5, ACCURACY);
        assertEquals(secondArray.indexOfX(7), 9, ACCURACY);
    }

    @Test
    public void testApply() {
        ArrayTabulatedFunction firstArray = initializeArrayThroughTwoArrays();
        ArrayTabulatedFunction secondArray = initializeArrayThroughMathFunction();
        for (int i = -5; i < xValues.length + 5; i++) {
            assertEquals(firstArray.apply((i < 0 || i >= xValues.length) ? i : firstArray.getX(i)), (i < 0 || i >= xValues.length) ? i * 2 : yValues[i], ACCURACY);
        }
        for (int i = 0; i < secondArray.getCount(); i++) {
            assertEquals(secondArray.apply(secondArray.getX(i)), i * i, ACCURACY);
        }
    }

    @Test
    public void testRemove() {
        ArrayTabulatedFunction secondArray = initializeArrayThroughMathFunction();
        secondArray.remove(secondArray.getCount() - 2);
        assertEquals(secondArray.rightBound(), 10, ACCURACY);
        secondArray.remove(0);
        assertEquals(secondArray.leftBound(), 1, ACCURACY);
        secondArray.remove(8);
        assertEquals(secondArray.getCount(), 8, ACCURACY);
        Assert.assertThrows(ArrayIndexOutOfBoundsException.class, () -> secondArray.remove(-1));
    }

    @Test
    public void testIterator() {
        ArrayTabulatedFunction firstArray = initializeArrayThroughTwoArrays();
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
}