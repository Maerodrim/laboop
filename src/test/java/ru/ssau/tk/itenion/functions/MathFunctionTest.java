package ru.ssau.tk.itenion.functions;

import org.testng.annotations.Test;
import ru.ssau.tk.itenion.functions.hyperbolicFunctions.SinhFunction;
import ru.ssau.tk.itenion.functions.powerFunctions.CbrtFunction;
import ru.ssau.tk.itenion.functions.powerFunctions.IdentityFunction;
import ru.ssau.tk.itenion.functions.powerFunctions.SqrFunction;

import static org.testng.Assert.assertEquals;

public class MathFunctionTest {
    private MathFunction sinh = new SinhFunction();
    private MathFunction id = new IdentityFunction();
    private MathFunction cbrt = new CbrtFunction();
    private MathFunction sqr = new SqrFunction();
    private MathFunction composite = sinh.andThen(sqr).andThen(cbrt);

    @Test
    public void testAndThen() {
        double ACCURACY = 0.0001;
        assertEquals(composite.apply(8.), Math.sinh(4), ACCURACY);
        assertEquals(composite.andThen(id).apply(8.), Math.sinh(4), ACCURACY);
    }
}