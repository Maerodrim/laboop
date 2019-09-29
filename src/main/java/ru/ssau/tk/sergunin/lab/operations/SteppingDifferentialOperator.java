package ru.ssau.tk.sergunin.lab.operations;

import ru.ssau.tk.sergunin.lab.functions.MathFunction;

public abstract class SteppingDifferentialOperator implements DifferentialOperator<MathFunction> {

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
