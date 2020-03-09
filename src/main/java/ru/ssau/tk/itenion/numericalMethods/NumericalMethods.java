package ru.ssau.tk.itenion.numericalMethods;

import Jama.Matrix;
import ru.ssau.tk.itenion.functions.MathFunction;
import ru.ssau.tk.itenion.functions.Variable;
import ru.ssau.tk.itenion.functions.multipleVariablesFunctions.vectorFunctions.VMF;
import ru.ssau.tk.itenion.operations.DifferentialOperator;
import ru.ssau.tk.itenion.operations.MathFunctionDifferentialOperator;
import ru.ssau.tk.itenion.operations.MiddleSteppingDifferentialOperator;
import ru.ssau.tk.itenion.ui.ConnectableItem;
import ru.ssau.tk.itenion.ui.Item;

import java.util.HashMap;
import java.util.Map;

@ConnectableItem(name = "", type = Item.NUMERICAL_METHOD)
public class NumericalMethods {
    private static final int NUMBER_OF_SEGMENT_SPLITS = 1001;
    private final int dim = Variable.values().length;
    double left, right, eps;
    Matrix initialApproximation = new Matrix(dim, dim);

    public NumericalMethods(Double left, Double right, double initialApproximation, Double eps) {
        this(left, right, new double[]{initialApproximation}, eps);
    }

    public NumericalMethods(Double left, Double right, double[] initialApproximation, Double eps) {
        this.left = left;
        this.right = right;
        for (int i = 0; i < dim; i++)
            this.initialApproximation.set(0, i, initialApproximation[i]);
        this.eps = eps;
    }

    @ConnectableItem(name = "Solve Nonlinear System", type = Item.NUMERICAL_METHOD, priority = 1, forVMF = true)
    public Matrix solveNonlinearSystem(VMF VMF, boolean isModified) {
        final double EPS = 1E-6;
        Matrix x1;
        Matrix x0 = initialApproximation;
        Matrix jacobian = VMF.getJacobiMatrix(x0.transpose());
        Matrix y = VMF.apply(x0.transpose());
        Matrix p = jacobian.solve(y.timesEquals(-1));
        x1 = x0.plus(p);
        while (x0.minus(x1).norm2() > EPS) {
            x0 = x1;
            y = VMF.apply(x0.transpose());
            if (!isModified) {
                jacobian = VMF.getJacobiMatrix(x0.transpose());
            }
            p = jacobian.solve(y.timesEquals(-1));
            x1 = x0.plus(p);
        }
        return x1;
    }

    @ConnectableItem(name = "Half-division method (all roots)", type = Item.NUMERICAL_METHOD, priority = 1)
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

    @ConnectableItem(name = "Half-division method", type = Item.NUMERICAL_METHOD, priority = 2)
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

    public Map<Double, Map.Entry<Double, Integer>> newtonMethod(MathFunction func, DifferentialOperator<MathFunction> differentialOperator) {
        double a, b;
        int numberOfIterations = 0;
        Map<Double, Map.Entry<Double, Integer>> result = new HashMap<>();
        a = initialApproximation.get(0, 0); //задаём начальное приближение
        b = a - func.apply(a) / (differentialOperator.derive(func)).apply(a); // первое приближение
        while (Math.abs(b - a) > eps) {
            a = b;
            b = a - func.apply(a) / (differentialOperator.derive(func)).apply(a);
            numberOfIterations++;
        }
        result.put(b, Map.entry(func.apply(b), numberOfIterations));
        return result;
    }

    @ConnectableItem(name = "Newton method", type = Item.NUMERICAL_METHOD, priority = 3)
    public Map<Double, Map.Entry<Double, Integer>> solveWithNewtonMethod(MathFunction func) {
        return newtonMethod(func, new MathFunctionDifferentialOperator());
    }

    @ConnectableItem(name = "Secant method", type = Item.NUMERICAL_METHOD, priority = 4)
    public Map<Double, Map.Entry<Double, Integer>> solveWithSecantMethod(MathFunction func) {
        return newtonMethod(func, new MiddleSteppingDifferentialOperator(eps));
    }

}
