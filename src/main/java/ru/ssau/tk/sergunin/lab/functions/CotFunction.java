package ru.ssau.tk.sergunin.lab.functions;

import ru.ssau.tk.sergunin.lab.ui.SelectableFunction;

@SelectableFunction(name = "Котангенс", priority = 15)
public class CotFunction implements MathFunction {
    @Override
    public double apply(double x) throws ArrayIndexOutOfBoundsException, IllegalArgumentException {
        return 1 / java.lang.Math.tan(x);
    }
}