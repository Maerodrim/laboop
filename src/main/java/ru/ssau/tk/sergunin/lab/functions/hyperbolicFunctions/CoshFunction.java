package ru.ssau.tk.sergunin.lab.functions.hyperbolicFunctions;

import ru.ssau.tk.sergunin.lab.functions.MathFunction;
import ru.ssau.tk.sergunin.lab.ui.Item;
import ru.ssau.tk.sergunin.lab.ui.ConnectableItem;

@ConnectableItem(name = "Гиперболический косинус", priority = 17, type = Item.FUNCTION)
public class CoshFunction implements MathFunction {
    @Override
    public double apply(double x) throws ArrayIndexOutOfBoundsException, IllegalArgumentException {
        return Math.cosh(x);
    }

    @Override
    public String toString(){
        return "cosh(x)";
    }
}
