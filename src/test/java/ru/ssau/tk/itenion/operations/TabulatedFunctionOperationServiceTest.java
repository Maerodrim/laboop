package ru.ssau.tk.itenion.operations;

import org.testng.annotations.Test;
import ru.ssau.tk.itenion.exceptions.InconsistentFunctionsException;
import ru.ssau.tk.itenion.functions.Point;
import ru.ssau.tk.itenion.functions.factory.ArrayTabulatedFunctionFactory;
import ru.ssau.tk.itenion.functions.factory.LinkedListTabulatedFunctionFactory;
import ru.ssau.tk.itenion.functions.factory.TabulatedFunctionFactory;
import ru.ssau.tk.itenion.functions.powerFunctions.PowFunction;
import ru.ssau.tk.itenion.functions.tabulatedFunctions.ArrayTabulatedFunction;
import ru.ssau.tk.itenion.functions.tabulatedFunctions.TabulatedFunction;

import static org.testng.Assert.*;

public class TabulatedFunctionOperationServiceTest {

    double[] firstXValues = new double[]{1, 2, 3};
    double[] firstYValues = new double[]{2, 4, 6};
    double[] secondYValues = new double[]{5, 7, 9};
    double[] secondXValues = new double[]{1, 2, 5};
    double[] thirdXValues = new double[]{1, 2, 3, 4};
    double[] thirdYValues = new double[]{5, 7, 9, 11};

    @Test
    public void testAsPoints() {
        TabulatedFunction list = (new ArrayTabulatedFunctionFactory()).create(firstXValues, firstYValues);
        Point[] points = TabulatedFunctionOperationService.asPoints(list);
        int i = 0;
        for (Point point : points) {
            assertEquals(point.x, list.getX(i));
            assertEquals(point.y, list.getY(i++));
        }
    }

    @Test
    public void testGetFactory() {
        TabulatedFunctionOperationService operationService = new TabulatedFunctionOperationService(new ArrayTabulatedFunctionFactory());
        assertTrue(operationService.getFactory() instanceof ArrayTabulatedFunctionFactory);
        operationService = new TabulatedFunctionOperationService(new LinkedListTabulatedFunctionFactory());
        assertTrue(operationService.getFactory() instanceof LinkedListTabulatedFunctionFactory);
    }

    @Test
    public void testSetFactory() {
        TabulatedFunctionOperationService operationService = new TabulatedFunctionOperationService();
        operationService.setFactory(new ArrayTabulatedFunctionFactory());
        assertTrue(operationService.getFactory() instanceof ArrayTabulatedFunctionFactory);
        operationService.setFactory(new LinkedListTabulatedFunctionFactory());
        assertTrue(operationService.getFactory() instanceof LinkedListTabulatedFunctionFactory);
    }

    @Test
    public void testSum() {
        TabulatedFunctionFactory arrayFactory = new ArrayTabulatedFunctionFactory();
        TabulatedFunctionFactory linkedListFactory = new LinkedListTabulatedFunctionFactory();
        TabulatedFunctionOperationService operationServiceThroughArray = new TabulatedFunctionOperationService(arrayFactory);
        TabulatedFunctionOperationService operationServiceThroughLinkedList = new TabulatedFunctionOperationService(linkedListFactory);
        TabulatedFunction a = arrayFactory.create(firstXValues, firstYValues);
        TabulatedFunction b = linkedListFactory.create(firstXValues, secondYValues);
        TabulatedFunction resultSumThroughArray = operationServiceThroughArray.sum(a, b);
        TabulatedFunction resultSumThroughLinkedList = operationServiceThroughLinkedList.sum(a, b);
        int i = 0;
        for (Point point : resultSumThroughArray) {
            assertEquals(point.x, firstXValues[i]);
            assertEquals(point.y, firstYValues[i] + secondYValues[i++]);
        }
        i = 0;
        for (Point point : resultSumThroughLinkedList) {
            assertEquals(point.x, firstXValues[i]);
            assertEquals(point.y, firstYValues[i] + secondYValues[i++]);
        }
        assertThrows(InconsistentFunctionsException.class, () -> operationServiceThroughLinkedList.sum(a, arrayFactory.create(secondXValues, secondYValues)));
    }

