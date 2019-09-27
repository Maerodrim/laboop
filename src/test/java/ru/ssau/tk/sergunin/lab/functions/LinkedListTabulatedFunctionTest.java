package ru.ssau.tk.sergunin.lab.functions;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class LinkedListTabulatedFunctionTest {
    private final double[] xValues = new double[]{1., 2., 3., 4., 5.};
    private final double[] yValues = new double[]{2., 4., 6., 8., 10.};
    private final double ACCURACY = 0.00001;
    private MathFunction funcF = new SqrFunction();

    private LinkedListTabulatedFunction initializeListThroughTwoArrays() {
        return new LinkedListTabulatedFunction(xValues, yValues);
    }

    private LinkedListTabulatedFunction initializeListThroughMathFunction() {
        return new LinkedListTabulatedFunction(funcF, 10, 0, 11);
    }

    @Test
    public void testConstructor() {
        LinkedListTabulatedFunction firstList = initializeListThroughTwoArrays();
        for (int i = 0; i < 5; i++) {
            assertEquals(firstList.getY(i), 2 * (i + 1), ACCURACY);
        }
        LinkedListTabulatedFunction secondList = initializeListThroughMathFunction();
        for (int i = 0; i < 11; i++) {
            assertEquals(secondList.getY(i), i * i, ACCURACY);
        }
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionInListInitializeThroughTwoArrays() {
        LinkedListTabulatedFunction list = new LinkedListTabulatedFunction(new double[]{1}, new double[]{2});
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionInListInitializeThroughMathFunction() {
        LinkedListTabulatedFunction list = new LinkedListTabulatedFunction(funcF, 0, 0, 1);
    }

    @Test
    public void testAddNode() {
        LinkedListTabulatedFunction firstList = initializeListThroughTwoArrays();
        firstList.addNode(6, 12);
        assertEquals(firstList.rightBound(), 6, ACCURACY);
    }

    @Test
    public void testGetCount() {
        LinkedListTabulatedFunction firstList = initializeListThroughTwoArrays();
        LinkedListTabulatedFunction secondList = initializeListThroughMathFunction();
        assertEquals(firstList.getCount(), 5, ACCURACY);
        assertEquals(secondList.getCount(), 11, ACCURACY);
    }

    @Test
    public void testLeftBound() {
        LinkedListTabulatedFunction firstList = initializeListThroughTwoArrays();
        LinkedListTabulatedFunction secondList = initializeListThroughMathFunction();
        assertEquals(firstList.leftBound(), 1, ACCURACY);
        assertEquals(secondList.leftBound(), 0, ACCURACY);
    }

    @Test
    public void testRightBound() {
        LinkedListTabulatedFunction firstList = initializeListThroughTwoArrays();
        LinkedListTabulatedFunction secondList = initializeListThroughMathFunction();
        assertEquals(firstList.rightBound(), 5, ACCURACY);
        assertEquals(secondList.rightBound(), 10, ACCURACY);
    }


    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testGetX() {
        LinkedListTabulatedFunction firstList = initializeListThroughTwoArrays();
        LinkedListTabulatedFunction secondList = initializeListThroughMathFunction();
        assertEquals(firstList.getX(0), 1, ACCURACY);
        assertEquals(secondList.getX(0), 0, ACCURACY);
        assertEquals(secondList.getX(-1), 0, ACCURACY);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testGetY() {
        LinkedListTabulatedFunction firstList = initializeListThroughTwoArrays();
        LinkedListTabulatedFunction secondList = initializeListThroughMathFunction();
        assertEquals(firstList.getY(0), 2, ACCURACY);
        assertEquals(secondList.getY(0), 0, ACCURACY);
        assertEquals(secondList.getY(-1), 0, ACCURACY);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testSetY() {
        LinkedListTabulatedFunction firstList = initializeListThroughTwoArrays();
        LinkedListTabulatedFunction secondList = initializeListThroughMathFunction();
        firstList.setY(4, 13);
        assertEquals(firstList.getY(4), 13, ACCURACY);
        secondList.setY(7, 25);
        assertEquals(secondList.getY(7), 25, ACCURACY);
        secondList.setY(-1, 0);
    }

    @Test
    public void testIndexOfX() {
        LinkedListTabulatedFunction firstList = initializeListThroughTwoArrays();
        LinkedListTabulatedFunction secondList = initializeListThroughMathFunction();
        assertEquals(firstList.indexOfX(5), 4, ACCURACY);
        assertEquals(secondList.indexOfX(0), 0, ACCURACY);
    }

    @Test
    public void testIndexOfY() {
        LinkedListTabulatedFunction firstList = initializeListThroughTwoArrays();
        LinkedListTabulatedFunction secondList = initializeListThroughMathFunction();
        assertEquals(firstList.indexOfY(13), -1, ACCURACY);
        assertEquals(secondList.indexOfY(0), 0, ACCURACY);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testFloorIndexOfX() {
        LinkedListTabulatedFunction firstList = initializeListThroughTwoArrays();
        LinkedListTabulatedFunction secondList = initializeListThroughMathFunction();
        assertEquals(firstList.floorIndexOfX(3.5), 2, ACCURACY);
        assertEquals(firstList.floorIndexOfX(6), 5, ACCURACY);
        assertEquals(secondList.floorIndexOfX(5.5), 5, ACCURACY);
        assertEquals(secondList.floorIndexOfX(11), 11, ACCURACY);
        assertEquals(firstList.floorIndexOfX(0), 0, ACCURACY);
    }

    @Test
    public void testExtrapolateLeft() {
        LinkedListTabulatedFunction firstList = initializeListThroughTwoArrays();
        LinkedListTabulatedFunction secondList = initializeListThroughMathFunction();
        assertEquals(firstList.extrapolateLeft(0), 0, ACCURACY);
        assertEquals(secondList.extrapolateLeft(0), 0, ACCURACY);
    }

    @Test
    public void testExtrapolateRight() {
        LinkedListTabulatedFunction firstList = initializeListThroughTwoArrays();
        LinkedListTabulatedFunction secondList = initializeListThroughMathFunction();
        assertEquals(firstList.extrapolateRight(6), 12, ACCURACY);
        assertEquals(secondList.extrapolateRight(11), 119, ACCURACY);
    }

    @Test
    public void testInterpolate() {
        LinkedListTabulatedFunction firstList = initializeListThroughTwoArrays();
        LinkedListTabulatedFunction secondList = initializeListThroughMathFunction();
        assertEquals(firstList.interpolate(2.5, 2), 5, ACCURACY);
        assertEquals(secondList.interpolate(1.5, 1), 2.5, ACCURACY);
    }

    @Test
    public void testInsert() {
        LinkedListTabulatedFunction firstList = initializeListThroughTwoArrays();
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
    public void testApply() {
        LinkedListTabulatedFunction firstList = initializeListThroughTwoArrays();
        LinkedListTabulatedFunction secondList = initializeListThroughMathFunction();
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
        LinkedListTabulatedFunction secondList = initializeListThroughMathFunction();
        secondList.remove(secondList.getCount() - 1);
        assertEquals(secondList.rightBound(), 10, ACCURACY);
        secondList.remove(-1);
    }

}