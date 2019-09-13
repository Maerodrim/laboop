package ru.ssau.tk.sergunin.lab.functions;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class LinkedListTabulatedFunctionTest {
    MathFunction FuncF = new SqrFunction();
    double[] xValues = new double[]{1., 2., 3., 4., 5.};
    double[] yValues = new double[]{2., 4., 6., 8., 10.};
    LinkedListTabulatedFunction ListOne = new LinkedListTabulatedFunction(xValues, yValues);
    LinkedListTabulatedFunction ListTwo = new LinkedListTabulatedFunction(FuncF, 10, 0, 1000);

    @Test
    public void testAddNode() {
        ListOne.addNode(6,12);
        assertEquals(ListOne.rightBound(), 12, 0.00001);
    }

    @Test
    public void testGetCount() {
        assertEquals(ListOne.getCount(), 5, 0.00001);
        assertEquals(ListTwo.getCount(), 999, 0.00001);
    }

    @Test
    public void testLeftBound() {
        assertEquals(ListOne.leftBound(), 2, 0.00001);
        assertEquals(ListTwo.leftBound(), 0, 0.00001);
    }

    @Test
    public void testRightBound() {
        assertEquals(ListOne.rightBound(), 12, 0.00001);
        assertEquals(ListTwo.rightBound(), 100, 0.00001);
    }


    @Test
    public void testGetX() {
        assertEquals(ListOne.getX(0),1, 0.00001);
        assertEquals(ListTwo.getX(0), 0, 0.00001);
    }

    @Test
    public void testGetY() {
        assertEquals(ListOne.getY(0), 2, 0.00001);
        assertEquals(ListTwo.getY(0), 0, 0.00001);
    }

    @Test
    public void testSetY() {
        ListOne.setY(4,13);
        assertEquals(ListOne.getY(5), 13, 0.00001);

    }

    @Test
    public void testIndexOfX() {
        assertEquals(ListOne.indexOfX(5),4, 0.00001);
        assertEquals(ListTwo.indexOfX(0), 0, 0.00001);
    }

    @Test
    public void testIndexOfY() {
        assertEquals(ListOne.indexOfY(13),-1, 0.00001);
        assertEquals(ListTwo.indexOfY(0), 0, 0.00001);
    }

    @Test
    public void testFloorIndexOfX() {
        assertEquals(ListOne.floorIndexOfX(9), 5, 0.00001);
        assertEquals(ListTwo.floorIndexOfX(11), 999, 0.00001);
    }

    @Test
    public void testExtrapolateLeft() {
        assertEquals(ListOne.extrapolateLeft(0), 0, 0.00001);
        assertEquals(ListTwo.extrapolateLeft(0), 0, 0.00001);
    }

    @Test
    public void testExtrapolateRight() {
        assertEquals(ListOne.extrapolateRight(0), 0, 0.00001);
        assertEquals(ListTwo.extrapolateRight(0), 0, 0.00001);
    }

    @Test
    public void testInterpolate() {
        assertEquals(ListOne.interpolate(0,0), 0, 0.00001);
        assertEquals(ListTwo.interpolate(0,0), 0, 0.00001);
    }
}