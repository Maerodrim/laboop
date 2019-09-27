package ru.ssau.tk.sergunin.lab.functions;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class UnmodifiableTabulatedFunctionTest {

    // TODO

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testSetY() {
        TabulatedFunction list = new UnmodifiableTabulatedFunction(new LinkedListTabulatedFunction(new double[]{1., 2., 3., 4., 5.}, new double[]{2., 4., 6., 8., 10.}));
        assertEquals(list.apply(2), 4);
        list.setY(1, 0);
    }
}