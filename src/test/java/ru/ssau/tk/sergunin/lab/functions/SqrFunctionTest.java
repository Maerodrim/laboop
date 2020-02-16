package ru.ssau.tk.sergunin.lab.functions;

import org.testng.annotations.Test;
import ru.ssau.tk.sergunin.lab.functions.powerFunctions.SqrFunction;

import static org.testng.Assert.assertEquals;

public class SqrFunctionTest {

    @Test
    public void testApply() {
        MathFunction x = new SqrFunction();
        double ACCURACY = 0.0001;
        assertEquals(x.apply(5), 25, ACCURACY);
    }
}