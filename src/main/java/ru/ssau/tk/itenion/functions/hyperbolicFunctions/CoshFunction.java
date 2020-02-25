package ru.ssau.tk.itenion.functions.hyperbolicFunctions;

import ru.ssau.tk.itenion.functions.AbstractMathFunction;
import ru.ssau.tk.itenion.functions.MathFunction;
import ru.ssau.tk.itenion.ui.ConnectableItem;
import ru.ssau.tk.itenion.ui.Item;

@ConnectableItem(name = "Гиперболический косинус", priority = 132, type = Item.FUNCTION)
public class CoshFunction extends AbstractMathFunction implements MathFunction {
    private static final long serialVersionUID = 8650346793589731760L;

    public CoshFunction() {
        name = "cosh(x)";
    }

    @Override
    public double apply(double x) throws ArrayIndexOutOfBoundsException, IllegalArgumentException {
        return Math.cosh(x);
    }

    @Override
    public MathFunction differentiate() {
        return new SinhFunction();
    }
}
