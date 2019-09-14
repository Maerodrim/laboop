package ru.ssau.tk.sergunin.lab.functions;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class ArrayTabulatedFunctionTest {
    private MathFunction FuncF = new SqrFunction();
    private double[] xValues = new double[]{1., 2., 3., 4., 5.};
    private double[] yValues = new double[]{2., 4., 6., 8., 10.};
    private ArrayTabulatedFunction FirstArray = new ArrayTabulatedFunction(xValues, yValues);
    private ArrayTabulatedFunction SecondArray = new ArrayTabulatedFunction(FuncF, 10, 0, 11);
    private final double accuracy = 0.00001;

    // TODO

    @Test
    public void testGetCount() {
        assertEquals(FirstArray.getCount(), 5, accuracy);
        assertEquals(SecondArray.getCount(), 11, accuracy);
    }
    @Test
    public void testLeftBound() {
        assertEquals(FirstArray.leftBound(), -2, accuracy);
        assertEquals(SecondArray.leftBound(), 0, accuracy);
    }

    @Test
    public void testRightBound() {
        assertEquals(FirstArray.rightBound(), 5, accuracy);
        assertEquals(SecondArray.rightBound(), 10, accuracy);
    }


    @Test
    public void testGetX() {
        assertEquals(FirstArray.getX(0), 1, accuracy);
        assertEquals(SecondArray.getX(0), 0, accuracy);
    }

    @Test
    public void testGetY() {
        assertEquals(FirstArray.getY(0), 2, accuracy);
        assertEquals(SecondArray.getY(0), 0, accuracy);
    }

    @Test
    public void testSetY() {
        FirstArray.setY(4, 13);
        assertEquals(FirstArray.getY(4), 13, accuracy);
        SecondArray.setY(7, 25);
        assertEquals(SecondArray.getY(7), 25, accuracy);
    }

    @Test
    public void testIndexOfX() {
        assertEquals(FirstArray.indexOfX(5), 4, accuracy);
        assertEquals(SecondArray.indexOfX(0), 0, accuracy);
    }

    @Test
    public void testIndexOfY() {
        assertEquals(FirstArray.indexOfY(13), -1, accuracy);
        assertEquals(SecondArray.indexOfY(0), 0, accuracy);
    }

    @Test
    public void testFloorIndexOfX() {
        assertEquals(FirstArray.floorIndexOfX(0), 0, accuracy);
        assertEquals(FirstArray.floorIndexOfX(3.5), 2, accuracy);
        assertEquals(FirstArray.floorIndexOfX(6), 5, accuracy);
        assertEquals(SecondArray.floorIndexOfX(-1), 0, accuracy);
        assertEquals(SecondArray.floorIndexOfX(5.5), 5, accuracy);
        assertEquals(SecondArray.floorIndexOfX(11), 11, accuracy);
    }

    @Test
    public void testExtrapolateLeft() {
        assertEquals(FirstArray.extrapolateLeft(0), 0, accuracy);
        assertEquals(SecondArray.extrapolateLeft(-1), -1, accuracy);
    }

    @Test
    public void testExtrapolateRight() {
        assertEquals(FirstArray.extrapolateRight(6), 12, accuracy);
        assertEquals(SecondArray.extrapolateRight(11), 119, accuracy);
    }

    @Test
    public void testInterpolate() {
        assertEquals(FirstArray.interpolate(2.5, 2), 5, accuracy);
        assertEquals(SecondArray.interpolate(1.5, 1), 2.5, accuracy);
    }

    @Test
    public void testInsert() {
        FirstArray.insert(-2, 5);
        assertEquals(FirstArray.getY(0), 5, accuracy);
        FirstArray.insert(1, 10);
        assertEquals(FirstArray.getY(1), 10, accuracy);
        FirstArray.insert(4.5, 5);
        assertEquals(FirstArray.indexOfX(4.5), 4, accuracy);
        FirstArray.insert(7, 5);
        assertEquals(FirstArray.indexOfX(7), 7, accuracy);
    }

    @Test
    public void testApply() {
        assertEquals(FirstArray.apply(1), 2, accuracy);
    }

}