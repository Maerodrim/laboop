package ru.ssau.tk.itenion.numericalMethods.factory;

import ru.ssau.tk.itenion.numericalMethods.MNumericalMethods;
import ru.ssau.tk.itenion.numericalMethods.NumericalMethods;

public class MNumericalMethodsFactory implements NumericalMethodsFactory {

    @Override
    public NumericalMethods create(Double left, Double right, double initialApproximation, Double eps) {
        return new MNumericalMethods(left, right, initialApproximation, eps);
    }

    @Override
    public NumericalMethods create(Double left, Double right, double[] initialApproximation, Double eps) {
        return new MNumericalMethods(left, right, initialApproximation, eps);
    }
}
