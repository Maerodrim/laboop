package ru.ssau.tk.sergunin.lab.functions;

import ru.ssau.tk.sergunin.lab.alt_ui.Selectable;

@Selectable(name = "Экспонента", priority = 9, parameter = true)
public class ExpFunction implements MathFunction {
    private final double exp;

    public ExpFunction(double exp) {
        this.exp = exp;
    }

    public ExpFunction() {
        this(Math.E);
    }

    @Override
    public double apply(double x) {
        if (exp < 0 && (int) x != x) {
            throw new UnsupportedOperationException();
        }
        return Math.pow(exp, x);
    }
}
