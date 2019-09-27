package ru.ssau.tk.sergunin.lab.functions;

public class ConstantFunction implements MathFunction {
    final private double constant;

    public ConstantFunction(double constant) {
        this.constant = constant;
    }

    @Override
    public double apply(double x) throws ArrayIndexOutOfBoundsException, IllegalArgumentException {
        return constant;
    }

    public double getConstant() {
        return constant;
    }
}
