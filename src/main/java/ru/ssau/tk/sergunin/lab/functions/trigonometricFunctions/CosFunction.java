package ru.ssau.tk.sergunin.lab.functions.trigonometricFunctions;

import ru.ssau.tk.sergunin.lab.functions.MathFunction;
import ru.ssau.tk.sergunin.lab.ui.Item;
import ru.ssau.tk.sergunin.lab.ui.ConnectableItem;

@ConnectableItem(name = "Косинус", priority = 13, type = Item.FUNCTION)
public class CosFunction implements MathFunction {
    @Override
    public double apply(double x) throws ArrayIndexOutOfBoundsException, IllegalArgumentException {
        return Math.cos(x);
    }

    @Override
    public String toString(){
        return "cos(x)";
    }
}