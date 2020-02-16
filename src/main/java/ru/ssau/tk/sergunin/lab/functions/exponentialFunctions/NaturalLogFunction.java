package ru.ssau.tk.sergunin.lab.functions.exponentialFunctions;

import ru.ssau.tk.sergunin.lab.ui.ConnectableItem;
import ru.ssau.tk.sergunin.lab.ui.Item;

@ConnectableItem(name = "Натуральный логарифм", priority = 19, type = Item.FUNCTION)
public class NaturalLogFunction extends LogFunction {
    public NaturalLogFunction() {
        super(Math.E);
    }

    @Override
    public String toString(){
        return "ln(x)";
    }
}
