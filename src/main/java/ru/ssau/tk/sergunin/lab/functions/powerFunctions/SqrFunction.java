package ru.ssau.tk.sergunin.lab.functions.powerFunctions;

import ru.ssau.tk.sergunin.lab.ui.ConnectableItem;
import ru.ssau.tk.sergunin.lab.ui.Item;

@ConnectableItem(name = "Квадратичная функция", priority = 14, type = Item.FUNCTION)
public class SqrFunction extends PowFunction {

    private static final long serialVersionUID = -2272708426467520428L;

    public SqrFunction() {
        super(2);
        name = "x^2";
    }
}
