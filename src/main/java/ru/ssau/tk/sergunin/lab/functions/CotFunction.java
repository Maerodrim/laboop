package ru.ssau.tk.sergunin.lab.functions;

import ru.ssau.tk.sergunin.lab.alt_ui.Selectable;

@Selectable(name = "Котангенс", priority = 15)
public class CotFunction implements MathFunction {
    @Override
    public double apply(double x) throws ArrayIndexOutOfBoundsException, IllegalArgumentException {
        return 1 / java.lang.Math.tan(x);
    }
}