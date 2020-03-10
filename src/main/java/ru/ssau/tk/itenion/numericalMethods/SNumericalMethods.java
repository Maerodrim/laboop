package ru.ssau.tk.itenion.numericalMethods;

import Jama.Matrix;
import ru.ssau.tk.itenion.enums.Variable;
import ru.ssau.tk.itenion.functions.MathFunction;
import ru.ssau.tk.itenion.functions.multipleVariablesFunctions.vectorFunctions.VMF;
import ru.ssau.tk.itenion.operations.DifferentialOperator;
import ru.ssau.tk.itenion.operations.MathFunctionDifferentialOperator;
import ru.ssau.tk.itenion.ui.ConnectableItem;
import ru.ssau.tk.itenion.ui.Item;

import java.util.HashMap;
import java.util.Map;

@ConnectableItem(name = "Stanislav", type = Item.NUMERICAL_METHOD)
public class SNumericalMethods {
    private static final int NUMBER_OF_SEGMENT_SPLITS = 1001;
    double left, right, eps;
    Matrix initialApproximation;
    private int iterationsNumber = 0;
    private int dim = Variable.values().length;

    public SNumericalMethods(Double left, Double right, double initialApproximation, Double eps) {
        this(left, right, new double[]{initialApproximation}, eps);
    }

    public SNumericalMethods(Double left, Double right, double[] initialApproximation, Double eps) {
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

    @ConnectableItem(name = "Half-division method all roots", type = Item.NUMERICAL_METHOD, priority = 1)
    public Map<Double, Map.Entry<Double, Integer>> solveWithHalfDivisionMethodAllRoots(MathFunction func) {
        double x = left, step;
        Map<Double, Map.Entry<Double, Integer>> result = new HashMap<>();
        step = (right - left) / NUMBER_OF_SEGMENT_SPLITS;
        while ((x + step) < right) {
            if (func.apply(x) * func.apply(x + step) <= 0) {
                solveWithHalfDivisionMethod(func, result, x, x + step);
            }
            x = x + step;
        }
        return result;
    }

    @ConnectableItem(name = "Half-division method Stas", type = Item.NUMERICAL_METHOD, priority = 2)
    public Map<Double, Map.Entry<Double, Integer>> solveWithHalfDivisionMethod(MathFunction func) {
        Map<Double, Map.Entry<Double, Integer>> map = new HashMap<>();
        solveWithHalfDivisionMethod(func, map, left, right);
        return map;
    }

    public void solveWithHalfDivisionMethod(MathFunction func, Map<Double, Map.Entry<Double, Integer>> map, double a, double b) {
        int numberOfIterations = 0;
        while (Math.abs(a - b) > 2 * eps) {
            if (func.apply(a) * func.apply(0.5 * (a + b)) <= 0) {
                b = 0.5 * (a + b);
            } else {
                a = 0.5 * (a + b);
            }
            numberOfIterations++;
        }
        map.put((a + b) / 2, Map.entry(func.apply((a + b) / 2), numberOfIterations));
    }

    @ConnectableItem(name = "Newton method Stas", type = Item.NUMERICAL_METHOD, priority = 3)
    public Map<Double, Map.Entry<Double, Integer>> solveWithNewtonMethod(MathFunction func) {
        return newtonMethod(func, new MathFunctionDifferentialOperator(), false);
    }

    @ConnectableItem(name = "Modified newton method", type = Item.NUMERICAL_METHOD, priority = 5)
    public Map<Double, Map.Entry<Double, Integer>> solveWithModifiedNewtonMethod(MathFunction func) {
        return newtonMethod(func, new MathFunctionDifferentialOperator(), true);
    }

    public Map<Double, Map.Entry<Double, Integer>> newtonMethod(MathFunction func, DifferentialOperator<MathFunction> differentialOperator, boolean isModified) {
        double a, b;
        int numberOfIterations = 0;
        Map<Double, Map.Entry<Double, Integer>> result = new HashMap<>();
        a = initialApproximation.get(0, 0); //задаём начальное приближение
        double diff = (differentialOperator.derive(func)).apply(a);
        b = a - func.apply(a) / diff; // первое приближение
        while (Math.abs(b - a) > eps) {
            a = b;
            if (!isModified) {
                diff = (differentialOperator.derive(func)).apply(a);
            }
            b = a - func.apply(a) / diff;
            numberOfIterations++;
        }
        result.put(b, Map.entry(func.apply(b), numberOfIterations));
        return result;
    }

    @ConnectableItem(name = "Modified Newton method Stas", type = Item.NUMERICAL_METHOD, priority = 3, forVMF = true)
    public Matrix solveNonlinearSystemWithNewtonMethod2(VMF VMF) {
        Matrix a, b = initialApproximation, p = VMF.getJacobiMatrix(b.transpose()).solve(VMF.apply(b.transpose()).timesEquals(-1));
        Matrix J = VMF.getJacobiMatrix(b.transpose());
        a = b.plus(p);
        for (; eps <= b.minus(a).norm2(); ) {
            b = a;
            p = J.solve(VMF.apply(a.transpose()).timesEquals(-1));
            a = b.plus(p);
            iterationsNumber++;
        }
        return a;
    }

    @ConnectableItem(name = "Newton method Stas", type = Item.NUMERICAL_METHOD, priority = 4, forVMF = true)
    public Matrix solveNonlinearSystemWithModifiedNewtonMethod2(VMF VMF) {
        Matrix a, b = initialApproximation, p = VMF.getJacobiMatrix(b.transpose()).solve(VMF.apply(b.transpose()).timesEquals(-1));
        a = b.plus(p);
        for (; eps <= b.minus(a).norm2(); ) {
            b = a;
            p = VMF.getJacobiMatrix(b.transpose()).solve(VMF.apply(a.transpose()).timesEquals(-1));
            a = b.plus(p);
            iterationsNumber++;
        }
        return a;
    }

    public int getIterationsNumber() {
        return iterationsNumber;
    }
}