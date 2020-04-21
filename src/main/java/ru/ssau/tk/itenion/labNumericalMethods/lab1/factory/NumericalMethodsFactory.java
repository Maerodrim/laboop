package ru.ssau.tk.itenion.labNumericalMethods.lab1.factory;

import ru.ssau.tk.itenion.labNumericalMethods.lab1.SolveNonlinearEquations;

public interface NumericalMethodsFactory {
    SolveNonlinearEquations create(Double left, Double right, double initialApproximation, Double eps);

    SolveNonlinearEquations create(Double left, Double right, double[] initialApproximation, Double eps);
}
