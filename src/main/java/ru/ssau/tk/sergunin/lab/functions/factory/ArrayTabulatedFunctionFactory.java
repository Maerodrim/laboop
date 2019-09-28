package ru.ssau.tk.sergunin.lab.functions.factory;

import ru.ssau.tk.sergunin.lab.functions.ArrayTabulatedFunction;
import ru.ssau.tk.sergunin.lab.functions.TabulatedFunction;

public class ArrayTabulatedFunctionFactory implements TabulatedFunctionFactory {

    @Override
    public TabulatedFunction create(double[] xValues, double[] yValues) {
        return new ArrayTabulatedFunction(xValues, yValues);
    }
}
