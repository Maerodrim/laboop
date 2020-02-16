package ru.ssau.tk.sergunin.lab.NumericalMethods;

import ru.ssau.tk.sergunin.lab.functions.MathFunction;
import ru.ssau.tk.sergunin.lab.functions.PolynomialFunction;

import java.util.LinkedList;

public interface NumericalMethod {
    public LinkedList<Double> solve(PolynomialFunction func, Double left, Double right, Integer count, Double eps);
}
