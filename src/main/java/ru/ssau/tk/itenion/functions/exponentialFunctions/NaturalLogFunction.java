package ru.ssau.tk.itenion.functions.exponentialFunctions;

import ru.ssau.tk.itenion.ui.ConnectableItem;
import ru.ssau.tk.itenion.ui.Item;

@ConnectableItem(name = "Натуральный логарифм", priority = 111, type = Item.FUNCTION)
public class NaturalLogFunction extends LogFunction {
    private static final long serialVersionUID = 6097993488536679277L;

    public NaturalLogFunction() {
        super(Math.E);
        name = "ln(x)";
    }
}
