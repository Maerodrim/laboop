package ru.ssau.tk.itenion.functions.powerFunctions;

import ru.ssau.tk.itenion.ui.ConnectableItem;
import ru.ssau.tk.itenion.ui.Item;

@ConnectableItem(name = "Кубический корень", priority = 17, type = Item.FUNCTION)
public class CbrtFunction extends PowFunction {

    private static final long serialVersionUID = 5983111173485824486L;

    public CbrtFunction() {
        super(1 / 3.);
        name = "x^(0.33333)";
    }
}
