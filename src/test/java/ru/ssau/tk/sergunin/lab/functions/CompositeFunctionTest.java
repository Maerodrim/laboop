package ru.ssau.tk.sergunin.lab.functions;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class CompositeFunctionTest {
    final private double accuracy = 0.0001;

    @Test
    public void testApply() {
        final double[] xValues = new double[]{1, 2, 3, 4, 5, 7, 9};
        final double[] yValues = new double[]{12, 44, 32, 76, 54, 87, 19};
        MathFunction sinh = new SinhFunction();
        MathFunction sqr = new SqrFunction();
        MathFunction cbrt = new CbrtFunction();
        MathFunction funcG = new CompositeFunction(sinh, sqr);
        MathFunction tabList = new LinkedListTabulatedFunction(xValues, yValues);
        MathFunction tabArray = new ArrayTabulatedFunction(xValues, yValues);
        assertEquals(funcG.apply(5), Math.sinh(25), accuracy);

        assertEquals(tabArray.andThen(sqr).apply(2), 76, accuracy);
        assertEquals(sqr.andThen(tabArray).apply(2), 1936, accuracy);
        assertEquals(cbrt.andThen(sinh).andThen(tabList).andThen(cbrt).apply(2), 693.2857, accuracy);
        assertEquals(cbrt.andThen(sinh).andThen(tabArray).andThen(cbrt).apply(2), 693.2857, accuracy);
    }
}