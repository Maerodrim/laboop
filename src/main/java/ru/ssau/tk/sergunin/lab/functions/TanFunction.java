package ru.ssau.tk.sergunin.lab.functions;

import ru.ssau.tk.sergunin.lab.ui.Item;
import ru.ssau.tk.sergunin.lab.ui.SelectableItem;

@SelectableItem(name = "Тангенс", priority = 14, type = Item.FUNCTION)
public class TanFunction implements MathFunction {
    @Override
    public double apply(double x) throws ArrayIndexOutOfBoundsException, IllegalArgumentException {
        return java.lang.Math.tan(x);
    }
}