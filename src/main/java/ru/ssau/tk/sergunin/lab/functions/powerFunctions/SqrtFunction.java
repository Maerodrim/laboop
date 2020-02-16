package ru.ssau.tk.sergunin.lab.functions.powerFunctions;

import ru.ssau.tk.sergunin.lab.ui.Item;
import ru.ssau.tk.sergunin.lab.ui.ConnectableItem;

@ConnectableItem(name = "Квадратный корень", priority = 7, type = Item.FUNCTION)
public class SqrtFunction extends PowFunction {

    public SqrtFunction() {
        super(1 / 2.);
    }

    @Override
    public String toString(){
        return "x^(0.5)";
    }
}
