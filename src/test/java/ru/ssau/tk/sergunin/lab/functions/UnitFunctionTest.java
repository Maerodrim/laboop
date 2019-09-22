package ru.ssau.tk.sergunin.lab.functions;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class UnitFunctionTest {
    final private double ACCURACY = 0.0001;

    @Test
    public void testApply() {
        MathFunction x = new UnitFunction();
        assertEquals(x.apply(5), 1, ACCURACY);
    }
}