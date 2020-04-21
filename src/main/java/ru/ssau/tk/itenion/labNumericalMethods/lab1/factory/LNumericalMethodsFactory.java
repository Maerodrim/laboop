package ru.ssau.tk.itenion.labNumericalMethods.lab1.factory;

import ru.ssau.tk.itenion.labNumericalMethods.lab1.LSolveNonlinearEquations;
import ru.ssau.tk.itenion.labNumericalMethods.lab1.SolveNonlinearEquations;

public class LNumericalMethodsFactory implements NumericalMethodsFactory {
    @Override
    public SolveNonlinearEquations create(Double left, Double right, double initialApproximation, Double eps) {
        return new LSolveNonlinearEquations(left, right, initialApproximation, eps);
    }

    @Override
    public SolveNonlinearEquations create(Double left, Double right, double[] initialApproximation, Double eps) {
        return new LSolveNonlinearEquations(left, right, initialApproximation, eps);
    }
}
