package ru.ssau.tk.sergunin.lab.functions;

import ru.ssau.tk.sergunin.lab.alt_ui.SelectableFunction;

@SelectableFunction(name = "Нуль-функция", priority = 1)
public class ZeroFunction extends ConstantFunction {

    public ZeroFunction() {
        super(0);
    }
}
