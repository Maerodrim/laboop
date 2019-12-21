package ru.ssau.tk.sergunin.lab.functions;

import ru.ssau.tk.sergunin.lab.ui.SelectableFunction;

@SelectableFunction(name = "Единичная функция", priority = 2)
public class UnitFunction extends ConstantFunction {

    public UnitFunction() {
        super(1.);
    }
}
