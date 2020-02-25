package ru.ssau.tk.itenion.functions.hyperbolicFunctions;

import ru.ssau.tk.itenion.functions.AbstractMathFunction;
import ru.ssau.tk.itenion.functions.MathFunction;
import ru.ssau.tk.itenion.ui.ConnectableItem;
import ru.ssau.tk.itenion.ui.Item;

@ConnectableItem(name = "Гиперболический тангенс", priority = 133, type = Item.FUNCTION)
public class TanhFunction extends AbstractMathFunction implements MathFunction {

    private static final long serialVersionUID = -6316407655195470376L;

    public TanhFunction() {
        name = "tanh(x)";
    }

    @Override
    public double apply(double x) throws ArrayIndexOutOfBoundsException, IllegalArgumentException {
        return java.lang.Math.tanh(x);
    }

    @Override
    public MathFunction differentiate() {
        return new CoshFunction().sqr().inverse();
    }
}
