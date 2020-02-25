package ru.ssau.tk.itenion.functions;

import org.testng.annotations.Test;
import ru.ssau.tk.itenion.functions.powerFunctions.ConstantFunction;

import static org.testng.Assert.assertEquals;

public class ConstantFunctionTest {
    private final double ACCURACY = 0.00001;
    private MathFunction x = new ConstantFunction(10);

    @Test
    public void testApply() {
        assertEquals(x.apply(5.), 10, ACCURACY);
    }

    @Test
    public void testGetConstant() {
        assertEquals(((ConstantFunction) x).getConstant(), 10, ACCURACY);
    }
}