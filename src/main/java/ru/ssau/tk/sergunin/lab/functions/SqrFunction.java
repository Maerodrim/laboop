package ru.ssau.tk.sergunin.lab.functions;

import ru.ssau.tk.sergunin.lab.alt_ui.SelectableFunction;

@SelectableFunction(name = "Квадратичная функция", priority = 5)
public class SqrFunction extends PowFunction {

    public SqrFunction() {
        super(2);
    }

}
