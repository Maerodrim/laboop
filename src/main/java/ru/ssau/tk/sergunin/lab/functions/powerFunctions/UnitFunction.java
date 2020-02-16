package ru.ssau.tk.sergunin.lab.functions.powerFunctions;

import ru.ssau.tk.sergunin.lab.ui.Item;
import ru.ssau.tk.sergunin.lab.ui.ConnectableItem;

@ConnectableItem(name = "Единичная функция", priority = 2, type = Item.FUNCTION)
public class UnitFunction extends ConstantFunction {

    public UnitFunction() {
        super(1.);
    }

    @Override
    public String toString(){
        return 1 + "";
    }
}
