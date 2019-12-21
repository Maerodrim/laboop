package ru.ssau.tk.sergunin.lab.functions;

import ru.ssau.tk.sergunin.lab.ui.Item;
import ru.ssau.tk.sergunin.lab.ui.SelectableItem;

@SelectableItem(name = "Кубический корень", priority = 8, type = Item.FUNCTION)
public class CbrtFunction extends PowFunction {

    public CbrtFunction() {
        super(1 / 3.);
    }

}
