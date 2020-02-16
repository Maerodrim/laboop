package ru.ssau.tk.sergunin.lab.functions.powerFunctions;

import ru.ssau.tk.sergunin.lab.functions.MathFunction;
import ru.ssau.tk.sergunin.lab.ui.Item;
import ru.ssau.tk.sergunin.lab.ui.ConnectableItem;

@ConnectableItem(name = "Константа", priority = 3, type = Item.FUNCTION, hasParameter = true)
public class ConstantFunction implements MathFunction {
    final private double constant;

    public ConstantFunction(double constant) {
        this.constant = constant;
    }

    @Override
    public double apply(double x) throws ArrayIndexOutOfBoundsException, IllegalArgumentException {
        return constant;
    }

    public double getConstant() {
        return constant;
    }

    @Override
    public String toString(){
        return constant + "";
    }
}
