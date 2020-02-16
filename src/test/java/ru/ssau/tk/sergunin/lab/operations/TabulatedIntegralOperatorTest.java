package ru.ssau.tk.sergunin.lab.operations;

import org.testng.annotations.Test;
import ru.ssau.tk.sergunin.lab.functions.factory.ArrayTabulatedFunctionFactory;
import ru.ssau.tk.sergunin.lab.functions.powerFunctions.PowFunction;
import ru.ssau.tk.sergunin.lab.functions.tabulatedFunctions.ArrayTabulatedFunction;
import ru.ssau.tk.sergunin.lab.functions.tabulatedFunctions.TabulatedFunction;

import static org.testng.Assert.assertTrue;

public class TabulatedIntegralOperatorTest {

    @Test
    public void testIntegrate() {
        TabulatedFunction actual = new ArrayTabulatedFunction(new PowFunction(2), 1, 5, 5000001);
        TabulatedFunction expected = new ArrayTabulatedFunction(new PowFunction(2), 1, 5, 1001);
        TabulatedIntegralOperator integralOperator = new TabulatedIntegralOperator();
        integralOperator.setFactory(new ArrayTabulatedFunctionFactory());
        assertTrue(integralOperator.getFactory() instanceof ArrayTabulatedFunctionFactory);
        TabulatedDifferentialOperator differentialOperator = new TabulatedDifferentialOperator();

        //TabulatedFunction integrateActual = differentialOperator.derive(integralOperator.integrate(actual));
        TabulatedFunction integrateActual = integralOperator.integrate(actual);

        int n = 5;

        /*Point[] actualPoint = asPoints(integrateActual);
        Point[] expectedPoint = asPoints(expected);

        for (int i = 2; i < integrateActual.getCount() - 2; i++) {
            assertEquals(actualPoint[i].y, expectedPoint[i].y, 0.0002); // Converges to within a constant
        }

        TabulatedFunction actualThroughLinkedList = new LinkedListTabulatedFunction(new PowFunction(2), -5, 5, 10001);
        TabulatedFunction expectedThroughLinkedList = new LinkedListTabulatedFunction(new PowFunction(3), -5, 5, 10001);
        TabulatedIntegralOperator integralOperatorThroughLinkedList = new TabulatedIntegralOperator(new LinkedListTabulatedFunctionFactory());
        TabulatedFunction integrateActualThroughLinkedList = integralOperatorThroughLinkedList.integrate(actualThroughLinkedList);

        Point[] actualPointThroughLinkedList = asPoints(integrateActualThroughLinkedList);
        Point[] expectedPointThroughLinkedList = asPoints(expectedThroughLinkedList);

        for (int i = 3; i < integrateActualThroughLinkedList.getCount() - 3; i++) {
            assertEquals(actualPointThroughLinkedList[i].y * 3, expectedPointThroughLinkedList[i].y, 0.05); // Converges to within a constant
        }*/

    }
}