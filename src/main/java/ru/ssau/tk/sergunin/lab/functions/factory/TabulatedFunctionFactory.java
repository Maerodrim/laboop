package ru.ssau.tk.sergunin.lab.functions.factory;

import ru.ssau.tk.sergunin.lab.functions.TabulatedFunction;

public interface TabulatedFunctionFactory {
    TabulatedFunction create(double[] xValues, double[] yValues);
}
