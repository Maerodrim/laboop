package ru.ssau.tk.itenion.numericalMethods.factory;

import ru.ssau.tk.itenion.numericalMethods.NumericalMethods;
import ru.ssau.tk.itenion.numericalMethods.LNumericalMethods;

public class LNumericalMethodsFactory implements NumericalMethodsFactory {
    @Override
    public NumericalMethods create(Double left, Double right, double initialApproximation, Double eps) {
        return new LNumericalMethods(left, right, initialApproximation, eps);
    }

    @Override
    public NumericalMethods create(Double left, Double right, double[] initialApproximation, Double eps) {
        return new LNumericalMethods(left, right, initialApproximation, eps);
    }
}
