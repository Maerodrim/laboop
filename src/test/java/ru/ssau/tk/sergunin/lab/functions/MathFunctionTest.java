package ru.ssau.tk.sergunin.lab.functions;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class MathFunctionTest {
    private MathFunction sinh = new SinhFunction();
    private MathFunction id = new IdentityFunction();
    private MathFunction cbrt = new CbrtFunction();
    private MathFunction sqr = new SqrFunction();
    private MathFunction composite = sinh.andThen(sqr).andThen(cbrt);

    @Test
    public void testAndThen() {
        assertEquals(composite.apply(8), Math.sinh(4), 0.00001);
        assertEquals(composite.andThen(id).apply(8), Math.sinh(4), 0.00001);
    }
}