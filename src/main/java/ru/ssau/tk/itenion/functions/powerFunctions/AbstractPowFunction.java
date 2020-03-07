package ru.ssau.tk.itenion.functions.powerFunctions;

import ru.ssau.tk.itenion.functions.AbstractMathFunction;
import ru.ssau.tk.itenion.functions.MathFunction;

public abstract class AbstractPowFunction<T extends Number> extends AbstractMathFunction {
    private static final long serialVersionUID = 8614974500777379496L;
    protected final T pow;

    public AbstractPowFunction(T pow) {
        this.pow = pow;
        name = "x^" + pow;
    }

    public abstract double apply(double x);

    public abstract MathFunction differentiate();

    public T getPow() {
        return pow;
    }
}
