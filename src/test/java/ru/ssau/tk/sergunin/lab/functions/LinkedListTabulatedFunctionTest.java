package ru.ssau.tk.sergunin.lab.functions;

import org.testng.annotations.Test;
import ru.ssau.tk.sergunin.lab.exceptions.ArrayIsNotSortedException;
import ru.ssau.tk.sergunin.lab.exceptions.DifferentLengthOfArraysException;
import ru.ssau.tk.sergunin.lab.exceptions.InterpolationException;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.testng.Assert.assertEquals;

public class LinkedListTabulatedFunctionTest {
    private final double[] xValues = new double[]{1., 2., 3., 4., 5.};
    private final double[] yValues = new double[]{2., 4., 6., 8., 10.};
    private final double ACCURACY = 0.00001;
    private MathFunction funcF = new SqrFunction();

    private LinkedListTabulatedFunction initializeLinkedListThroughTwoArrays() {
        return new LinkedListTabulatedFunction(xValues, yValues);
    }

    private LinkedListTabulatedFunction initializeLinkedListThroughMathFunction() {
        return new LinkedListTabulatedFunction(funcF, 10, 0, 11);
    }

    @Test
    public void testConstructor() {
        LinkedListTabulatedFunction firstList = initializeLinkedListThroughTwoArrays();
        for (int i = 0; i < 5; i++) {
            assertEquals(firstList.getY(i), 2 * (i + 1), ACCURACY);
        }
        LinkedListTabulatedFunction secondList = initializeLinkedListThroughMathFunction();
        for (int i = 0; i < 11; i++) {
            assertEquals(secondList.getY(i), i * i, ACCURACY);
        }
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionInLinkedListInitializeThroughTwoArrays() {
        LinkedListTabulatedFunction list = new LinkedListTabulatedFunction(new double[]{1}, new double[]{2});
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionInLinkedListInitializeThroughMathFunction() {
        LinkedListTabulatedFunction list = new LinkedListTabulatedFunction(funcF, 0, 0, 1);
    }

    @Test(expectedExceptions = DifferentLengthOfArraysException.class)
    public void testDifferentLengthOfArraysExceptionInLinkedListInitializeThroughTwoArrays() {
        LinkedListTabulatedFunction list = new LinkedListTabulatedFunction(new double[]{1, 2}, new double[]{2});
    }

    @Test(expectedExceptions = ArrayIsNotSortedException.class)
    public void testArrayIsNotSortedExceptionInLinkedListInitializeThroughTwoArrays() {
        LinkedListTabulatedFunction list = new LinkedListTabulatedFunction(new double[]{2, 1}, new double[]{2, 2});
    }

    @Test
    public void testAddNode() {
        LinkedListTabulatedFunction firstList = initializeLinkedListThroughTwoArrays();
        firstList.addNode(6, 12);
        assertEquals(firstList.rightBound(), 6, ACCURACY);
    }

    @Test
    public void testGetCount() {
        LinkedListTabulatedFunction firstList = initializeLinkedListThroughTwoArrays();
        LinkedListTabulatedFunction secondList = initializeLinkedListThroughMathFunction();
        assertEquals(firstList.getCount(), 5, ACCURACY);
        assertEquals(secondList.getCount(), 11, ACCURACY);
    }

    @Test
    public void testLeftBound() {
        LinkedListTabulatedFunction firstList = initializeLinkedListThroughTwoArrays();
        LinkedListTabulatedFunction secondList = initializeLinkedListThroughMathFunction();
        assertEquals(firstList.leftBound(), 1, ACCURACY);
        assertEquals(secondList.leftBound(), 0, ACCURACY);
    }

    @Test
    public void testRightBound() {
        LinkedListTabulatedFunction firstList = initializeLinkedListThroughTwoArrays();
        LinkedListTabulatedFunction secondList = initializeLinkedListThroughMathFunction();
        assertEquals(firstList.rightBound(), 5, ACCURACY);
        assertEquals(secondList.rightBound(), 10, ACCURACY);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testGetX() {
        LinkedListTabulatedFunction firstList = initializeLinkedListThroughTwoArrays();
        LinkedListTabulatedFunction secondList = initializeLinkedListThroughMathFunction();
        for (int i = 0; i < firstList.getCount();) {
            assertEquals(firstList.getX(i), ++i, ACCURACY);
        }
        for (int i = 0; i < secondList.getCount();) {
            assertEquals(secondList.getX(i), i++, ACCURACY);
        }
        assertEquals(secondList.getX(-1), 0, ACCURACY);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testGetY() {
        LinkedListTabulatedFunction firstList = initializeLinkedListThroughTwoArrays();
        LinkedListTabulatedFunction secondList = initializeLinkedListThroughMathFunction();
        for (int i = 0; i < firstList.getCount();) {
            assertEquals(firstList.getY(i), 2*(++i), ACCURACY);
        }
        for (int i = 0; i < secondList.getCount();) {
            assertEquals(secondList.getY(i), i*i++, ACCURACY);
        }
        assertEquals(secondList.getY(-1), 0, ACCURACY);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testSetY() {
        LinkedListTabulatedFunction firstList = initializeLinkedListThroughTwoArrays();
        LinkedListTabulatedFunction secondList = initializeLinkedListThroughMathFunction();
        firstList.setY(4, 13);
        assertEquals(firstList.getY(4), 13, ACCURACY);
        firstList.setY(2, 5);
        assertEquals(firstList.getY(2), 5, ACCURACY);
        secondList.setY(7, 25);
        assertEquals(secondList.getY(7), 25, ACCURACY);
        secondList.setY(5, 10);
        assertEquals(secondList.getY(5), 10, ACCURACY);
        secondList.setY(-1, 0);
    }

    @Test
    public void testIndexOfX() {
        LinkedListTabulatedFunction firstList = initializeLinkedListThroughTwoArrays();
        LinkedListTabulatedFunction secondList = initializeLinkedListThroughMathFunction();
        for (int i = 1; i <= firstList.getCount(); i++) {
            assertEquals(firstList.indexOfX(i), i-1, ACCURACY);
        }
        for (int i = 0; i < secondList.getCount(); i++) {
            assertEquals(secondList.indexOfX(i), i, ACCURACY);
        }
    }

    @Test
    public void testIndexOfY() {
        LinkedListTabulatedFunction firstList = initializeLinkedListThroughTwoArrays();
        LinkedListTabulatedFunction secondList = initializeLinkedListThroughMathFunction();
        for (int i = 0; i < firstList.getCount(); i++) {
            assertEquals(firstList.indexOfY(2*(i+1)), i, ACCURACY);
        }
        for (int i = 0; i < secondList.getCount(); i++) {
            assertEquals(secondList.indexOfY(i*i), i, ACCURACY);
        }
    }
    
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testFloorIndexOfX() {
        LinkedListTabulatedFunction firstList = initializeLinkedListThroughTwoArrays();
        LinkedListTabulatedFunction secondList = initializeLinkedListThroughMathFunction();
        assertEquals(firstList.floorIndexOfX(3.5), 2, ACCURACY);
        assertEquals(firstList.floorIndexOfX(6), 5, ACCURACY);
        assertEquals(secondList.floorIndexOfX(5.5), 5, ACCURACY);
        assertEquals(secondList.floorIndexOfX(11), 11, ACCURACY);
        assertEquals(firstList.floorIndexOfX(0), 0, ACCURACY);
    }

    @Test
    public void testExtrapolateLeft() {
        LinkedListTabulatedFunction firstList = initializeLinkedListThroughTwoArrays();
        LinkedListTabulatedFunction secondList = initializeLinkedListThroughMathFunction();
        assertEquals(firstList.extrapolateLeft(0), 0, ACCURACY);
        assertEquals(secondList.extrapolateLeft(0), 0, ACCURACY);
    }

    @Test
    public void testExtrapolateRight() {
        LinkedListTabulatedFunction firstList = initializeLinkedListThroughTwoArrays();
        LinkedListTabulatedFunction secondList = initializeLinkedListThroughMathFunction();
        assertEquals(firstList.extrapolateRight(6), 12, ACCURACY);
        assertEquals(secondList.extrapolateRight(11), 119, ACCURACY);
    }

    @Test(expectedExceptions = InterpolationException.class)
    public void testInterpolate() {
        LinkedListTabulatedFunction firstList = initializeLinkedListThroughTwoArrays();
        LinkedListTabulatedFunction secondList = initializeLinkedListThroughMathFunction();
        assertEquals(firstList.interpolate(2.5, 1), 5, ACCURACY);
        assertEquals(secondList.interpolate(1.5, 2), 2.5, ACCURACY);
        assertEquals(secondList.interpolate(1.5, 2), 2.5, ACCURACY);
    }

    @Test
    public void testInsertWithInitializeLinkedListThroughTwoArrays() {
        LinkedListTabulatedFunction firstList = initializeLinkedListThroughTwoArrays();
        firstList.insert(-2, 5);
        firstList.insert(1, 10);
        firstList.insert(3.5, 5);
        firstList.insert(7, 5);
        assertEquals(firstList.getY(0), 5, ACCURACY);
        assertEquals(firstList.getY(1), 10, ACCURACY);
        assertEquals(firstList.indexOfX(3.5), 4, ACCURACY);
        assertEquals(firstList.indexOfX(7), 7, ACCURACY);
    }

    @Test
    public void testInsertWithInitializeLinkedListThroughMathFunction() {
        LinkedListTabulatedFunction secondList = initializeLinkedListThroughMathFunction();
        secondList.insert(-2, 5);
        secondList.insert(1, 10);
        secondList.insert(3.5, 5);
        secondList.insert(7, 5);
        assertEquals(secondList.getY(0), 5, ACCURACY);
        assertEquals(secondList.getY(2), 10, ACCURACY);
        assertEquals(secondList.indexOfX(3.5), 5, ACCURACY);
        //assertEquals(secondList.indexOfX(7), 7, ACCURACY);
    }

    @Test
    public void testApply() {
        LinkedListTabulatedFunction firstList = initializeLinkedListThroughTwoArrays();
        LinkedListTabulatedFunction secondList = initializeLinkedListThroughMathFunction();
        for (int i = -5; i < xValues.length + 5; i++) {
            assertEquals(firstList.apply((i < 0 || i >= xValues.length) ? i : firstList.getX(i)), (i < 0 || i >= xValues.length) ? i * 2 : yValues[i], ACCURACY);
        }
        for (int i = 0; i < secondList.getCount(); i++) {
            assertEquals(secondList.apply(secondList.getX(i)), i * i, ACCURACY);
        }
        assertEquals(firstList.apply(1.5), 3);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testRemove() {
        LinkedListTabulatedFunction secondList = initializeLinkedListThroughMathFunction();
        secondList.remove(secondList.getCount() - 1);
        assertEquals(secondList.rightBound(), 10, ACCURACY);
        secondList.remove(-1);
    }

    @Test(expectedExceptions = NoSuchElementException.class)
    public void testIterator() {
        LinkedListTabulatedFunction firstArray = initializeLinkedListThroughTwoArrays();
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
        iterator.next();
    }

}