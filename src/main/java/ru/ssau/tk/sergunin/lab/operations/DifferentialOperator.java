package ru.ssau.tk.sergunin.lab.operations;

import ru.ssau.tk.sergunin.lab.functions.MathFunction;

public interface DifferentialOperator extends MathFunction {
    <T> T derive(T function);
}
