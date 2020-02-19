package ru.ssau.tk.sergunin.lab.functions.powerFunctions;

import ru.ssau.tk.sergunin.lab.ui.ConnectableItem;
import ru.ssau.tk.sergunin.lab.ui.Item;

@ConnectableItem(name = "Линейная функция", priority = 11, type = Item.FUNCTION)

public final class IdentityFunction extends PowFunction {
    private static final long serialVersionUID = -2513148644855402989L;

    public IdentityFunction() {
        super(1);
        name = "x";
    }
}
