package ru.ssau.tk.sergunin.lab.numericalMethods;

import ru.ssau.tk.sergunin.lab.functions.PolynomialFunction;
import ru.ssau.tk.sergunin.lab.ui.ConnectableItem;
import ru.ssau.tk.sergunin.lab.ui.Item;

import java.util.List;

@ConnectableItem(name = "Newton method", type = Item.NUMERICAL_METHOD)
public interface NumericalMethod {
    List<Double> solve(PolynomialFunction func, Double left, Double right, Integer count, Double eps);
}
