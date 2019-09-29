package ru.ssau.tk.sergunin.lab.functions;

public class PowFunction implements MathFunction {
    private final double pow;

    PowFunction(double pow) {
        this.pow = pow;
    }

    @Override
    public double apply(double x) {
        return Math.pow(x, pow);
    }
}
