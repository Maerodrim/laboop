package ru.ssau.tk.itenion.labNumericalMethods.lab1.factory;

import ru.ssau.tk.itenion.labNumericalMethods.lab1.MSolveNonlinearEquations;
import ru.ssau.tk.itenion.labNumericalMethods.lab1.SolveNonlinearEquations;

public class MNumericalMethodsFactory implements NumericalMethodsFactory {

    @Override
    public SolveNonlinearEquations create(Double left, Double right, double initialApproximation, Double eps) {
        return new MSolveNonlinearEquations(left, right, initialApproximation, eps);
    }

    @Override
    public SolveNonlinearEquations create(Double left, Double right, double[] initialApproximation, Double eps) {
        return new MSolveNonlinearEquations(left, right, initialApproximation, eps);
    }
}
