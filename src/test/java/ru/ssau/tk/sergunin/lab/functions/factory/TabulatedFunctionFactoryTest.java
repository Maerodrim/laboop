package ru.ssau.tk.sergunin.lab.functions.factory;

import org.testng.annotations.Test;
import ru.ssau.tk.sergunin.lab.functions.ArrayTabulatedFunction;
import ru.ssau.tk.sergunin.lab.functions.LinkedListTabulatedFunction;
import ru.ssau.tk.sergunin.lab.functions.TabulatedFunction;

import static org.testng.Assert.assertTrue;

public class TabulatedFunctionFactoryTest {
    private final double[] xValues = new double[]{1., 2., 3., 4., 5.};
    private final double[] yValues = new double[]{2., 4., 6., 8., 10.};

    @Test
    public void testCreate() {
        TabulatedFunctionFactory arrayTabulatedFunctionFactory = new ArrayTabulatedFunctionFactory();
        TabulatedFunctionFactory linkedListTabulatedFunctionFactory = new LinkedListTabulatedFunctionFactory();

        TabulatedFunction func = arrayTabulatedFunctionFactory.create(xValues, yValues);
        assertTrue(func instanceof ArrayTabulatedFunction);
        func = linkedListTabulatedFunctionFactory.create(xValues, yValues);
        assertTrue(func instanceof LinkedListTabulatedFunction);
    }
}