package ru.ssau.tk.itenion.numericalMethods;

import ru.ssau.tk.itenion.functions.MathFunction;
import ru.ssau.tk.itenion.functions.Variable;
import ru.ssau.tk.itenion.functions.multipleVariablesFunctions.vectorFunctions.VectorMathFunction;
import ru.ssau.tk.itenion.matrix.Matrices;
import ru.ssau.tk.itenion.operations.DifferentialOperator;
import ru.ssau.tk.itenion.operations.MathFunctionDifferentialOperator;
import ru.ssau.tk.itenion.operations.MiddleSteppingDifferentialOperator;
import ru.ssau.tk.itenion.ui.ConnectableItem;
import ru.ssau.tk.itenion.ui.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@ConnectableItem(name = "", type = Item.NUMERICAL_METHOD)
public class NumericalMethods {
    private static final int NUMBER_OF_SEGMENT_SPLITS = 1001;
    double left, right, initialApproximation, eps;

    public NumericalMethods(Double left, Double right, Double initialApproximation, Double eps) {
        this.left = left;
        this.right = right;
        this.initialApproximation = initialApproximation;
        this.eps = eps;
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
        a = initialApproximation; //задаём начальное приближение
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

    public ArrayList<Double> solveNonlinearSystem(VectorMathFunction vectorMathFunction, ArrayList<Double> initialApproximation){
        ArrayList<Double> x0 = new ArrayList<>(initialApproximation);
        ArrayList<Double> x1 = new ArrayList<>();
        ArrayList<Double> y = vectorMathFunction.apply(x0);
        y.forEach(y1 -> y1 = -y1);
        ArrayList<Double> p = Matrices.solve(vectorMathFunction.getJacobiMatrix(x0), y);
        for (int i = 0; i < x0.size(); i++) {
            x1.add(x0.get(i) + p.get(i));
        }
        while (Matrices.normOfDifference(x0, x1) > eps) {
            x0 = x1;
            y = vectorMathFunction.apply(x0);
            y.forEach(y1 -> y1 = -y1);
            p = Matrices.solve(vectorMathFunction.getJacobiMatrix(x0), y);
            for (int i = 0; i < x0.size(); i++) {
                x1.set(i, x0.get(i) + p.get(i));
            }
        }
        return x1;
    }

}
