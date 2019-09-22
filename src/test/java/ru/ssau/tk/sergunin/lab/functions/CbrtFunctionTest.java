package ru.ssau.tk.sergunin.lab.functions;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class CbrtFunctionTest {

    private final double ACCURACY = 0.00001;

    @Test
    public void testApply() {
        MathFunction x = new CbrtFunction();
        assertEquals(x.apply(5), Math.pow(5, 1. / 3), ACCURACY);
    }
}