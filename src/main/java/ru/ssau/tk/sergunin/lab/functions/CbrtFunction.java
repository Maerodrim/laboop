package ru.ssau.tk.sergunin.lab.functions;

import ru.ssau.tk.sergunin.lab.alt_ui.SelectableFunction;

@SelectableFunction(name = "Кубический корень", priority = 8)
public class CbrtFunction extends PowFunction {

    public CbrtFunction() {
        super(1 / 3.);
    }

}
