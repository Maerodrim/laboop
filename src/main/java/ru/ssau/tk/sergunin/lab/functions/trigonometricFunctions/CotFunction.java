package ru.ssau.tk.sergunin.lab.functions.trigonometricFunctions;

import ru.ssau.tk.sergunin.lab.functions.MathFunction;
import ru.ssau.tk.sergunin.lab.ui.Item;
import ru.ssau.tk.sergunin.lab.ui.ConnectableItem;

@ConnectableItem(name = "Котангенс", priority = 15, type = Item.FUNCTION)
public class CotFunction implements MathFunction {
    @Override
    public double apply(double x) throws ArrayIndexOutOfBoundsException, IllegalArgumentException {
        return 1 / java.lang.Math.tan(x);
    }

    @Override
    public String toString(){
        return "cot(x)";
    }
}