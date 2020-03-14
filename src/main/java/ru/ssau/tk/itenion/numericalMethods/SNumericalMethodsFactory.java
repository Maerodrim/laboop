package ru.ssau.tk.itenion.numericalMethods;

public class SNumericalMethodsFactory implements NumericalMethodsFactory {

    @Override
    public NumericalMethods create(Double left, Double right, double initialApproximation, Double eps) {
        return new SNumericalMethods(left, right, initialApproximation, eps);
    }

    @Override
    public NumericalMethods create(Double left, Double right, double[] initialApproximation, Double eps) {
        return new SNumericalMethods(left, right, initialApproximation, eps);
    }
}
