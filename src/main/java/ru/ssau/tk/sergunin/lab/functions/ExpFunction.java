package ru.ssau.tk.sergunin.lab.functions;

import ru.ssau.tk.sergunin.lab.ui.SelectableFunction;

@SelectableFunction(name = "Экспонента", priority = 10)
public class ExpFunction extends ExponentialFunction {

    public ExpFunction() {
        super(Math.E);
    }
}
