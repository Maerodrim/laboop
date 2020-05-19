package ru.ssau.tk.itenion.functions;

public interface Differentiable extends Copyable {
    MathFunction differentiate();

    default MathFunction differentiate(int order) {
        MathFunction function = this.copy();
        for (int i = 0; i < order; i++) {
            function = function.differentiate();
        }
        return function;
    }
}
