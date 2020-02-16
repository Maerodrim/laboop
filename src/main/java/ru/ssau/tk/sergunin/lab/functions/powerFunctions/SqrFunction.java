package ru.ssau.tk.sergunin.lab.functions.powerFunctions;

import ru.ssau.tk.sergunin.lab.ui.Item;
import ru.ssau.tk.sergunin.lab.ui.ConnectableItem;

@ConnectableItem(name = "Квадратичная функция", priority = 5, type = Item.FUNCTION)
public class SqrFunction extends PowFunction {

    public SqrFunction() {
        super(2);
    }

    @Override
    public String toString(){
        return "x^2";
    }
}
