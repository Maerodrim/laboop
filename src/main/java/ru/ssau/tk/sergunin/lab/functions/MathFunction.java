package ru.ssau.tk.sergunin.lab.functions;

@FunctionalInterface
public interface MathFunction {
    double apply(double x);

    default CompositeFunction andThen(MathFunction afterFunction) {
        return new CompositeFunction(this, afterFunction);
    }
}
