package ru.ssau.tk.sergunin.lab.operations;

import org.testng.annotations.Test;
import ru.ssau.tk.sergunin.lab.functions.*;
import ru.ssau.tk.sergunin.lab.functions.exponentialFunctions.ExpFunction;
import ru.ssau.tk.sergunin.lab.functions.powerFunctions.IdentityFunction;
import ru.ssau.tk.sergunin.lab.functions.powerFunctions.SqrFunction;
import ru.ssau.tk.sergunin.lab.functions.powerFunctions.UnitFunction;

import static org.testng.Assert.assertEquals;

public class MathFunctionIntegralOperatorTest {

    @Test
    public void testIntegrate() {
        MathFunction sqr = new SqrFunction();
        IntegralOperator<MathFunction> integralOperator = new MathFunctionIntegralOperator(1);
        MathFunction cube = integralOperator.integrate(sqr.andThen(new IdentityFunction().subtract(new UnitFunction())));
        assertEquals(cube.apply(4), 9, 0.01);

        MathFunction exp = new ExpFunction();
        MathFunction tooExp = integralOperator.integrate(exp);
        assertEquals(tooExp.apply(3), Math.E * Math.E * Math.E, 0.01);

/*        MathFunction exp = new PowFunction(0.5).andThen(new ExpFunction());
        MathFunction tooExp = integralOperator.integrate(exp);
        assertEquals(tooExp.apply(2), 2*Math.E, 0.01);*/
    }
}