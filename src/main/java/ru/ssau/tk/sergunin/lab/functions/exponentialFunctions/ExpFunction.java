package ru.ssau.tk.sergunin.lab.functions.exponentialFunctions;

import ru.ssau.tk.sergunin.lab.ui.ConnectableItem;
import ru.ssau.tk.sergunin.lab.ui.Item;

@ConnectableItem(name = "Экспонента", priority = 102, type = Item.FUNCTION)
public class ExpFunction extends ExponentialFunction {

    private static final long serialVersionUID = -4803949844687254247L;

    public ExpFunction() {
        super(Math.E);
        name = "e^x";
    }
}
