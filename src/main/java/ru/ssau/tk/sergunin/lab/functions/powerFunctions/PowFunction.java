package ru.ssau.tk.sergunin.lab.functions.powerFunctions;

import ru.ssau.tk.sergunin.lab.functions.AbstractMathFunction;
import ru.ssau.tk.sergunin.lab.functions.MathFunction;
import ru.ssau.tk.sergunin.lab.ui.ConnectableItem;
import ru.ssau.tk.sergunin.lab.ui.Item;

@ConnectableItem(name = "Степенная функция", priority = 12, type = Item.FUNCTION, hasParameter = true)
public class PowFunction extends AbstractMathFunction implements MathFunction {
    private static final long serialVersionUID = -1379753316955199587L;
    private final double pow;

    public PowFunction(double pow) {
        this.pow = pow;
        name = "x^" + pow;
    }

    @Override
    public double apply(double x) {
        return Math.pow(x, pow);
    }

    @Override
    public MathFunction differentiate() {
        return pow != 0 ? new PowFunction(pow - 1).multiply(pow) : new ZeroFunction();
    }
}
