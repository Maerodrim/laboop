package ru.ssau.tk.itenion.functions.trigonometricFunctions;

import ru.ssau.tk.itenion.functions.AbstractMathFunction;
import ru.ssau.tk.itenion.functions.MathFunction;
import ru.ssau.tk.itenion.ui.ConnectableItem;
import ru.ssau.tk.itenion.ui.Item;

@ConnectableItem(name = "Синус", priority = 121, type = Item.FUNCTION, isAdjacentConstant = true)
public class SinFunction extends AbstractMathFunction implements MathFunction {
    private static final long serialVersionUID = -2240731871796397021L;

    public SinFunction() {
        name = "sin(x)";
    }

    @Override
    public double apply(double x) throws ArrayIndexOutOfBoundsException, IllegalArgumentException {
        return java.lang.Math.sin(x);
    }

    @Override
    public MathFunction differentiate() {
        return new CosFunction();
    }
}