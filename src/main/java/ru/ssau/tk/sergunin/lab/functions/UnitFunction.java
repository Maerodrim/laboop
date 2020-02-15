package ru.ssau.tk.sergunin.lab.functions;

import ru.ssau.tk.sergunin.lab.ui.Item;
import ru.ssau.tk.sergunin.lab.ui.ConnectableItem;

@ConnectableItem(name = "Единичная функция", priority = 2, type = Item.FUNCTION)
public class UnitFunction extends ConstantFunction {

    public UnitFunction() {
        super(1.);
    }
}
