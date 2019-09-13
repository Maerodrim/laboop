package ru.ssau.tk.sergunin.lab.functions;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class SqrFunctionTest {

    @Test
    public void testApply() {
        MathFunction x = new SqrFunction();
        assertEquals(x.apply(5), 25, 0.0001);
    }
}