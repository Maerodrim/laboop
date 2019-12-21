package ru.ssau.tk.sergunin.lab.functions;

import ru.ssau.tk.sergunin.lab.ui.Item;
import ru.ssau.tk.sergunin.lab.ui.SelectableItem;

@SelectableItem(name = "Квадратный корень", priority = 7, type = Item.FUNCTION)
public class SqrtFunction extends PowFunction {

    public SqrtFunction() {
        super(1 / 2.);
    }

}
