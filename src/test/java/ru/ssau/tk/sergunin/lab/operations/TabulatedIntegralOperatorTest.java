package ru.ssau.tk.sergunin.lab.operations;

import org.testng.annotations.Test;
import ru.ssau.tk.sergunin.lab.functions.*;
import ru.ssau.tk.sergunin.lab.functions.factory.ArrayTabulatedFunctionFactory;
import ru.ssau.tk.sergunin.lab.functions.factory.LinkedListTabulatedFunctionFactory;

import java.util.Iterator;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class TabulatedIntegralOperatorTest {

    @Test
    public void testIntegrate() {
        TabulatedFunction actual = new ArrayTabulatedFunction(new PowFunction(5), -5, 5, 1000001);
        TabulatedFunction expected = new ArrayTabulatedFunction(new PowFunction(5), -5, 5, 1000001);
        TabulatedFunction actualThroughLinkedList = new LinkedListTabulatedFunction(new PowFunction(-2 / 3.), 1, 10, 10001);
        TabulatedFunction expectedThroughLinkedList = new LinkedListTabulatedFunction(new CbrtFunction(), 1, 10, 10001);
        TabulatedIntegralOperator integralOperator = new TabulatedIntegralOperator();

        integralOperator.setFactory(new ArrayTabulatedFunctionFactory());
        assertTrue(integralOperator.getFactory() instanceof ArrayTabulatedFunctionFactory);

        TabulatedDifferentialOperator differentialOperator = new TabulatedDifferentialOperator();
        TabulatedIntegralOperator integralOperatorThroughLinkedList = new TabulatedIntegralOperator(new LinkedListTabulatedFunctionFactory());
        TabulatedFunction integrateActual = differentialOperator.derive(integralOperator.integrate(actual));

        Iterator<Point> iteratorOfIntegrateActual = integrateActual.iterator();
        Iterator<Point> iteratorOfExpected = expected.iterator();

        for (Point point : expected) {
            assertEquals(iteratorOfIntegrateActual.next().y, iteratorOfExpected.next().y, 0.02); // Converges to within a constant
        }

        TabulatedFunction integrateActualThroughLinkedList = integralOperatorThroughLinkedList.integrate(actualThroughLinkedList);

        Iterator<Point> iteratorOfIntegrateActualThroughLinkedList = integrateActualThroughLinkedList.iterator();

        for (Point point : expectedThroughLinkedList) {
            assertEquals(iteratorOfIntegrateActualThroughLinkedList.next().y + 3, point.y * 3, 0.001); // Converges to within a constant
        }

    }
}