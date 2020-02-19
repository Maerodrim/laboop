package ru.ssau.tk.sergunin.lab.functions.trigonometricFunctions;

import ru.ssau.tk.sergunin.lab.functions.AbstractMathFunction;
import ru.ssau.tk.sergunin.lab.functions.MathFunction;
import ru.ssau.tk.sergunin.lab.ui.ConnectableItem;
import ru.ssau.tk.sergunin.lab.ui.Item;

@ConnectableItem(name = "Синус", priority = 121, type = Item.FUNCTION)
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