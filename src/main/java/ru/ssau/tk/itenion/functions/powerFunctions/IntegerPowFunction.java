package ru.ssau.tk.itenion.functions.powerFunctions;

import ru.ssau.tk.itenion.exceptions.FractionalPowerException;
import ru.ssau.tk.itenion.functions.MathFunction;
import ru.ssau.tk.itenion.ui.ConnectableItem;
import ru.ssau.tk.itenion.ui.Item;

@ConnectableItem(name = "Целостепенная функция", priority = 13, type = Item.FUNCTION, hasParameter = true)
public class IntegerPowFunction extends PowFunction {
    private static final long serialVersionUID = -8140593473469820529L;
    private final int pow;

    public IntegerPowFunction(double pow) {
        super(pow);
        if ((int) pow != pow) {
            throw new FractionalPowerException();
        }
        this.pow = (int) super.pow;
    }

    @Override
    public MathFunction differentiate() {
        return pow != 0 ? new IntegerPowFunction(pow - 1).multiply(pow) : new ZeroFunction();
    }


    public Number getPow() {
        return pow;
    }
}
