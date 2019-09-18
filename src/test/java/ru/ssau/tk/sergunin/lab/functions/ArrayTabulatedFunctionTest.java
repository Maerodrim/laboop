package ru.ssau.tk.sergunin.lab.functions;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class ArrayTabulatedFunctionTest {
    private final double accuracy = 0.00001;
    private MathFunction funcF = new SqrFunction();
    private double[] xValues = new double[]{1., 2., 3., 4., 5.};
    private double[] yValues = new double[]{2., 4., 6., 8., 10.};
    private ArrayTabulatedFunction firstArray = new ArrayTabulatedFunction(xValues, yValues);
    private ArrayTabulatedFunction secondArray = new ArrayTabulatedFunction(funcF, 10, 0, 11);

    @Test
    public void testGetCount() {
        assertEquals(firstArray.getCount(), 5, accuracy);
        assertEquals(secondArray.getCount(), 11, accuracy);
    }

    @Test
    public void testLeftBound() {
        assertEquals(firstArray.leftBound(), -2, accuracy);
        assertEquals(secondArray.leftBound(), 0, accuracy);
    }

    @Test
    public void testRightBound() {
        assertEquals(firstArray.rightBound(), 7, accuracy);
        assertEquals(secondArray.rightBound(), 10, accuracy);
    }


    @Test
    public void testGetX() {
        assertEquals(firstArray.getX(0), 1, accuracy);
        assertEquals(secondArray.getX(0), 0, accuracy);
    }

    @Test
    public void testGetY() {
        assertEquals(firstArray.getY(0), 2, accuracy);
        assertEquals(secondArray.getY(0), 0, accuracy);
    }

    @Test
    public void testSetY() {
        firstArray.setY(4, 13);
        assertEquals(firstArray.getY(4), 13, accuracy);
        secondArray.setY(7, 25);
        assertEquals(secondArray.getY(7), 25, accuracy);
    }

    @Test
    public void testIndexOfX() {
        assertEquals(firstArray.indexOfX(5), 4, accuracy);
        assertEquals(secondArray.indexOfX(0), 0, accuracy);
    }

    @Test
    public void testIndexOfY() {
        assertEquals(firstArray.indexOfY(13), -1, accuracy);
        assertEquals(secondArray.indexOfY(0), 0, accuracy);
    }

    @Test
    public void testFloorIndexOfX() {
        assertEquals(firstArray.floorIndexOfX(0), 0, accuracy);
        assertEquals(firstArray.floorIndexOfX(3.5), 2, accuracy);
        assertEquals(firstArray.floorIndexOfX(6), 5, accuracy);
        assertEquals(secondArray.floorIndexOfX(-1), 0, accuracy);
        assertEquals(secondArray.floorIndexOfX(5.5), 5, accuracy);
        assertEquals(secondArray.floorIndexOfX(11), 11, accuracy);
    }

    @Test
    public void testExtrapolateLeft() {
        assertEquals(firstArray.extrapolateLeft(0), 0, accuracy);
        assertEquals(secondArray.extrapolateLeft(-1), -1, accuracy);
    }

    @Test
    public void testExtrapolateRight() {
        assertEquals(firstArray.extrapolateRight(6), 12, accuracy);
        assertEquals(secondArray.extrapolateRight(11), 119, accuracy);
    }

    @Test
    public void testInterpolate() {
        assertEquals(firstArray.interpolate(2.5, 2), 5, accuracy);
        assertEquals(secondArray.interpolate(1.5, 1), 2.5, accuracy);
    }

    @Test
    public void testInsert() {
        firstArray.insert(-2, 5);
        assertEquals(firstArray.getY(0), 5, accuracy);
        firstArray.insert(1, 10);
        assertEquals(firstArray.getY(1), 10, accuracy);
        firstArray.insert(4.5, 5);
        assertEquals(firstArray.indexOfX(4.5), 4, accuracy);
        firstArray.insert(7, 5);
        assertEquals(firstArray.indexOfX(7), 7, accuracy);
    }

    @Test
    public void testApply() {
        assertEquals(firstArray.apply(1), 2, accuracy);
    }

    @Test
    public void testRemove() {
        secondArray.remove(secondArray.getCount() - 3);
        assertEquals(secondArray.getX(8), 9, accuracy);
    }

}