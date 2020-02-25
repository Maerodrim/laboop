package ru.ssau.tk.itenion.functions.powerFunctions;

import ru.ssau.tk.itenion.ui.ConnectableItem;
import ru.ssau.tk.itenion.ui.Item;

@ConnectableItem(name = "Единичная функция", priority = 2, type = Item.FUNCTION)
public class UnitFunction extends ConstantFunction {

    private static final long serialVersionUID = 8718835720490187167L;

    public UnitFunction() {
        super(1.);
    }
}
