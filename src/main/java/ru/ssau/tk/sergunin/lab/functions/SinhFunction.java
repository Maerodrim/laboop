package ru.ssau.tk.sergunin.lab.functions;

public class SinhFunction implements MathFunction {
    @Override
    public double apply(double x) {
        return java.lang.Math.sinh(x);
    }
}
