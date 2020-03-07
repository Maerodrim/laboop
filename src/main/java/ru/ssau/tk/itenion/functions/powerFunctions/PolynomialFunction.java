package ru.ssau.tk.itenion.functions.powerFunctions;

import ru.ssau.tk.itenion.functions.AbstractMathFunction;
import ru.ssau.tk.itenion.functions.MathFunction;
import ru.ssau.tk.itenion.functions.powerFunctions.polynomial.Polynomial;
import ru.ssau.tk.itenion.functions.powerFunctions.polynomial.PolynomialParser;
import ru.ssau.tk.itenion.functions.powerFunctions.polynomial.PseudoPolynomialFunction;
import ru.ssau.tk.itenion.ui.ConnectableItem;
import ru.ssau.tk.itenion.ui.Item;

import java.util.Map;

@ConnectableItem(name = "Полином", priority = 14, type = Item.FUNCTION, hasParameter = true, inputParameterName = "Polynomial:", parameterInstance = String.class)
public class PolynomialFunction extends AbstractMathFunction implements MathFunction {
    private static final long serialVersionUID = -5871823052195353949L;
    private Polynomial polynomial;

    public PolynomialFunction() {
        this(new ZeroFunction());
    }

    public PolynomialFunction(String rawPolynomial) {
        polynomial = new PolynomialParser().parse(rawPolynomial);
        name = rawPolynomial;
    }

    public PolynomialFunction(Polynomial polynomial) {
        this.polynomial = polynomial;
        name = polynomial.toString();
    }

    public PolynomialFunction(IntegerPowFunction powFunction) {
        this.polynomial = new Polynomial(Map.of(powFunction.getPow(), 1.));
        name = polynomial.toString();
    }

    public PolynomialFunction(ConstantFunction constantFunction) {
        this.polynomial = new Polynomial(Map.of(0, constantFunction.getConstant()));
        name = polynomial.toString();
    }

    @Override
    public MathFunction sum(MathFunction afterFunction) {
        return doOperation(afterFunction, new Operation("+"));
    }

    @Override
    public MathFunction subtract(MathFunction afterFunction) {
        return doOperation(afterFunction, new Operation("-"));
    }

    @Override
    public MathFunction multiply(MathFunction afterFunction) {
        return doOperation(afterFunction, new Operation("*"));
    }

    public MathFunction doOperation(MathFunction afterFunction, Operation operation) {
        if (afterFunction instanceof PolynomialFunction) {
            return operation.apply(this.polynomial, ((PolynomialFunction) afterFunction).polynomial);
        } else if (afterFunction instanceof IntegerPowFunction) {
            return operation.apply(this.polynomial, new PolynomialFunction((IntegerPowFunction) afterFunction).polynomial);
        } else if (afterFunction instanceof ConstantFunction) {
            return operation.apply(this.polynomial, new PolynomialFunction((ConstantFunction) afterFunction).polynomial);
        } else {
            return operation.apply(this, afterFunction);
        }
    }

    public Polynomial getPolynomial() {
        return polynomial;
    }

    @Override
    public double apply(double x) {
        return polynomial.apply(x);
    }

    @Override
    public PolynomialFunction differentiate() {
        return new PolynomialFunction(polynomial.derive());
    }

    private static class Operation {
        private String operation;

        Operation(String operation) {
            this.operation = operation;
        }

        PolynomialFunction apply(Polynomial a, Polynomial b) {
            Polynomial result = null;
            switch (operation) {
                case "+": {
                    result = a.add(b);
                    break;
                }
                case "-": {
                    result = a.subtract(b);
                    break;
                }
                case "*": {
                    result = a.multiply(b);
                    break;
                }
            }
            assert result != null;
            return new PolynomialFunction(result);
        }

        MathFunction apply(PolynomialFunction function, MathFunction afterFunction) {
            MathFunction result = null;
            switch (operation) {
                case "+": {
                    result = new PseudoPolynomialFunction(function).sum(afterFunction);
                    break;
                }
                case "-": {
                    result = new PseudoPolynomialFunction(function).subtract(afterFunction);
                    break;
                }
                case "*": {
                    result = new PseudoPolynomialFunction(function).multiply(afterFunction);
                    break;
                }
            }
            return result;
        }
    }

}
