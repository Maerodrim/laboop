package ru.ssau.tk.sergunin.lab.functions;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class CbrtFunctionTest {

    @Test
    public void testApply() {
        MathFunction x=new CbrtFunction();
        assertEquals(x.apply(5), Math.pow(5,1./3), 0.0001);
    }
}