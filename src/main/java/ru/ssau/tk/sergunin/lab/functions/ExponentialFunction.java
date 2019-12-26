package ru.ssau.tk.sergunin.lab.functions;

import ru.ssau.tk.sergunin.lab.ui.Item;
import ru.ssau.tk.sergunin.lab.ui.ConnectableItem;

@ConnectableItem(name = "Показательная функция", priority = 9, type = Item.FUNCTION, parameter = true)
public class ExponentialFunction implements MathFunction {
    private final double exp;

    public ExponentialFunction(double exp) {
        this.exp = exp;
    }

    @Override
    public double apply(double x) {
        if (exp < 0 && (int) x != x) {
            throw new UnsupportedOperationException();
        }
        return Math.pow(exp, x);
    }
}