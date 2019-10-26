package ru.ssau.tk.sergunin.lab.functions;

public class CompositeFunction implements MathFunction {
    private MathFunction funcH;
    private MathFunction funcG;

    public CompositeFunction(MathFunction funcH, MathFunction funcG) {
        this.funcG = funcG;
        this.funcH = funcH;
    }

    public double apply(double x) {
        return funcH.apply(funcG.apply(x));
    }
}
