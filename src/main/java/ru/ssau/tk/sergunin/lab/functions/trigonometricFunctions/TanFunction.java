package ru.ssau.tk.sergunin.lab.functions.trigonometricFunctions;

import ru.ssau.tk.sergunin.lab.functions.AbstractMathFunction;
import ru.ssau.tk.sergunin.lab.functions.MathFunction;
import ru.ssau.tk.sergunin.lab.ui.ConnectableItem;
import ru.ssau.tk.sergunin.lab.ui.Item;

@ConnectableItem(name = "Тангенс", priority = 123, type = Item.FUNCTION)
public class TanFunction extends AbstractMathFunction implements MathFunction {
    private static final long serialVersionUID = 328577643121116862L;

    public TanFunction() {
        name = "tan(x)";
    }

    @Override
    public double apply(double x) throws ArrayIndexOutOfBoundsException, IllegalArgumentException {
        return Math.tan(x);
    }

    @Override
    public MathFunction differentiate() {
        return (new CosFunction().multiply(new CosFunction())).inverse();
    }
}