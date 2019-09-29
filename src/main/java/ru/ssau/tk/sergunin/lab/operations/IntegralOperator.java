package ru.ssau.tk.sergunin.lab.operations;

import ru.ssau.tk.sergunin.lab.functions.MathFunction;

public interface IntegralOperator<T extends MathFunction> extends MathFunction {
    T integrate(T function);

    @Override
    default double apply(double x) {
        return 0;
    }
}
