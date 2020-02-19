package ru.ssau.tk.sergunin.lab.operations;

import ru.ssau.tk.sergunin.lab.functions.MathFunction;

public class MiddleSteppingDifferentialOperator extends SteppingDifferentialOperator {

    public MiddleSteppingDifferentialOperator(double step) {
        super(step);
    }

    @Override
    public MathFunction derive(MathFunction function) {
        return new MathFunction() {
            private static final long serialVersionUID = -8920707897773883408L;

            @Override
            public double apply(double x) {
                return (function.apply(x + step) - function.apply(x - step)) / (2 * step);
            }

            @Override
            public MathFunction differentiate() {
                return derive(function);
            }

            @Override
            public String getName() {
                return "d(" + function.getName() + ")/d" + VARIABLE;
            }
        };
    }

}