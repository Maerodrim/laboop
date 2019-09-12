package ru.ssau.tk.sergunin.lab.functions;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class IdentityFunctionTest {

    @Test
    public void testApply() {
        MathFunction x = new IdentityFunction();
        assertEquals(x.apply(5), 5, 0.0001);
    }
}