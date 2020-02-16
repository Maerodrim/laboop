package ru.ssau.tk.sergunin.lab.functions.exponentialFunctions;

import ru.ssau.tk.sergunin.lab.functions.MathFunction;
import ru.ssau.tk.sergunin.lab.ui.Item;
import ru.ssau.tk.sergunin.lab.ui.ConnectableItem;

@ConnectableItem(name = "Логарифм", priority = 11, type = Item.FUNCTION, hasParameter = true)
public class LogFunction implements MathFunction {
    private final double log;

    public LogFunction(double log) {
        this.log = log;
    }

    @Override
    public double apply(double x) {
        if (log < 0 || log == 1 || x < 0) {
            throw new UnsupportedOperationException();
        }
        return Math.log(x) / Math.log(log);
    }

    @Override
    public String toString(){
        return log + "(x)";
    }
}
