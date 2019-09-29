package ru.ssau.tk.sergunin.lab.operations;

import ru.ssau.tk.sergunin.lab.functions.TabulatedFunction;
import ru.ssau.tk.sergunin.lab.functions.factory.TabulatedFunctionFactory;

public class TabulatedDifferentialOperator extends TabulatedOperator implements DifferentialOperator<TabulatedFunction> {

    public TabulatedDifferentialOperator() {
        super();
    }

    public TabulatedDifferentialOperator(TabulatedFunctionFactory factory) {
        super(factory);
    }

    @Override
    public TabulatedFunction derive(TabulatedFunction function) {
        return super.derive(function);
    }
}
