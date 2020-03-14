package ru.ssau.tk.itenion.numericalMethods;

import Jama.Matrix;
import ru.ssau.tk.itenion.enums.Variable;
import ru.ssau.tk.itenion.ui.ConnectableItem;
import ru.ssau.tk.itenion.ui.Item;

@ConnectableItem(name = "", type = Item.NUMERICAL_METHOD)
public abstract class NumericalMethods {
    protected double left, right, eps;
    protected Matrix initialApproximation;
    protected int iterationsNumber = 0;
    protected int dim = Variable.values().length;

    public NumericalMethods(Double left, Double right, double initialApproximation, Double eps) {
        this(left, right, new double[]{initialApproximation}, eps);
    }

    public NumericalMethods(Double left, Double right, double[] initialApproximation, Double eps) {
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
}
