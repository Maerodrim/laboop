package ru.ssau.tk.itenion.operations;

import ru.ssau.tk.itenion.functions.MathFunction;

public class RightSteppingDifferentialOperator extends SteppingDifferentialOperator {

    RightSteppingDifferentialOperator(double step) {
        super(step);
    }

    @Override
    public MathFunction derive(MathFunction function) {
        return new MathFunction() {
            private static final long serialVersionUID = -8920707897773883408L;

            @Override
            public double apply(double x) {
                return (function.apply(x + step) - function.apply(x)) / step;
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