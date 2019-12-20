package ru.ssau.tk.sergunin.lab.functions;

import ru.ssau.tk.sergunin.lab.alt_ui.Selectable;

@Selectable(name = "Тождественная функция", priority = 4)
public final class IdentityFunction extends PowFunction implements MathFunction {
    public IdentityFunction() {
        super(1);
    }
}
