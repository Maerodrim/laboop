package ru.ssau.tk.sergunin.lab.operations;

import ru.ssau.tk.sergunin.lab.functions.MathFunction;
import ru.ssau.tk.sergunin.lab.functions.TabulatedFunction;

public interface DifferentialOperator<T extends MathFunction> extends MathFunction {
   public  T derive(T function);
}
