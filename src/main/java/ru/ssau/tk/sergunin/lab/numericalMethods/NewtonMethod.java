package ru.ssau.tk.sergunin.lab.numericalMethods;

import ru.ssau.tk.sergunin.lab.functions.MathFunction;
import ru.ssau.tk.sergunin.lab.functions.PolynomialFunction;
import ru.ssau.tk.sergunin.lab.operations.MiddleSteppingDifferentialOperator;

import java.util.List;


public class NewtonMethod implements NumericalMethod {
    public List<Double> solve(PolynomialFunction func, Double left, Double right, Integer count, Double eps) {
        double x1 = 0, x0 = 0,x=left,step;
        List<Double> X = null;
        step=(right-left)/count;
        while(x!=right) {
            if (func.apply(x)*func.apply(x+step)<=0) {
                x0=x;
                x1 = x0 - func.apply(x0) / (new MiddleSteppingDifferentialOperator(eps).derive((MathFunction) func)).apply(x0); // первое приближение
                while (Math.abs(x1 - x0) > eps) {
                    x0 = x1;
                    x1 = x0 - func.apply(x0) / (new MiddleSteppingDifferentialOperator(eps).derive((MathFunction) func)).apply(x0);
                }
                X.add(x1);
            }
            x=x+step;
        }
        return X;
    }
}
