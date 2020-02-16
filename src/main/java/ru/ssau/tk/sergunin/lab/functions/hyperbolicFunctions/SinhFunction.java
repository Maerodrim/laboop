package ru.ssau.tk.sergunin.lab.functions.hyperbolicFunctions;

import ru.ssau.tk.sergunin.lab.functions.MathFunction;
import ru.ssau.tk.sergunin.lab.ui.Item;
import ru.ssau.tk.sergunin.lab.ui.ConnectableItem;

@ConnectableItem(name = "Гиперболический синус", priority = 16, type = Item.FUNCTION)
public class SinhFunction implements MathFunction {
    @Override
    public double apply(double x) throws ArrayIndexOutOfBoundsException, IllegalArgumentException {
        return java.lang.Math.sinh(x);
    }

    @Override
    public String toString(){
        return "sinh(x)";
    }
}
