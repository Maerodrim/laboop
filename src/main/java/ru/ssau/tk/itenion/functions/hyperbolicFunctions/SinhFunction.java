package ru.ssau.tk.itenion.functions.hyperbolicFunctions;

import ru.ssau.tk.itenion.functions.AbstractMathFunction;
import ru.ssau.tk.itenion.functions.MathFunction;
import ru.ssau.tk.itenion.ui.ConnectableItem;
import ru.ssau.tk.itenion.ui.Item;

@ConnectableItem(name = "Гиперболический синус", priority = 131, type = Item.FUNCTION, isAdjacentConstant = true)
public class SinhFunction extends AbstractMathFunction implements MathFunction {
    private static final long serialVersionUID = 5697896654854631504L;

    public SinhFunction() {
        name = "sinh(x)";
    }

    @Override
    public double apply(double x) throws ArrayIndexOutOfBoundsException, IllegalArgumentException {
        return java.lang.Math.sinh(x);
    }

    @Override
    public MathFunction differentiate() {
        return new CoshFunction();
    }
}
