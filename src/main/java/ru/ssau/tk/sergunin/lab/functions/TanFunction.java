package ru.ssau.tk.sergunin.lab.functions;

import ru.ssau.tk.sergunin.lab.ui.SelectableFunction;

@SelectableFunction(name = "Тангенс", priority = 14)
public class TanFunction implements MathFunction {
    @Override
    public double apply(double x) throws ArrayIndexOutOfBoundsException, IllegalArgumentException {
        return java.lang.Math.tan(x);
    }
}