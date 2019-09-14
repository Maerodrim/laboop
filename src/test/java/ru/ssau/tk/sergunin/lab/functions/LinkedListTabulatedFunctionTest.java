package ru.ssau.tk.sergunin.lab.functions;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class LinkedListTabulatedFunctionTest {
    private MathFunction FuncF = new SqrFunction();
    private double[] xValues = new double[]{1., 2., 3., 4., 5.};
    private double[] yValues = new double[]{2., 4., 6., 8., 10.};
    private LinkedListTabulatedFunction FirstList = new LinkedListTabulatedFunction(xValues, yValues);
    private LinkedListTabulatedFunction SecondList = new LinkedListTabulatedFunction(FuncF, 10, 0, 11);
    private final double accuracy = 0.00001;

    @Test
    public void testAddNode() {
        FirstList.addNode(6, 12);
        assertEquals(FirstList.rightBound(), 6, accuracy);
    }

    @Test
    public void testGetCount() {
        assertEquals(FirstList.getCount(), 5, accuracy);
        assertEquals(SecondList.getCount(), 11, accuracy);
    }

    @Test
    public void testLeftBound() {
        assertEquals(FirstList.leftBound(), -2, accuracy);
        assertEquals(SecondList.leftBound(), 0, accuracy);
    }

    @Test
    public void testRightBound() {
        assertEquals(FirstList.rightBound(), 7, accuracy);
        assertEquals(SecondList.rightBound(), 10, accuracy);
    }


    @Test
    public void testGetX() {
        assertEquals(FirstList.getX(0), 1, accuracy);
        assertEquals(SecondList.getX(0), 0, accuracy);
    }

    @Test
    public void testGetY() {
        assertEquals(FirstList.getY(0), 2, accuracy);
        assertEquals(SecondList.getY(0), 0, accuracy);
    }

    @Test
    public void testSetY() {
        FirstList.setY(4, 13);
        assertEquals(FirstList.getY(4), 13, accuracy);
        SecondList.setY(7, 25);
        assertEquals(SecondList.getY(7), 25, accuracy);
    }

    @Test
    public void testIndexOfX() {
        assertEquals(FirstList.indexOfX(5), 4, accuracy);
        assertEquals(SecondList.indexOfX(0), 0, accuracy);
    }

    @Test
    public void testIndexOfY() {
        assertEquals(FirstList.indexOfY(13), -1, accuracy);
        assertEquals(SecondList.indexOfY(0), 0, accuracy);
    }

    @Test
    public void testFloorIndexOfX() {
        assertEquals(FirstList.floorIndexOfX(0), 0, accuracy);
        assertEquals(FirstList.floorIndexOfX(3.5), 2, accuracy);
        assertEquals(FirstList.floorIndexOfX(6), 5, accuracy);
        assertEquals(SecondList.floorIndexOfX(-1), 0, accuracy);
        assertEquals(SecondList.floorIndexOfX(5.5), 5, accuracy);
        assertEquals(SecondList.floorIndexOfX(11), 11, accuracy);
    }

    @Test
    public void testExtrapolateLeft() {
        assertEquals(FirstList.extrapolateLeft(0), 0, accuracy);
        assertEquals(SecondList.extrapolateLeft(-1), -1, accuracy);
    }

    @Test
    public void testExtrapolateRight() {
        assertEquals(FirstList.extrapolateRight(6), 12, accuracy);
        assertEquals(SecondList.extrapolateRight(11), 119, accuracy);
    }

    @Test
    public void testInterpolate() {
        assertEquals(FirstList.interpolate(2.5, 2), 5, accuracy);
        assertEquals(SecondList.interpolate(1.5, 1), 2.5, accuracy);
    }

    @Test
    public void testInsert() {
        FirstList.insert(-2, 5);
        FirstList.insert(1, 10);
        FirstList.insert(3.5, 5);
        FirstList.insert(7, 5);
        assertEquals(FirstList.getY(0), 5, accuracy);
        assertEquals(FirstList.getY(1), 10, accuracy);
        assertEquals(FirstList.indexOfX(3.5), 4, accuracy);
        assertEquals(FirstList.indexOfX(7), 7, accuracy);
    }

    @Test
    public void testApply() {
        assertEquals(FirstList.apply(1), 2, accuracy);
    }

    @Test
    public void testRemove() {
        SecondList.remove(SecondList.getCount()-1);
        assertEquals(SecondList.rightBound(), 10, accuracy);
    }
}