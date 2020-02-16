package ru.ssau.tk.sergunin.lab.operations;

import ru.ssau.tk.sergunin.lab.functions.PolynomialFunction;

public class PolynomialDifferentialOperator implements DifferentialOperator<PolynomialFunction> {
    @Override
    public PolynomialFunction derive(PolynomialFunction function) {
        return new PolynomialFunction(function.getPolynomial().derive());
    }
}
