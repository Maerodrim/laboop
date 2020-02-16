package ru.ssau.tk.sergunin.lab.numericalMethods;

import ru.ssau.tk.sergunin.lab.functions.MathFunction;
import ru.ssau.tk.sergunin.lab.functions.powerFunctions.PolynomialFunction;
import ru.ssau.tk.sergunin.lab.operations.DifferentialOperator;
import ru.ssau.tk.sergunin.lab.operations.MiddleSteppingDifferentialOperator;
import ru.ssau.tk.sergunin.lab.operations.PolynomialDifferentialOperator;
import ru.ssau.tk.sergunin.lab.ui.ConnectableItem;
import ru.ssau.tk.sergunin.lab.ui.Item;

import java.util.ArrayList;
import java.util.List;

@ConnectableItem(name = "", type = Item.NUMERICAL_METHOD)
public class NumericalMethods {
    double left, right, count, eps;

    public NumericalMethods(Double left, Double right, Integer count, Double eps) {
        this.left = left;
        this.right = right;
        this.count = count;
        this.eps = eps;
    }

    public List<Double> newtonMethod(MathFunction func, DifferentialOperator differentialOperator) {
        double x1, x0, x = left, step;
        List<Double> X = new ArrayList<>();
        step = (right - left) / count;
        while ((x + step) < right) {
            if (func.apply(x) * func.apply(x + step) <= 0) {
                x0 = x;
                x1 = x0 - func.apply(x0) / (differentialOperator.derive(func)).apply(x0); // первое приближение
                while (Math.abs(x1 - x0) > eps) {
                    x0 = x1;
                    x1 = x0 - func.apply(x0) / (differentialOperator.derive(func)).apply(x0);
                }
                X.add(x1);
            }
            x = x + step;
        }
        return X;
    }

    @ConnectableItem(name = "Half-division method", type = Item.NUMERICAL_METHOD, priority = 1)
    public List<Double> solveWithHalfDivisionMethod(MathFunction func) {
        double x1, x0, x = left, step;
        List<Double> X = new ArrayList<>();
        step = (right - left) / count;
        while ((x + step) < right) {
            if (func.apply(x) * func.apply(x + step) <= 0) {
                x0 = x;
                x1 = x + step; // первое приближение
                while (Math.abs(x1 - x0) > eps) {
                    if (func.apply(x0) * func.apply(0.5 * (x1 + x0)) <= 0) {
                        x1 = 0.5 * (x1 + x0);
                    } else {
                        x0 = 0.5 * (x1 + x0);
                    }
                }
                X.add(x1);
            }
            x = x + step;
        }
        return X;
    }

    @ConnectableItem(name = "Newton method", type = Item.NUMERICAL_METHOD, priority = 2, methodOnlyForPolynomialFunction = true)
    public List<Double> solveWithNewtonMethod(PolynomialFunction func) {
        return newtonMethod(func, new PolynomialDifferentialOperator());
    }

    @ConnectableItem(name = "Secant method", type = Item.NUMERICAL_METHOD, priority = 3)
    public List<Double> solveWithSecantMethod(MathFunction func) {
        return newtonMethod(func, new MiddleSteppingDifferentialOperator(eps));
    }

}
