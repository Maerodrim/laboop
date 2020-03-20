package ru.ssau.tk.itenion.functions;

/**
 * It should unite {@link MathFunction} and {@link ru.ssau.tk.itenion.functions.multipleVariablesFunctions.vectorFunctions.VMF}
 */
public interface TabHolderMathFunction<T extends AnyTabHolderMathFunction> extends AnyTabHolderMathFunction {
    double[] getDimensionArray();
}
