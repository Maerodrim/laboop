package ru.ssau.tk.sergunin.lab.functions;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class CompositeFunctionTest {

    @Test
    public void testApply() {
        MathFunction FuncH=new CbrtFunction();
        MathFunction FuncF=new SqrFunction();
        MathFunction FuncG=new CompositeFunction(FuncH,FuncF);
        assertEquals(FuncG.apply(8), 4, 0.0001);
    }
}