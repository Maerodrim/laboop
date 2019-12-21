package ru.ssau.tk.sergunin.lab.functions;

import ru.ssau.tk.sergunin.lab.ui.Item;
import ru.ssau.tk.sergunin.lab.ui.SelectableItem;

@SelectableItem(name = "Логарифм", priority = 11, type = Item.FUNCTION, parameter = true)
public class LogFunction implements MathFunction {
    private final double log;

    public LogFunction(double log) {
        this.log = log;
    }

    public LogFunction() {
        this(Math.E);
    }

    @Override
    public double apply(double x) {
        if (log < 0 || log == 1 || x < 0) {
            throw new UnsupportedOperationException();
        }
        return Math.log(x) / Math.log(log);
    }
}
