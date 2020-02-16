package ru.ssau.tk.sergunin.lab.operations;

import org.testng.annotations.Test;
import ru.ssau.tk.sergunin.lab.functions.Point;
import ru.ssau.tk.sergunin.lab.functions.tabulatedFunctions.TabulatedFunction;
import ru.ssau.tk.sergunin.lab.functions.factory.ArrayTabulatedFunctionFactory;
import ru.ssau.tk.sergunin.lab.functions.factory.LinkedListTabulatedFunctionFactory;
import ru.ssau.tk.sergunin.lab.functions.factory.TabulatedFunctionFactory;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class TabulatedDifferentialOperatorTest {

    private double[] firstXValues = new double[]{1, 2, 3};
    private double[] firstYValues = new double[]{2, 4, 6};

    @Test
    public void testDerive() {
        TabulatedFunctionFactory arrayFactory = new ArrayTabulatedFunctionFactory();
        TabulatedFunctionFactory linkedListFactory = new LinkedListTabulatedFunctionFactory();
        TabulatedFunction a = arrayFactory.create(firstXValues, firstYValues);
        TabulatedFunction b = linkedListFactory.create(firstXValues, firstYValues);
        TabulatedDifferentialOperator differentialOperator = new TabulatedDifferentialOperator();
        TabulatedDifferentialOperator differentialOperatorThroughLinkedList = new TabulatedDifferentialOperator(linkedListFactory);
        TabulatedFunction deriveAThroughArray = differentialOperator.derive(a);
        for (Point point : deriveAThroughArray) {
            assertEquals(point.y, 2);
        }
        TabulatedFunction deriveBThroughLinkedList = differentialOperatorThroughLinkedList.derive(b);
        for (Point point : deriveBThroughLinkedList) {
            assertEquals(point.y, 2);
        }
        differentialOperator.setFactory(linkedListFactory);
        deriveBThroughLinkedList = differentialOperator.derive(b);
        for (Point point : deriveBThroughLinkedList) {
            assertEquals(point.y, 2);
        }
        assertTrue(differentialOperator.getFactory() instanceof LinkedListTabulatedFunctionFactory);
    }

    @Test
    public void testDeriveSynchronously() {
        TabulatedFunctionFactory arrayFactory = new ArrayTabulatedFunctionFactory();
        TabulatedFunctionFactory linkedListFactory = new LinkedListTabulatedFunctionFactory();
        TabulatedFunction a = arrayFactory.create(firstXValues, firstYValues);
        TabulatedFunction b = linkedListFactory.create(firstXValues, firstYValues);
        TabulatedDifferentialOperator differentialOperator = new TabulatedDifferentialOperator();
        TabulatedDifferentialOperator differentialOperatorThroughLinkedList = new TabulatedDifferentialOperator(linkedListFactory);
        TabulatedFunction deriveAThroughArray = differentialOperator.deriveSynchronously(a);
        for (Point point : deriveAThroughArray) {
            assertEquals(point.y, 2);
        }
        TabulatedFunction deriveBThroughLinkedList = differentialOperatorThroughLinkedList.deriveSynchronously(b);
        for (Point point : deriveBThroughLinkedList) {
            assertEquals(point.y, 2);
        }
        differentialOperator.setFactory(linkedListFactory);
        deriveBThroughLinkedList = differentialOperator.deriveSynchronously(b);
        for (Point point : deriveBThroughLinkedList) {
            assertEquals(point.y, 2);
        }
        assertTrue(differentialOperator.getFactory() instanceof LinkedListTabulatedFunctionFactory);
    }
}