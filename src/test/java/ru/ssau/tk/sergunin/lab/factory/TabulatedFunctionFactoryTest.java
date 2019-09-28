package ru.ssau.tk.sergunin.lab.factory;

import org.testng.annotations.Test;
import ru.ssau.tk.sergunin.lab.functions.TabulatedFunction;

import static org.testng.Assert.*;

public class TabulatedFunctionFactoryTest {
    private final double[] xValues = new double[]{1., 2., 3., 4., 5.};
    private final double[] yValues = new double[]{2., 4., 6., 8., 10.};

    @Test
    public void testCreate() {
        //TabulatedFunction func = new ArrayTabulatedFunctionFactory(xValues, yValues);
    }
}