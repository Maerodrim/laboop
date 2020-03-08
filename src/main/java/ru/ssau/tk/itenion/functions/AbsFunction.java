package ru.ssau.tk.itenion.functions;

import ru.ssau.tk.itenion.exceptions.FunctionIsNotDifferentiableException;
import ru.ssau.tk.itenion.functions.powerFunctions.IdentityFunction;
import ru.ssau.tk.itenion.ui.ConnectableItem;
import ru.ssau.tk.itenion.ui.Item;

@ConnectableItem(name = "Модуль", priority = 141, type = Item.FUNCTION)
public class AbsFunction extends AbstractMathFunction {
    private static final long serialVersionUID = 1501965212346444671L;

    public AbsFunction() {
        name = "|x|";
    }

    @Override
    public double apply(double x) {
        return Math.abs(new IdentityFunction().apply(x));
    }

    @Override
    public MathFunction differentiate() {
        throw new FunctionIsNotDifferentiableException();
    }
}
