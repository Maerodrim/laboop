package ru.ssau.tk.itenion.functions.trigonometricFunctions;

import ru.ssau.tk.itenion.functions.AbstractMathFunction;
import ru.ssau.tk.itenion.functions.MathFunction;
import ru.ssau.tk.itenion.ui.ConnectableItem;
import ru.ssau.tk.itenion.ui.Item;

@ConnectableItem(name = "Тангенс", priority = 123, type = Item.FUNCTION, isAdjacentConstant = true)
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