package ru.ssau.tk.itenion.functions.powerFunctions;

import ru.ssau.tk.itenion.functions.MathFunction;
import ru.ssau.tk.itenion.ui.ConnectableItem;
import ru.ssau.tk.itenion.ui.Item;

@ConnectableItem(name = "Целостепенная функция", priority = 13, type = Item.FUNCTION, hasParameter = true, parameterInstance = Integer.class)
public class IntegerPowFunction extends AbstractPowFunction<Integer> {
    private static final long serialVersionUID = -8140593473469820529L;

    public IntegerPowFunction() {
        this(0);
    }

    public IntegerPowFunction(Integer pow) {
        super(pow);
    }

    @Override
    public double apply(double x) {
        return Math.pow(x, pow);
    }

    @Override
    public MathFunction differentiate() {
        return pow != 0 ? new IntegerPowFunction(pow - 1).multiply(pow) : new ZeroFunction();
    }

    @Override
    public String getName() {
        return "x^" + pow;
    }
}
