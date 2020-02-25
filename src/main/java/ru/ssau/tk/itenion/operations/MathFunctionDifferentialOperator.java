package ru.ssau.tk.itenion.operations;

import ru.ssau.tk.itenion.functions.MathFunction;
import ru.ssau.tk.itenion.ui.ConnectableItem;
import ru.ssau.tk.itenion.ui.Item;

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
