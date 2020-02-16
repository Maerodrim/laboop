package ru.ssau.tk.sergunin.lab.functions.powerFunctions;

import ru.ssau.tk.sergunin.lab.ui.Item;
import ru.ssau.tk.sergunin.lab.ui.ConnectableItem;

@ConnectableItem(name = "Нуль-функция", priority = 1, type = Item.FUNCTION)
public class ZeroFunction extends ConstantFunction {

    public ZeroFunction() {
        super(0);
    }

    @Override
    public String toString(){
        return 0 + "";
    }
}
