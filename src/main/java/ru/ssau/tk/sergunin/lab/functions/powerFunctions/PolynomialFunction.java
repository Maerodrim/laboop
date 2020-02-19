package ru.ssau.tk.sergunin.lab.functions.powerFunctions;

import ru.ssau.tk.sergunin.lab.functions.AbstractMathFunction;
import ru.ssau.tk.sergunin.lab.functions.MathFunction;
import ru.ssau.tk.sergunin.lab.functions.powerFunctions.polynomial.Polynomial;
import ru.ssau.tk.sergunin.lab.functions.powerFunctions.polynomial.PolynomialParser;
import ru.ssau.tk.sergunin.lab.ui.ConnectableItem;
import ru.ssau.tk.sergunin.lab.ui.Item;

@ConnectableItem(name = "Полином", priority = 13, type = Item.FUNCTION, hasParameter = true, parameterInstanceOfDouble = false)
public class PolynomialFunction extends AbstractMathFunction implements MathFunction {
    private static final long serialVersionUID = -5871823052195353949L;
    private Polynomial polynomial;

    public PolynomialFunction(String rawPolynomial) {
        polynomial = new PolynomialParser().parse(rawPolynomial);
        name = rawPolynomial;
    }

    public PolynomialFunction(Polynomial polynomial) {
        this.polynomial = polynomial;
        name = polynomial.toString();
    }

    public Polynomial getPolynomial() {
        return polynomial;
    }

    @Override
    public double apply(double x) {
        return polynomial.apply(x);
    }

    @Override
    public MathFunction differentiate() {
        return new PolynomialFunction(polynomial.derive());
    }
}
