package ru.ssau.tk.sergunin.lab.operations;

import ru.ssau.tk.sergunin.lab.functions.MathFunction;
import ru.ssau.tk.sergunin.lab.ui.ConnectableItem;
import ru.ssau.tk.sergunin.lab.ui.Item;

@ConnectableItem(name = "", priority = 5, type = Item.OPERATOR)
public class MathFunctionDifferentialOperator implements DifferentialOperator<MathFunction> {

    public MathFunctionDifferentialOperator() {
    }

    @Override
    @ConnectableItem(name = "Derive", priority = 6, type = Item.OPERATOR, numericalOperator = false)
    public MathFunction derive(MathFunction function) {
        return function.differentiate();
    }
}
