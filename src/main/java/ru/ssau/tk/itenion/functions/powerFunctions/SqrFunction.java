package ru.ssau.tk.itenion.functions.powerFunctions;

import ru.ssau.tk.itenion.ui.ConnectableItem;
import ru.ssau.tk.itenion.ui.Item;

@ConnectableItem(name = "Квадратичная функция", priority = 15, type = Item.FUNCTION)
public class SqrFunction extends IntegerPowFunction {

    private static final long serialVersionUID = -2272708426467520428L;

    public SqrFunction() {
        super(2);
        name = "x^2";
    }
}
