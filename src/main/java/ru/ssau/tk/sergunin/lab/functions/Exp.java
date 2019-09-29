package ru.ssau.tk.sergunin.lab.functions;

public class Exp implements MathFunction {
    private final double exp;

    public Exp(double exp) {
        this.exp = exp;
    }

    public Exp() {
        this(Math.E);
    }

    @Override
    public double apply(double x) {
        if (exp < 0 && (int) x != x) {
            throw new UnsupportedOperationException();
        }
        return Math.pow(exp, x);
    }
}
