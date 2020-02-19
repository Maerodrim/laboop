package ru.ssau.tk.sergunin.lab.functions.trigonometricFunctions;

import ru.ssau.tk.sergunin.lab.functions.AbstractMathFunction;
import ru.ssau.tk.sergunin.lab.functions.MathFunction;
import ru.ssau.tk.sergunin.lab.ui.ConnectableItem;
import ru.ssau.tk.sergunin.lab.ui.Item;

@ConnectableItem(name = "Котангенс", priority = 124, type = Item.FUNCTION)
public class CotFunction extends AbstractMathFunction implements MathFunction {
    private static final long serialVersionUID = 2671809717408098478L;

    public CotFunction() {
        name = "cot(x)";
    }

    @Override
    public double apply(double x) throws ArrayIndexOutOfBoundsException, IllegalArgumentException {
        return 1 / java.lang.Math.tan(x);
    }

    @Override
    public MathFunction differentiate() {
        return new SinFunction().multiply(new SinFunction()).inverse().negate();
    }
}