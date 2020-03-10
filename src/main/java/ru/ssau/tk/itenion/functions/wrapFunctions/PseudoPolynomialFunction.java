package ru.ssau.tk.itenion.functions.wrapFunctions;

import ru.ssau.tk.itenion.functions.AbstractMathFunction;
import ru.ssau.tk.itenion.functions.MathFunction;
import ru.ssau.tk.itenion.functions.powerFunctions.PolynomialFunction;

import java.io.Serializable;

public class PseudoPolynomialFunction extends AbstractMathFunction implements MathFunction, Serializable {
    private static final long serialVersionUID = -3344565831834182243L;
    PolynomialFunction polynomialFunction;

    public PseudoPolynomialFunction(PolynomialFunction polynomialFunction) {
        this.polynomialFunction = polynomialFunction;
        name = polynomialFunction.getName();
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
