package ru.ssau.tk.sergunin.lab.functions;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class ArrayTabulatedFunctionTest {
    private MathFunction funcF = new SqrFunction();
    private final double[] xValues = new double[]{1., 2., 3., 4., 5.};
    private final double[] yValues = new double[]{2., 4., 6., 8., 10.};
    private final double accuracy = 0.00001;

    private  ArrayTabulatedFunction initializeArrayThroughTwoArrays() {
        return new  ArrayTabulatedFunction(xValues, yValues);
    }

    private  ArrayTabulatedFunction initializeArrayThroughMathFunction() {
        return new  ArrayTabulatedFunction(funcF, 10, 0, 11);
    }

    private  ArrayTabulatedFunction initializeArrayWithOneElement() {
        return new  ArrayTabulatedFunction(funcF, 5, 5, 1);
    }
    @Test
    public void testConstrakt() {
        ArrayTabulatedFunction firstArray = initializeArrayThroughTwoArrays();
        for(int i=0;i<5;i++) {
            assertEquals(firstArray.getY(i), 2 * (i+1), accuracy);
        }
        ArrayTabulatedFunction secondArray = initializeArrayThroughMathFunction();
        for(int i=0;i<11;i++) {
            assertEquals(secondArray.getY(i), i * i, accuracy);
        }
        ArrayTabulatedFunction thirdArray = initializeArrayWithOneElement();
        for(int i=0;i<1;i++) {
            assertEquals(thirdArray.getY(i), 25, accuracy);
        }
    }

    @Test
    public void testGetCount() {
        ArrayTabulatedFunction firstArray = initializeArrayThroughTwoArrays();
        ArrayTabulatedFunction secondArray = initializeArrayThroughMathFunction();
        assertEquals(firstArray.getCount(), 5, accuracy);
        assertEquals(secondArray.getCount(), 11, accuracy);
    }

    @Test
    public void testLeftBound() {
        ArrayTabulatedFunction firstArray = initializeArrayThroughTwoArrays();
        ArrayTabulatedFunction secondArray = initializeArrayThroughMathFunction();
        assertEquals(firstArray.leftBound(), 1, accuracy);
        assertEquals(secondArray.leftBound(), 0, accuracy);
    }

    @Test
    public void testRightBound() {
        ArrayTabulatedFunction firstArray = initializeArrayThroughTwoArrays();
        ArrayTabulatedFunction secondArray = initializeArrayThroughMathFunction();
        assertEquals(firstArray.rightBound(), 5, accuracy);
        assertEquals(secondArray.rightBound(), 10, accuracy);
    }


    @Test
    public void testGetX() {
        ArrayTabulatedFunction firstArray = initializeArrayThroughTwoArrays();
        ArrayTabulatedFunction secondArray = initializeArrayThroughMathFunction();
        assertEquals(firstArray.getX(0), 1, accuracy);
        assertEquals(secondArray.getX(0), 0, accuracy);
    }

    @Test
    public void testGetY() {
        ArrayTabulatedFunction firstArray = initializeArrayThroughTwoArrays();
        ArrayTabulatedFunction secondArray = initializeArrayThroughMathFunction();
        assertEquals(firstArray.getY(0), 2, accuracy);
        assertEquals(secondArray.getY(0), 0, accuracy);
    }

    @Test
    public void testSetY() {
        ArrayTabulatedFunction firstArray = initializeArrayThroughTwoArrays();
        ArrayTabulatedFunction secondArray = initializeArrayThroughMathFunction();
        firstArray.setY(4, 13);
        assertEquals(firstArray.getY(4), 13, accuracy);
        secondArray.setY(7, 25);
        assertEquals(secondArray.getY(7), 25, accuracy);
    }

    @Test
    public void testIndexOfX() {
        ArrayTabulatedFunction firstArray = initializeArrayThroughTwoArrays();
        ArrayTabulatedFunction secondArray = initializeArrayThroughMathFunction();
        assertEquals(firstArray.indexOfX(5), 4, accuracy);
        assertEquals(secondArray.indexOfX(0), 0, accuracy);
    }

    @Test
    public void testIndexOfY() {
        ArrayTabulatedFunction firstArray = initializeArrayThroughTwoArrays();
        ArrayTabulatedFunction secondArray = initializeArrayThroughMathFunction();
        assertEquals(firstArray.indexOfY(13), -1, accuracy);
        assertEquals(secondArray.indexOfY(0), 0, accuracy);
    }

    @Test
    public void testFloorIndexOfX() {
        ArrayTabulatedFunction firstArray = initializeArrayThroughTwoArrays();
        ArrayTabulatedFunction secondArray = initializeArrayThroughMathFunction();
        assertEquals(firstArray.floorIndexOfX(0), 0, accuracy);
        assertEquals(firstArray.floorIndexOfX(3.5), 2, accuracy);
        assertEquals(firstArray.floorIndexOfX(6), 5, accuracy);
        assertEquals(secondArray.floorIndexOfX(-1), 0, accuracy);
        assertEquals(secondArray.floorIndexOfX(5.5), 5, accuracy);
        assertEquals(secondArray.floorIndexOfX(11), 11, accuracy);
    }

    @Test
    public void testExtrapolateLeft() {
        ArrayTabulatedFunction firstArray = initializeArrayThroughTwoArrays();
        ArrayTabulatedFunction secondArray = initializeArrayThroughMathFunction();
        ArrayTabulatedFunction thirdArray = initializeArrayWithOneElement();
        assertEquals(firstArray.extrapolateLeft(0), 0, accuracy);
        assertEquals(firstArray.extrapolateLeft(0), 0, accuracy);
        assertEquals(thirdArray.extrapolateLeft(4), 4, accuracy);
    }

    @Test
    public void testExtrapolateRight() {
        ArrayTabulatedFunction firstArray = initializeArrayThroughTwoArrays();
        ArrayTabulatedFunction secondArray = initializeArrayThroughMathFunction();
        ArrayTabulatedFunction thirdArray = initializeArrayWithOneElement();
        assertEquals(firstArray.extrapolateRight(6), 12, accuracy);
        assertEquals(secondArray.extrapolateRight(11), 119, accuracy);
        assertEquals(thirdArray.extrapolateRight(6), 6, accuracy);
    }

    @Test
    public void testInterpolate() {
        ArrayTabulatedFunction firstArray = initializeArrayThroughTwoArrays();
        ArrayTabulatedFunction secondArray = initializeArrayThroughMathFunction();
        assertEquals(firstArray.interpolate(2.5, 2), 5, accuracy);
        assertEquals(secondArray.interpolate(1.5, 1), 2.5, accuracy);
    }

    @Test
    public void testInsert() {
        ArrayTabulatedFunction firstArray = initializeArrayThroughTwoArrays();
        ArrayTabulatedFunction secondArray = initializeArrayThroughMathFunction();
        ArrayTabulatedFunction thirdArray = initializeArrayWithOneElement();
        firstArray.insert(-2, 5);
        firstArray.insert(1, 10);
        firstArray.insert(3.5, 5);
        firstArray.insert(7, 5);
        assertEquals(firstArray.getY(0), 5, accuracy);
        assertEquals(firstArray.getY(1), 10, accuracy);
        assertEquals(firstArray.indexOfX(3.5), 3, accuracy);
        assertEquals(firstArray.indexOfX(7), 7, accuracy);
        assertEquals(thirdArray.interpolate(5, 0), 5, accuracy);
    }

    @Test
    public void testApply() {
        ArrayTabulatedFunction firstArray = initializeArrayThroughTwoArrays();
        ArrayTabulatedFunction secondArray = initializeArrayThroughMathFunction();
        ArrayTabulatedFunction thirdArray = initializeArrayWithOneElement();
        for (int i = -5; i < xValues.length + 5; i++) {
            assertEquals(firstArray.apply((i < 0 || i >= xValues.length) ? i : firstArray.getX(i)), (i < 0 || i >= xValues.length) ? i * 2 : yValues[i], accuracy);
        }
        for (int i = 0; i < secondArray.getCount(); i++) {
            assertEquals(secondArray.apply(secondArray.getX(i)), i * i, accuracy);
        }
    }

    @Test
    public void testRemove() {
        ArrayTabulatedFunction secondArray = initializeArrayThroughMathFunction();
        secondArray.remove(secondArray.getCount() - 2);
        assertEquals(secondArray.rightBound(), 10, accuracy);
        secondArray.remove(0);
        assertEquals(secondArray.leftBound(), 1, accuracy);
        secondArray.remove(8);
        assertEquals(secondArray.getCount(), 8, accuracy);
    }

}