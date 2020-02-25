package ru.ssau.tk.itenion.functions.exponentialFunctions;

import ru.ssau.tk.itenion.functions.AbstractMathFunction;
import ru.ssau.tk.itenion.functions.MathFunction;
import ru.ssau.tk.itenion.functions.powerFunctions.PowFunction;
import ru.ssau.tk.itenion.ui.ConnectableItem;
import ru.ssau.tk.itenion.ui.Item;

@ConnectableItem(name = "Логарифм", priority = 110, type = Item.FUNCTION, hasParameter = true)
public class LogFunction extends AbstractMathFunction implements MathFunction {
    private static final long serialVersionUID = -3816833312969864166L;
    private final double log;

    public LogFunction(double log) {
        this.log = log;
        name = "log" + log + "(x)";
    }

    @Override
    public double apply(double x) {
        if (log < 0 || log == 1 || x < 0) {
            throw new UnsupportedOperationException();
        }
        return Math.log(x) / Math.log(log);
    }

    @Override
    public MathFunction differentiate() {
        return new PowFunction(-1).multiply(1. / Math.log(log));
    }
}
