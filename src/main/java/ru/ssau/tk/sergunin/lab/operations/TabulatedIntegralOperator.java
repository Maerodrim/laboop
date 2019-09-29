package ru.ssau.tk.sergunin.lab.operations;

import ru.ssau.tk.sergunin.lab.functions.TabulatedFunction;
import ru.ssau.tk.sergunin.lab.functions.factory.ArrayTabulatedFunctionFactory;
import ru.ssau.tk.sergunin.lab.functions.factory.TabulatedFunctionFactory;

public class TabulatedIntegralOperator extends TabulatedOperator implements IntegralOperator<TabulatedFunction> {
    public TabulatedIntegralOperator() {
        super();
    }

    public TabulatedIntegralOperator(TabulatedFunctionFactory factory) {
        super(factory);
    }

    @Override
    public TabulatedFunction integrate(TabulatedFunction function) {
        return new TabulatedFunctionOperationService(new ArrayTabulatedFunctionFactory()).subtract(super.integrate(function), super.shift);
    }
}
