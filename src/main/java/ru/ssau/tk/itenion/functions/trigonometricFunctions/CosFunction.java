package ru.ssau.tk.itenion.functions.trigonometricFunctions;

import ru.ssau.tk.itenion.functions.AbstractMathFunction;
import ru.ssau.tk.itenion.functions.MathFunction;
import ru.ssau.tk.itenion.ui.ConnectableItem;
import ru.ssau.tk.itenion.ui.Item;

@ConnectableItem(name = "Косинус", priority = 122, type = Item.FUNCTION)
public class CosFunction extends AbstractMathFunction implements MathFunction {
    private static final long serialVersionUID = -3834483377181979232L;

    public CosFunction() {
        name = "cos(x)";
    }

    @Override
    public double apply(double x) throws ArrayIndexOutOfBoundsException, IllegalArgumentException {
        return Math.cos(x);
    }

    @Override
    public MathFunction differentiate() {
        return new SinFunction().negate();
    }
}