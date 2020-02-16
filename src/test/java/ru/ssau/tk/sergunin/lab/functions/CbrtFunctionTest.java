package ru.ssau.tk.sergunin.lab.functions;

import org.testng.annotations.Test;
import ru.ssau.tk.sergunin.lab.functions.powerFunctions.CbrtFunction;

import static org.testng.Assert.assertEquals;

public class CbrtFunctionTest {

    @Test
    public void testApply() {
        MathFunction x = new CbrtFunction();
        double ACCURACY = 0.00001;
        assertEquals(x.apply(5), Math.pow(5, 1. / 3), ACCURACY);
    }
}