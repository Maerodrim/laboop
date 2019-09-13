package ru.ssau.tk.sergunin.lab.functions;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class SinhFunctionTest {

    @Test
    public void testApply() {
        MathFunction x = new SinhFunction();
        assertEquals(x.apply(1), Math.sinh(1), 0.0001);
    }
}