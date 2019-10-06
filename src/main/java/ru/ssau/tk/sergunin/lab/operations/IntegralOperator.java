package ru.ssau.tk.sergunin.lab.operations;

import ru.ssau.tk.sergunin.lab.functions.MathFunction;

public interface IntegralOperator<T extends MathFunction> {
    T integrate(T function);
}
