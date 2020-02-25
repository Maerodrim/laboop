package ru.ssau.tk.itenion.functions.exponentialFunctions;

import ru.ssau.tk.itenion.functions.AbstractMathFunction;
import ru.ssau.tk.itenion.functions.MathFunction;
import ru.ssau.tk.itenion.ui.ConnectableItem;
import ru.ssau.tk.itenion.ui.Item;

@ConnectableItem(name = "Показательная функция", priority = 101, type = Item.FUNCTION, hasParameter = true)
public class ExponentialFunction extends AbstractMathFunction implements MathFunction {
    private static final long serialVersionUID = 8190276995050776178L;
    private final double exp;

    public ExponentialFunction(double exp) {
        this.exp = exp;
        name = exp + "^x";
    }

    @Override
    public double apply(double x) {
        if (exp < 0 && (int) x != x) {
            throw new UnsupportedOperationException();
        }
        return Math.pow(exp, x);
    }

    @Override
    public MathFunction differentiate() {
        return this.multiply(Math.log(exp));
    }
}