package ru.ssau.tk.itenion.numericalMethods;

import Jama.Matrix;
import ru.ssau.tk.itenion.functions.MathFunction;
import ru.ssau.tk.itenion.functions.multipleVariablesFunctions.vectorFunctions.VMF;
import ru.ssau.tk.itenion.functions.multipleVariablesFunctions.vectorFunctions.VMFSV;
import ru.ssau.tk.itenion.operations.DifferentialOperator;
import ru.ssau.tk.itenion.operations.MathFunctionDifferentialOperator;
import ru.ssau.tk.itenion.operations.MiddleSteppingDifferentialOperator;
import ru.ssau.tk.itenion.ui.AlertWindows;
import ru.ssau.tk.itenion.ui.ConnectableItem;
import ru.ssau.tk.itenion.ui.Item;

import java.util.HashMap;
import java.util.Map;

@ConnectableItem(name = "", type = Item.NUMERICAL_METHOD)
public class LNumericalMethods extends NumericalMethods {
    public LNumericalMethods(Double left, Double right, double initialApproximation, Double eps) {
        super(left, right, initialApproximation, eps);
    }

    public LNumericalMethods(Double left, Double right, double[] initialApproximation, Double eps) {
        super(left, right, initialApproximation, eps);
    }

    @ConnectableItem(name = "Newton method", type = Item.NUMERICAL_METHOD, priority = 1, forVMF = true)
    public Matrix solveNonlinearSystemWithNewtonMethod(VMF VMF) {
        return solveNonlinearSystem(VMF, false);
    }

    @ConnectableItem(name = "Modified newton method", type = Item.NUMERICAL_METHOD, priority = 2, forVMF = true)
    public Matrix solveNonlinearSystemWithModifiedNewtonMethod(VMF VMF) {
        return solveNonlinearSystem(VMF, true);
    }

    public int getIterationsNumber() {
        return iterationsNumber;
    }

    public Matrix solveNonlinearSystem(VMF VMF, boolean isModified) {
        Matrix x_next = null;
        String warning = "The method diverges for this initial approximation. Choose a different initial approximation";
        try {
            Matrix x_previous;
            Matrix x_current = initialApproximation;
            Matrix jacobian = VMF.getJacobiMatrix(x_current.transpose());
            Matrix y = VMF.apply(x_current.transpose());
            Matrix p = jacobian.solve(y.timesEquals(-1));
            x_next = x_current.plus(p);
            iterationsNumber++;
            while (x_current.minus(x_next).norm2() > eps) {
                x_previous = x_current;
                x_current = x_next;
                y = VMF.apply(x_current.transpose());
                if (!isModified) {
                    jacobian = VMF.getJacobiMatrix(x_current.transpose());
                }
                p = jacobian.solve(y.timesEquals(-1));
                x_next = x_current.plus(p);
                iterationsNumber++;
                if (x_current.minus(x_next).norm2() > x_previous.minus(x_current).norm2()) {
                    throw new RuntimeException(warning);
                }
            }
        } catch (RuntimeException e) {
            if (e.getMessage().equals("Matrix is singular.")) {
                AlertWindows.showWarning("Matrix is singular. Choose a different initial approximation");
            } else if (e.getMessage().equals(warning)) {
                AlertWindows.showWarning(warning);
            } else {
                AlertWindows.showError(e);
            }
        }
        return x_next;
    }

    @ConnectableItem(name = "Half-division method", type = Item.NUMERICAL_METHOD, priority = 6, isBorderRequired = true)
    public Map<Double, Map.Entry<Double, Integer>> solveWithHalfDivisionMethod(MathFunction func) {
        Map<Double, Map.Entry<Double, Integer>> map = new HashMap<>();
        int numberOfIterations = 0;
        while (Math.abs(left - right) > 2 * eps) {
            if (func.apply(left) * func.apply(0.5 * (left + right)) <= 0) {
                right = 0.5 * (left + right);
            } else {
                left = 0.5 * (left + right);
            }
            numberOfIterations++;
        }
        map.put((left + right) / 2, Map.entry(func.apply((left + right) / 2), numberOfIterations));
        return map;
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

    @ConnectableItem(name = "Secant method", type = Item.NUMERICAL_METHOD, priority = 4)
    public Map<Double, Map.Entry<Double, Integer>> solveWithSecantMethod(MathFunction func) {
        return newtonMethod(func, new MiddleSteppingDifferentialOperator(eps), false);
    }

    @ConnectableItem(name = "Modified Newton method", type = Item.NUMERICAL_METHOD, priority = 5)
    public Map<Double, Map.Entry<Double, Integer>> solveWithModifiedNewtonMethod(MathFunction func) {
        return newtonMethod(func, new MathFunctionDifferentialOperator(), true);
    }

    @ConnectableItem(name = "Newton method", type = Item.NUMERICAL_METHOD, priority = 6)
    public Map<Double, Map.Entry<Double, Integer>> solveWithNewtonMethod(MathFunction func) {
        return newtonMethod(func, new MathFunctionDifferentialOperator(), false);
    }

}
