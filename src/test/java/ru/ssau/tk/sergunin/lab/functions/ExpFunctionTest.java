package ru.ssau.tk.sergunin.lab.functions;

import org.testng.annotations.Test;

import static org.testng.Assert.assertThrows;

public class ExpFunctionTest {

    @Test
    public void testApply() {
        assertThrows(UnsupportedOperationException.class, () -> new ExpFunction(-1).apply(1.5));
    }
}