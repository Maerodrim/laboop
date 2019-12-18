package ru.ssau.tk.sergunin.lab.functions;

import ru.ssau.tk.sergunin.lab.alt_ui.Selectable;

@Selectable(name = "Кубический корень", priority = 8)
public class CbrtFunction extends PowFunction {

    public CbrtFunction() {
        super(1 / 3.);
    }

}
