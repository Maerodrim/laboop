package ru.ssau.tk.itenion.numericalMethods;

public interface NumericalMethodsFactory {
    NumericalMethods create(Double left, Double right, double initialApproximation, Double eps);

    NumericalMethods create(Double left, Double right, double[] initialApproximation, Double eps);
}
