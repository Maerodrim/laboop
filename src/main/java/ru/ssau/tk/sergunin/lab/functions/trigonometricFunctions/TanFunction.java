package ru.ssau.tk.sergunin.lab.functions.trigonometricFunctions;

import ru.ssau.tk.sergunin.lab.functions.MathFunction;
import ru.ssau.tk.sergunin.lab.ui.Item;
import ru.ssau.tk.sergunin.lab.ui.ConnectableItem;

@ConnectableItem(name = "Тангенс", priority = 14, type = Item.FUNCTION)
public class TanFunction implements MathFunction {
    @Override
    public double apply(double x) throws ArrayIndexOutOfBoundsException, IllegalArgumentException {
        return Math.tan(x);
    }

    @Override
    public String toString(){
        return "tan(x)";
    }
}