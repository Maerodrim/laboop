package ru.ssau.tk.sergunin.lab.functions;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class ConstantFunctionTest {
    private MathFunction x = new ConstantFunction(10);

    @Test
    public void testApply() {
        assertEquals(x.apply(5), 10, 0.0001);
    }

    @Test
    public void testGetConstant() {
        assertEquals(((ConstantFunction) x).getConstant(), 10, 0.0001);
    }
}