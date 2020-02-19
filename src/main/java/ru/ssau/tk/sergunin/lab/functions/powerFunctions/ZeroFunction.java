package ru.ssau.tk.sergunin.lab.functions.powerFunctions;

import ru.ssau.tk.sergunin.lab.ui.ConnectableItem;
import ru.ssau.tk.sergunin.lab.ui.Item;

@ConnectableItem(name = "Нуль-функция", priority = 1, type = Item.FUNCTION)
public class ZeroFunction extends ConstantFunction {

    private static final long serialVersionUID = 4408722946595865659L;

    public ZeroFunction() {
        super(0);
    }
}
