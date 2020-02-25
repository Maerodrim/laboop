package ru.ssau.tk.itenion.operations;

import ru.ssau.tk.itenion.functions.MathFunction;

public interface DifferentialOperator<T extends MathFunction> {
    T derive(T function);
}
