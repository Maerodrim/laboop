package ru.ssau.tk.sergunin.lab.functions;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class CompositeFunctionTest {

    @Test
    public void testApply() {
        MathFunction FuncH = new SinhFunction();
        MathFunction FuncF = new SqrFunction();
        MathFunction FuncG = new CompositeFunction(FuncH, FuncF);
        assertEquals(FuncG.apply(5), Math.sinh(25), 0.0001);
    }
}