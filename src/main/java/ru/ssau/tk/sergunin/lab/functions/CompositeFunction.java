package ru.ssau.tk.sergunin.lab.functions;

import ru.ssau.tk.sergunin.lab.functions.tabulatedFunctions.TabulatedFunction;
import ru.ssau.tk.sergunin.lab.ui.TabulatedFunctionAccessible;

public class CompositeFunction implements MathFunction {
    private MathFunction outer;
    private MathFunction inner;

    public CompositeFunction(MathFunction outer, MathFunction inner) {
        this.inner = inner;
        this.outer = outer;
    }

    public double apply(double x) {
        return outer.apply(inner.apply(x));
    }

    public MathFunction getOuter() {
        return outer;
    }

    public MathFunction getInner() {
        return inner;
    }

    @Override
    public String toString(){
        String innerString = inner instanceof TabulatedFunction ? ((TabulatedFunction) inner).printMathFunction() : inner.toString();
        String outerString = outer instanceof TabulatedFunction ? ((TabulatedFunction) outer).printMathFunction() : outer.toString();
        return outerString.replaceAll("x", "(" + innerString + ")");
    }
}
