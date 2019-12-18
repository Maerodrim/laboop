package ru.ssau.tk.sergunin.lab.functions;

import ru.ssau.tk.sergunin.lab.alt_ui.Selectable;

@Selectable(name = "Квадратный корень", priority = 7)
public class SqrtFunction extends PowFunction {

    public SqrtFunction() {
        super(1 / 2.);
    }

}
