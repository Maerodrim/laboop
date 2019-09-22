package ru.ssau.tk.sergunin.lab.functions;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class SinhFunctionTest {
    final private double ACCURACY = 0.0001;

    @Test
    public void testApply() {
        MathFunction x = new SinhFunction();
        assertEquals(x.apply(1), Math.sinh(1), ACCURACY);
    }
}