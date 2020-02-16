package ru.ssau.tk.sergunin.lab.functions;

import org.testng.annotations.Test;
import ru.ssau.tk.sergunin.lab.functions.exponentialFunctions.ExponentialFunction;

import static org.testng.Assert.assertThrows;

public class ExpFunctionTest {

    @Test
    public void testApply() {
        assertThrows(UnsupportedOperationException.class, () -> new ExponentialFunction(-1).apply(1.5));
    }
}