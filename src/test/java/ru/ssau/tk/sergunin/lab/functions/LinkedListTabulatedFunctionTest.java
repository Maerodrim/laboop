package ru.ssau.tk.sergunin.lab.functions;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class LinkedListTabulatedFunctionTest {
    private MathFunction funcF = new SqrFunction();
    private double[] xValues = new double[]{1., 2., 3., 4., 5.};
    private double[] yValues = new double[]{2., 4., 6., 8., 10.};
    private LinkedListTabulatedFunction firstList = new LinkedListTabulatedFunction(xValues, yValues);
    private LinkedListTabulatedFunction secondList = new LinkedListTabulatedFunction(funcF, 10, 0, 11);
    private final double accuracy = 0.00001;

    @Test
    public void testAddNode() {
        firstList.addNode(6, 12);
        assertEquals(firstList.rightBound(), 6, accuracy);
    }

    @Test
    public void testGetCount() {
        assertEquals(firstList.getCount(), 5, accuracy);
        assertEquals(secondList.getCount(), 11, accuracy);
    }

    @Test
    public void testLeftBound() {
        assertEquals(firstList.leftBound(), -2, accuracy);
        assertEquals(secondList.leftBound(), 0, accuracy);
    }

    @Test
    public void testRightBound() {
        assertEquals(firstList.rightBound(), 7, accuracy);
        assertEquals(secondList.rightBound(), 10, accuracy);
    }


    @Test
    public void testGetX() {
        assertEquals(firstList.getX(0), 1, accuracy);
        assertEquals(secondList.getX(0), 0, accuracy);
    }

    @Test
    public void testGetY() {
        assertEquals(firstList.getY(0), 2, accuracy);
        assertEquals(secondList.getY(0), 0, accuracy);
    }

    @Test
    public void testSetY() {
        firstList.setY(4, 13);
        assertEquals(firstList.getY(4), 13, accuracy);
        secondList.setY(7, 25);
        assertEquals(secondList.getY(7), 25, accuracy);
    }

    @Test
    public void testIndexOfX() {
        assertEquals(firstList.indexOfX(5), 4, accuracy);
        assertEquals(secondList.indexOfX(0), 0, accuracy);
    }

    @Test
    public void testIndexOfY() {
        assertEquals(firstList.indexOfY(13), -1, accuracy);
        assertEquals(secondList.indexOfY(0), 0, accuracy);
    }

    @Test
    public void testFloorIndexOfX() {
        assertEquals(firstList.floorIndexOfX(0), 0, accuracy);
        assertEquals(firstList.floorIndexOfX(3.5), 2, accuracy);
        assertEquals(firstList.floorIndexOfX(6), 5, accuracy);
        assertEquals(secondList.floorIndexOfX(-1), 0, accuracy);
        assertEquals(secondList.floorIndexOfX(5.5), 5, accuracy);
        assertEquals(secondList.floorIndexOfX(11), 11, accuracy);
    }

    @Test
    public void testExtrapolateLeft() {
        assertEquals(firstList.extrapolateLeft(0), 0, accuracy);
        assertEquals(secondList.extrapolateLeft(-1), -1, accuracy);
    }

    @Test
    public void testExtrapolateRight() {
        assertEquals(firstList.extrapolateRight(6), 12, accuracy);
        assertEquals(secondList.extrapolateRight(11), 119, accuracy);
    }

    @Test
    public void testInterpolate() {
        assertEquals(firstList.interpolate(2.5, 2), 5, accuracy);
        assertEquals(secondList.interpolate(1.5, 1), 2.5, accuracy);
    }

    @Test
    public void testInsert() {
        firstList.insert(-2, 5);
        firstList.insert(1, 10);
        firstList.insert(3.5, 5);
        firstList.insert(7, 5);
        assertEquals(firstList.getY(0), 5, accuracy);
        assertEquals(firstList.getY(1), 10, accuracy);
        assertEquals(firstList.indexOfX(3.5), 4, accuracy);
        assertEquals(firstList.indexOfX(7), 7, accuracy);
    }

    @Test
    public void testApply() {
        assertEquals(firstList.apply(1), 2, accuracy);
    }

    @Test
    public void testRemove() {
        secondList.remove(secondList.getCount() - 1);
        assertEquals(secondList.rightBound(), 10, accuracy);
    }
}