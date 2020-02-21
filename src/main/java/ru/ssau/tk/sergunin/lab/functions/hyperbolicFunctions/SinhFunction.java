package ru.ssau.tk.sergunin.lab.functions.hyperbolicFunctions;

import ru.ssau.tk.sergunin.lab.functions.AbstractMathFunction;
import ru.ssau.tk.sergunin.lab.functions.MathFunction;
import ru.ssau.tk.sergunin.lab.ui.ConnectableItem;
import ru.ssau.tk.sergunin.lab.ui.Item;

@ConnectableItem(name = "Гиперболический синус", priority = 131, type = Item.FUNCTION)
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