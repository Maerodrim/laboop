package ru.ssau.tk.itenion.functions;

import org.testng.annotations.Test;
import ru.ssau.tk.itenion.functions.hyperbolicFunctions.SinhFunction;
import ru.ssau.tk.itenion.functions.powerFunctions.CbrtFunction;
import ru.ssau.tk.itenion.functions.powerFunctions.SqrFunction;
import ru.ssau.tk.itenion.functions.tabulatedFunctions.ArrayTabulatedFunction;
import ru.ssau.tk.itenion.functions.tabulatedFunctions.LinkedListTabulatedFunction;

import static org.testng.Assert.assertEquals;

public class CompositeFunctionTest {

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
        double ACCURACY = 0.0001;
        assertEquals(funcG.apply(5.), Math.sinh(25), ACCURACY);

        assertEquals(tabArray.andThen(sqr).apply(2.), 76, ACCURACY);
        assertEquals(sqr.andThen(tabArray).apply(2.), 1936, ACCURACY);

        assertEquals(cbrt.andThen(sinh).andThen(tabList).andThen(cbrt).apply(2.), 693.2857, ACCURACY);
        assertEquals(cbrt.andThen(sinh).andThen(tabArray).andThen(cbrt).apply(2.), 693.2857, ACCURACY);
    }
}