package ru.ssau.tk.sergunin.lab.functions.powerFunctions.polynomial;

import ru.ssau.tk.sergunin.lab.functions.MathFunction;
import ru.ssau.tk.sergunin.lab.functions.powerFunctions.PolynomialFunction;

public class PseudoPolynomialFunction implements MathFunction {
    private static final long serialVersionUID = -3344565831834182243L;
    PolynomialFunction polynomialFunction;

    public PseudoPolynomialFunction(PolynomialFunction polynomialFunction) {
        this.polynomialFunction = polynomialFunction;
    }

    @Override
    public double apply(double x) {
        return polynomialFunction.apply(x);
    }

    @Override
    public MathFunction differentiate() {
        return polynomialFunction.differentiate();
    }

    @Override
    public String getName() {
        return polynomialFunction.getName();
    }
}
