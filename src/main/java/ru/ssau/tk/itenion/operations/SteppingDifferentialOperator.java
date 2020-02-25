package ru.ssau.tk.itenion.operations;

import ru.ssau.tk.itenion.functions.MathFunction;

public abstract class SteppingDifferentialOperator implements DifferentialOperator<MathFunction> {
    protected static final String VARIABLE = "x";
    protected double step;

    SteppingDifferentialOperator(double step) {
        if (step <= 0 || step == Double.POSITIVE_INFINITY || step != step) {
            throw new IllegalArgumentException();
        }
        this.step = step;
    }

    public double getStep() {
        return step;
    }

    public void setStep(double step) {
        if (step <= 0 || step == Double.POSITIVE_INFINITY || step != step) {
            throw new IllegalArgumentException();
        }
        this.step = step;
    }

}
