package ru.ssau.tk.sergunin.lab.functions;

import org.testng.annotations.Test;
import ru.ssau.tk.sergunin.lab.functions.powerFunctions.ZeroFunction;

import static org.testng.Assert.assertEquals;

public class ZeroFunctionTest {

    @Test
    public void testApply() {
        MathFunction x = new ZeroFunction();
        double ACCURACY = 0.0001;
        assertEquals(x.apply(5.), 0, ACCURACY);
    }
}