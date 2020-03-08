package ru.ssau.tk.itenion.functions.exponentialFunctions;

import ru.ssau.tk.itenion.ui.ConnectableItem;
import ru.ssau.tk.itenion.ui.Item;

@ConnectableItem(name = "Экспонента", priority = 102, type = Item.FUNCTION, isAdjacentConstant = true)
public class ExpFunction extends ExponentialFunction {

    private static final long serialVersionUID = -4803949844687254247L;

    public ExpFunction() {
        super(Math.E);
        name = "e^x";
    }
}
