package ru.ssau.tk.sergunin.lab.functions.powerFunctions;

import ru.ssau.tk.sergunin.lab.ui.Item;
import ru.ssau.tk.sergunin.lab.ui.ConnectableItem;

@ConnectableItem(name = "Кубический корень", priority = 8, type = Item.FUNCTION)
public class CbrtFunction extends PowFunction {

    public CbrtFunction() {
        super(1 / 3.);
    }

    @Override
    public String toString(){
        return "x^(0.33333)";
    }
}
