package ru.ssau.tk.sergunin.lab.numericalMethods;

import ru.ssau.tk.sergunin.lab.functions.MathFunction;
import ru.ssau.tk.sergunin.lab.functions.PolynomialFunction;
import ru.ssau.tk.sergunin.lab.operations.MiddleSteppingDifferentialOperator;
import ru.ssau.tk.sergunin.lab.ui.ConnectableItem;
import ru.ssau.tk.sergunin.lab.ui.Item;

import java.util.ArrayList;
import java.util.List;

@ConnectableItem(name = "Newton method", type = Item.NUMERICAL_METHOD)
public class NumericalMethods {
    double left, right, count, eps;

    public NumericalMethods(Double left, Double right, Integer count, Double eps){
        this.left = left;
        this.right = right;
        this.count = count;
        this.eps = eps;
    }

    public List<Double> solveWithSecantMethod(PolynomialFunction func) {
        double x1 = 0, x0 = 0,x=left,step;
        List<Double> X = new ArrayList<>();
        step=(right-left)/count;
        while((x+step)<right) {
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
