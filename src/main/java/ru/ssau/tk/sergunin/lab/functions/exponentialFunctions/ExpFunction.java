package ru.ssau.tk.sergunin.lab.functions.exponentialFunctions;

import ru.ssau.tk.sergunin.lab.ui.Item;
import ru.ssau.tk.sergunin.lab.ui.ConnectableItem;

@ConnectableItem(name = "Экспонента", priority = 10, type = Item.FUNCTION)
public class ExpFunction extends ExponentialFunction {

    public ExpFunction() {
        super(Math.E);
    }

    @Override
    public String toString(){
        return "e^x";
    }
}
