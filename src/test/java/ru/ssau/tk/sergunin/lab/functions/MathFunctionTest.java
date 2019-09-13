package ru.ssau.tk.sergunin.lab.functions;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class MathFunctionTest {
    MathFunction sinh = new SinhFunction();
    MathFunction cbrt = new CbrtFunction();
    MathFunction sqr = new SqrFunction();
    MathFunction composite = sinh.andThen(sqr).andThen(cbrt);

    @Test
    public void testAndThen() {
        assertEquals(composite.apply(8), Math.sinh(4), 0.00001);
    }
}