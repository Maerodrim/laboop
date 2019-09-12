package ru.ssau.tk.sergunin.lab.functions;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class CompositeFunctionTest {

    @Test
    public void testApply() {
        MathFunction FuncH=new IdentityFunction();
        MathFunction FuncF=new IdentityFunction();
        MathFunction FuncG=new CompositeFunction(FuncH,FuncF);
        assertEquals(FuncG.apply(5), 5, 0.0001);
    }
}