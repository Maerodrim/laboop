package ru.ssau.tk.sergunin.lab.functions;

import ru.ssau.tk.sergunin.lab.functions.polynomial.PolynomialParser;
import ru.ssau.tk.sergunin.lab.ui.ConnectableItem;
import ru.ssau.tk.sergunin.lab.ui.Item;

import java.util.Map;

@ConnectableItem(name = "Полином", priority = 18, type = Item.FUNCTION, hasParameter = true, parameterInstanceOfDouble = false)
public class PolynomialFunction implements MathFunction{
    private MathFunction polynomial;
    
    public PolynomialFunction(String rawPolynomial){
        Map<Integer, Double> map = new PolynomialParser().parseToMap(rawPolynomial);
        polynomial = new ZeroFunction();
        map.forEach((degree, coefficient) -> polynomial = polynomial.sum(new PowFunction(degree).multiply(coefficient)));
    }

    @Override
    public double apply(double x) {
        return polynomial.apply(x);
    }
}
