package ru.ssau.tk.sergunin.lab.functions;

import ru.ssau.tk.sergunin.lab.ui.Item;
import ru.ssau.tk.sergunin.lab.ui.SelectableItem;

@SelectableItem(name = "Квадратичная функция", priority = 5, type = Item.FUNCTION)
public class SqrFunction extends PowFunction {

    public SqrFunction() {
        super(2);
    }

}
