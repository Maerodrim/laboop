package ru.ssau.tk.itenion.functions.trigonometricFunctions;

import ru.ssau.tk.itenion.functions.AbstractMathFunction;
import ru.ssau.tk.itenion.functions.MathFunction;
import ru.ssau.tk.itenion.ui.ConnectableItem;
import ru.ssau.tk.itenion.ui.Item;

@ConnectableItem(name = "Котангенс", priority = 124, type = Item.FUNCTION, isAdjacentConstant = true)
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