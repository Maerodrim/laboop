package ru.ssau.tk.sergunin.lab.functions;

import ru.ssau.tk.sergunin.lab.ui.Item;
import ru.ssau.tk.sergunin.lab.ui.SelectableItem;

@SelectableItem(name = "Линейная функция", priority = 4, type = Item.FUNCTION)
public final class IdentityFunction extends PowFunction implements MathFunction {
    public IdentityFunction() {
        super(1);
    }
}
