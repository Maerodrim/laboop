package ru.ssau.tk.itenion.functions;

import org.testng.annotations.Test;
import ru.ssau.tk.itenion.functions.hyperbolicFunctions.SinhFunction;

import static org.testng.Assert.assertEquals;

public class SinhFunctionTest {

    @Test
    public void testApply() {
        MathFunction x = new SinhFunction();
        double ACCURACY = 0.0001;
        assertEquals(x.apply(1.), Math.sinh(1), ACCURACY);
    }
}