    @Test
    public void testSubtract() {
        TabulatedFunctionFactory arrayFactory = new ArrayTabulatedFunctionFactory();
        TabulatedFunctionFactory linkedListFactory = new LinkedListTabulatedFunctionFactory();
        TabulatedFunctionOperationService operationServiceThroughArray = new TabulatedFunctionOperationService(arrayFactory);
        TabulatedFunctionOperationService operationServiceThroughLinkedList = new TabulatedFunctionOperationService(linkedListFactory);
        TabulatedFunction a = arrayFactory.create(firstXValues, firstYValues);
        TabulatedFunction b = linkedListFactory.create(firstXValues, secondYValues);
        TabulatedFunction resultSubtractThroughArray = operationServiceThroughArray.subtract(a, b);
        TabulatedFunction resultSubtractThroughLinkedList = operationServiceThroughLinkedList.subtract(a, b);
        int i = 0;
        for (Point point : resultSubtractThroughArray) {
            assertEquals(point.x, firstXValues[i]);
            assertEquals(point.y, firstYValues[i] - secondYValues[i++]);
        }
        i = 0;
        for (Point point : resultSubtractThroughLinkedList) {
            assertEquals(point.x, firstXValues[i]);
            assertEquals(point.y, firstYValues[i] - secondYValues[i++]);
        }
        assertThrows(InconsistentFunctionsException.class, () -> operationServiceThroughLinkedList.subtract(a, arrayFactory.create(thirdXValues, thirdYValues)));
    }

    @Test
    public void testMultiplication() {
        TabulatedFunctionFactory arrayFactory = new ArrayTabulatedFunctionFactory();
        TabulatedFunctionFactory linkedListFactory = new LinkedListTabulatedFunctionFactory();
        TabulatedFunctionOperationService operationServiceThroughArray = new TabulatedFunctionOperationService(arrayFactory);
        TabulatedFunctionOperationService operationServiceThroughLinkedList = new TabulatedFunctionOperationService(linkedListFactory);
        TabulatedFunction a = arrayFactory.create(firstXValues, firstYValues);
        TabulatedFunction b = linkedListFactory.create(firstXValues, secondYValues);
        TabulatedFunction resultMultiplicationThroughArray = operationServiceThroughArray.multiplication(a, b);
        TabulatedFunction resultMultiplicationThroughLinkedList = operationServiceThroughLinkedList.multiplication(a, b);
        int i = 0;
        for (Point point : resultMultiplicationThroughArray) {
            assertEquals(point.x, firstXValues[i]);
            assertEquals(point.y, firstYValues[i] * secondYValues[i++]);
        }
        i = 0;
        for (Point point : resultMultiplicationThroughLinkedList) {
            assertEquals(point.x, firstXValues[i]);
            assertEquals(point.y, firstYValues[i] * secondYValues[i++]);
        }
        assertThrows(InconsistentFunctionsException.class, () -> operationServiceThroughLinkedList.multiplication(a, arrayFactory.create(thirdXValues, thirdYValues)));
    }

    @Test
    public void testDivision() {
        TabulatedFunctionFactory arrayFactory = new ArrayTabulatedFunctionFactory();
        TabulatedFunctionFactory linkedListFactory = new LinkedListTabulatedFunctionFactory();
        TabulatedFunctionOperationService operationServiceThroughArray = new TabulatedFunctionOperationService(arrayFactory);
        TabulatedFunctionOperationService operationServiceThroughLinkedList = new TabulatedFunctionOperationService(linkedListFactory);
        TabulatedFunction a = arrayFactory.create(firstXValues, firstYValues);
        TabulatedFunction b = linkedListFactory.create(firstXValues, secondYValues);
        TabulatedFunction resultDivisionThroughArray = operationServiceThroughArray.division(a, b);
        TabulatedFunction resultDivisionThroughLinkedList = operationServiceThroughLinkedList.division(a, b);
        int i = 0;
        for (Point point : resultDivisionThroughArray) {
            assertEquals(point.x, firstXValues[i]);
            assertEquals(point.y, firstYValues[i] / secondYValues[i++]);
        }
        i = 0;
        for (Point point : resultDivisionThroughLinkedList) {
            assertEquals(point.x, firstXValues[i]);
            assertEquals(point.y, firstYValues[i] / secondYValues[i++]);
        }
        assertThrows(InconsistentFunctionsException.class, () -> operationServiceThroughLinkedList.division(a, arrayFactory.create(thirdXValues, thirdYValues)));
    }

    @Test
    public void testOverrideAsPoints() {
        TabulatedFunction actual = new ArrayTabulatedFunction(new PowFunction(2), 1, 5, 5000001);
        double[][][] points = TabulatedFunctionOperationService.asPoints(actual, 10);
        int n = 5;
    }

}