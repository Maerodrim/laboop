package ru.ssau.tk.sergunin.lab.functions.trigonometricFunctions;

import ru.ssau.tk.sergunin.lab.functions.AbstractMathFunction;
import ru.ssau.tk.sergunin.lab.functions.MathFunction;
import ru.ssau.tk.sergunin.lab.ui.ConnectableItem;
import ru.ssau.tk.sergunin.lab.ui.Item;

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