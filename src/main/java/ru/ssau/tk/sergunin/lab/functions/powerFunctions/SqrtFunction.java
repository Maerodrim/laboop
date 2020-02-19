package ru.ssau.tk.sergunin.lab.functions.powerFunctions;

import ru.ssau.tk.sergunin.lab.ui.ConnectableItem;
import ru.ssau.tk.sergunin.lab.ui.Item;

@ConnectableItem(name = "Квадратный корень", priority = 15, type = Item.FUNCTION)
public class SqrtFunction extends PowFunction {

    private static final long serialVersionUID = -8606264259650325037L;

    public SqrtFunction() {
        super(1 / 2.);
        name = "x^(0.5)";
    }
}
