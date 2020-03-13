package ru.ssau.tk.itenion.functions.powerFunctions;

import ru.ssau.tk.itenion.ui.ConnectableItem;
import ru.ssau.tk.itenion.ui.Item;

@ConnectableItem(name = "Квадратный корень", priority = 16, type = Item.FUNCTION, isAdjacentConstant = true)
public class SqrtFunction extends PowFunction {

    private static final long serialVersionUID = -8606264259650325037L;

    public SqrtFunction() {
        super(1 / 2.);
        name = "x^(0.5)";
    }
}
