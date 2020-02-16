package ru.ssau.tk.sergunin.lab.NumericalMethods;

import ru.ssau.tk.sergunin.lab.functions.MathFunction;
import ru.ssau.tk.sergunin.lab.functions.PolynomialFunction;

import java.util.LinkedList;
import java.util.List;

public interface NumericalMethod {
    public List<Double> solve(PolynomialFunction func, Double left, Double right, Integer count, Double eps);
}
