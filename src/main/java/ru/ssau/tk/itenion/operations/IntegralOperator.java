package ru.ssau.tk.itenion.operations;

import ru.ssau.tk.itenion.functions.MathFunction;

public interface IntegralOperator<T extends MathFunction> {
    T integrate(T function);
}
