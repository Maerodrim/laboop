package ru.ssau.tk.sergunin.lab.functions;

public class CbrtFunction implements MathFunction {
    public double apply(double x) {
        return Math.pow(x, 1. / 3);
    }
}
