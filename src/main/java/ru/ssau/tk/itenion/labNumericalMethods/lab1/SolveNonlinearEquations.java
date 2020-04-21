package ru.ssau.tk.itenion.labNumericalMethods.lab1;

import Jama.Matrix;
import ru.ssau.tk.itenion.enums.Variable;
import ru.ssau.tk.itenion.functions.multipleVariablesFunctions.vectorFunctions.VMF;
import ru.ssau.tk.itenion.ui.ConnectableItem;
import ru.ssau.tk.itenion.ui.Item;

@ConnectableItem(name = "", type = Item.SOLVE_NUMERICAL_METHOD)
public abstract class SolveNonlinearEquations {
    protected double left, right, eps;
    protected Matrix initialApproximation;
    protected int iterationsNumber = 0;
    protected int dim = Variable.values().length;

    public SolveNonlinearEquations(Double left, Double right, double initialApproximation, Double eps) {
        this(left, right, new double[]{initialApproximation}, eps);
    }

    public SolveNonlinearEquations(Double left, Double right, double[] initialApproximation, Double eps) {
        if (initialApproximation.length == 1) {
            dim = 1;
        }
        this.initialApproximation = new Matrix(dim, 1);
        this.left = left;
        this.right = right;
        for (int i = 0; i < dim; i++)
            this.initialApproximation.set(i, 0, initialApproximation[i]);
        this.eps = eps;
    }

    public int getIterationsNumber() {
        return iterationsNumber;
    }

    public abstract Matrix solveNonlinearSystemWithNewtonMethod(VMF VMF);
}
