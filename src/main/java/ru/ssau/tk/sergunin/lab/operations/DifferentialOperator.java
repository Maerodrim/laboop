package ru.ssau.tk.sergunin.lab.operations;

import ru.ssau.tk.sergunin.lab.functions.MathFunction;

public interface DifferentialOperator <T extends MathFunction> extends MathFunction {
    T derive(T function);
}
