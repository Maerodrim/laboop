package ru.ssau.tk.sergunin.lab.functions.powerFunctions;

import ru.ssau.tk.sergunin.lab.ui.Item;
import ru.ssau.tk.sergunin.lab.ui.ConnectableItem;

@ConnectableItem(name = "Линейная функция", priority = 4, type = Item.FUNCTION)

public final class IdentityFunction extends PowFunction{
    public IdentityFunction() {
        super(1);
    }

    @Override
    public String toString(){
        return "x";
    }
}
