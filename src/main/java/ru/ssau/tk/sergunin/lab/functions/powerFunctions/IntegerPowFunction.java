package ru.ssau.tk.sergunin.lab.functions.powerFunctions;

import ru.ssau.tk.sergunin.lab.exceptions.FractionalPowerException;
import ru.ssau.tk.sergunin.lab.functions.MathFunction;
import ru.ssau.tk.sergunin.lab.ui.ConnectableItem;
import ru.ssau.tk.sergunin.lab.ui.Item;

@ConnectableItem(name = "Целостепенная функция", priority = 13, type = Item.FUNCTION, hasParameter = true)
public class IntegerPowFunction extends PowFunction {
    private static final long serialVersionUID = -8140593473469820529L;
    private final int pow;

    public IntegerPowFunction(double pow) {
        super(pow);
        if ((int)pow != pow) {
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
