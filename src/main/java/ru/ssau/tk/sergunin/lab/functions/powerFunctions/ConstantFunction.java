package ru.ssau.tk.sergunin.lab.functions.powerFunctions;

import ru.ssau.tk.sergunin.lab.functions.AbstractMathFunction;
import ru.ssau.tk.sergunin.lab.functions.MathFunction;
import ru.ssau.tk.sergunin.lab.ui.ConnectableItem;
import ru.ssau.tk.sergunin.lab.ui.Item;

@ConnectableItem(name = "Константа", priority = 3, type = Item.FUNCTION, hasParameter = true)
public class ConstantFunction extends AbstractMathFunction implements MathFunction {
    private static final long serialVersionUID = 4711466148660044986L;
    final private double constant;

    public ConstantFunction(double constant) {
        this.constant = constant;
        name = constant + "";
    }

    @Override
    public double apply(double x) throws ArrayIndexOutOfBoundsException, IllegalArgumentException {
        return constant;
    }

    public double getConstant() {
        return constant;
    }

    @Override
    public MathFunction differentiate() {
        return new ZeroFunction();
    }
}
