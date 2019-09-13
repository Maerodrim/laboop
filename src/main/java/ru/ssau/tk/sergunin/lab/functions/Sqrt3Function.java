package ru.ssau.tk.sergunin.lab.functions;

public class Sqrt3Function implements MathFunction {
    public double apply(double x) {
        return Math.pow(x, 1. / 3);
    }
    ;
}
