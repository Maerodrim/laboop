package ru.ssau.tk.sergunin.lab.functions.hyperbolicFunctions;

import ru.ssau.tk.sergunin.lab.functions.AbstractMathFunction;
import ru.ssau.tk.sergunin.lab.functions.MathFunction;
import ru.ssau.tk.sergunin.lab.ui.ConnectableItem;
import ru.ssau.tk.sergunin.lab.ui.Item;

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
