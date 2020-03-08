package ru.ssau.tk.itenion.functions.powerFunctions;

import ru.ssau.tk.itenion.ui.ConnectableItem;
import ru.ssau.tk.itenion.ui.Item;

@ConnectableItem(name = "Линейная функция", priority = 11, type = Item.FUNCTION)

public final class IdentityFunction extends IntegerPowFunction {
    private static final long serialVersionUID = -2513148644855402989L;

    public IdentityFunction() {
        super(1);
        name = "x";
    }

    @Override
    public String getName(){
        return name;
    }
}
