package ru.ssau.tk.sergunin.lab.functions;

import ru.ssau.tk.sergunin.lab.alt_ui.Selectable;

@Selectable(name = "Степенная функция", priority = 6, parameter = true)
public class PowFunction implements MathFunction {
    private final double pow;

    public PowFunction(double pow) {
        this.pow = pow;
    }

    @Override
    public double apply(double x) {
        return Math.pow(x, pow);
    }
}
