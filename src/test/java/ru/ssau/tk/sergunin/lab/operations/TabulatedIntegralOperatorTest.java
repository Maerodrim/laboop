package ru.ssau.tk.sergunin.lab.operations;

import org.testng.annotations.Test;
import ru.ssau.tk.sergunin.lab.functions.*;
import ru.ssau.tk.sergunin.lab.functions.factory.LinkedListTabulatedFunctionFactory;

import java.util.Iterator;

import static org.testng.Assert.assertEquals;

public class TabulatedIntegralOperatorTest {

    @Test
    public void testIntegrate() {
        TabulatedFunction actual = new ArrayTabulatedFunction(new ExpFunction(), -2, 5, 10001);
        TabulatedFunction expected = new ArrayTabulatedFunction(new ExpFunction(), -2, 5, 10001);
        TabulatedFunction actualThroughLinkedList = new LinkedListTabulatedFunction(new PowFunction(-2/3.), 1, 5, 1001);
        TabulatedFunction expectedThroughLinkedList = new LinkedListTabulatedFunction(new CbrtFunction(), 1, 5, 1001);
        TabulatedIntegralOperator integralOperator = new TabulatedIntegralOperator();
        TabulatedIntegralOperator integralOperatorThroughLinkedList = new TabulatedIntegralOperator(new LinkedListTabulatedFunctionFactory());
        TabulatedFunction integrateActual = integralOperator.integrate(actual);

        Iterator<Point> iteratorOfIntegrateActual = integrateActual.iterator();
        Iterator<Point> iteratorOfExpected = expected.iterator();

        iteratorOfExpected.next(); // TODO

        while (iteratorOfExpected.hasNext()) {
            assertEquals(iteratorOfIntegrateActual.next().y + 1, iteratorOfExpected.next().y, 0.001); // Converges to within a constant
        }

        TabulatedFunction integrateActualThroughLinkedList = integralOperatorThroughLinkedList.integrate(actualThroughLinkedList);

        Iterator<Point> iteratorOfIntegrateActualThroughLinkedList = integrateActualThroughLinkedList.iterator();
        Iterator<Point> iteratorOfExpectedThroughLinkedList = expectedThroughLinkedList.iterator();

        iteratorOfExpectedThroughLinkedList.next(); // TODO

        while (iteratorOfExpectedThroughLinkedList.hasNext()) {
            assertEquals(iteratorOfIntegrateActualThroughLinkedList.next().y + 3, iteratorOfExpectedThroughLinkedList.next().y*3, 0.001); // Converges to within a constant
        }
    }
}