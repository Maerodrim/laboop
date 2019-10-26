package ru.ssau.tk.sergunin.lab.functions;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class ZeroFunctionTest {
    final private double ACCURACY = 0.0001;

    @Test
    public void testApply() {
        MathFunction x = new ZeroFunction();
        assertEquals(x.apply(5), 0, ACCURACY);
    }
}