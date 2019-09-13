package ru.ssau.tk.sergunin.lab.functions;

public class CompositeFunction implements MathFunction {
    private MathFunction FuncH;
    private MathFunction FuncG;

    public CompositeFunction(MathFunction FuncH, MathFunction FuncG) {
        this.FuncG = FuncG;
        this.FuncH = FuncH;
    }

    public double apply(double x) {
        return FuncH.apply(FuncG.apply(x));
    }
}
