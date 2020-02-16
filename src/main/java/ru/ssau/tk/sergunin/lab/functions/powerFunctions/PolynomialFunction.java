package ru.ssau.tk.sergunin.lab.functions.powerFunctions;

import ru.ssau.tk.sergunin.lab.functions.MathFunction;
import ru.ssau.tk.sergunin.lab.functions.powerFunctions.polynomial.Polynomial;
import ru.ssau.tk.sergunin.lab.functions.powerFunctions.polynomial.PolynomialParser;
import ru.ssau.tk.sergunin.lab.ui.ConnectableItem;
import ru.ssau.tk.sergunin.lab.ui.Item;

@ConnectableItem(name = "Полином", priority = 18, type = Item.FUNCTION, hasParameter = true, parameterInstanceOfDouble = false)
public class PolynomialFunction implements MathFunction {
    private MathFunction wrapPolynomial;
    private Polynomial polynomial;
    private String rawPolynomial;
    
    public PolynomialFunction(String rawPolynomial){
        this.rawPolynomial = rawPolynomial;
        polynomial = new PolynomialParser().parse(rawPolynomial);
        wrapPolynomial = new ZeroFunction();
        polynomial.getMembers().forEach(
                (degree, coefficient) -> wrapPolynomial = wrapPolynomial.sum(new PowFunction(degree).multiply(coefficient))
        );
    }

    public PolynomialFunction(Polynomial polynomial){
        this.polynomial = polynomial;
        wrapPolynomial = new ZeroFunction();
        polynomial.getMembers().forEach(
                (degree, coefficient) -> wrapPolynomial = wrapPolynomial.sum(new PowFunction(degree).multiply(coefficient))
        );
        this.rawPolynomial = polynomial.toString();
    }

    public Polynomial getPolynomial() {
        return polynomial;
    }

    @Override
    public double apply(double x) {
        return wrapPolynomial.apply(x);
    }

    @Override
    public String toString() {
        return rawPolynomial;
    }
}
