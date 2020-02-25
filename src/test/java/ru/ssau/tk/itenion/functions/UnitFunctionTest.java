package ru.ssau.tk.itenion.functions;

import org.testng.annotations.Test;
import ru.ssau.tk.itenion.functions.powerFunctions.UnitFunction;

import static org.testng.Assert.assertEquals;

public class UnitFunctionTest {

    @Test
    public void testApply() {
        MathFunction x = new UnitFunction();
        double ACCURACY = 0.0001;
        assertEquals(x.apply(5.), 1, ACCURACY);
    }
}