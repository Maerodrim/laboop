package ru.ssau.tk.itenion.numericalMethods;

public class VNumericalMethodsFactory implements NumericalMethodsFactory {
    @Override
    public NumericalMethods create(Double left, Double right, double initialApproximation, Double eps) {
        return new VNumericalMethods(left, right, initialApproximation, eps);
    }

    @Override
    public NumericalMethods create(Double left, Double right, double[] initialApproximation, Double eps) {
        return new VNumericalMethods(left, right, initialApproximation, eps);
    }
}
