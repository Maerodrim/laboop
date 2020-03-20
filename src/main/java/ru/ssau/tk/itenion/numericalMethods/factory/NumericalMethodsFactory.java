package ru.ssau.tk.itenion.numericalMethods.factory;

import ru.ssau.tk.itenion.numericalMethods.NumericalMethods;

public interface NumericalMethodsFactory {
    NumericalMethods create(Double left, Double right, double initialApproximation, Double eps);

    NumericalMethods create(Double left, Double right, double[] initialApproximation, Double eps);
}
