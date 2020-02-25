package ru.ssau.tk.itenion.functions;

import ru.ssau.tk.itenion.functions.tabulatedFunctions.TabulatedFunction;
import ru.ssau.tk.itenion.operations.DifferentialOperator;
import ru.ssau.tk.itenion.operations.MathFunctionDifferentialOperator;


public class CompositeFunction extends AbstractMathFunction implements MathFunction {
    private static final long serialVersionUID = 4047520069658157120L;
    private MathFunction outer;
    private MathFunction inner;

    public CompositeFunction(MathFunction outer, MathFunction inner) {
        this.inner = inner;
        this.outer = outer;
        name = outer.getName().replaceAll("x", "(" + inner.getName() + ")");
    }

    public double apply(double x) {
        return outer.apply(inner.apply(x));
    }

    public MathFunction getOuter() {
        return outer instanceof TabulatedFunction ? ((TabulatedFunction) outer).getMathFunction() : outer;
    }

    public MathFunction getInner() {
        return inner instanceof TabulatedFunction ? ((TabulatedFunction) inner).getMathFunction() : inner;
    }

    @Override
    public MathFunction differentiate() {
        DifferentialOperator<MathFunction> differentialOperator = new MathFunctionDifferentialOperator();
        return differentialOperator.derive(getOuter()).andThen(differentialOperator.derive(getInner()));
    }
}
