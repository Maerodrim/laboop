package ru.ssau.tk.itenion.functions;

import ru.ssau.tk.itenion.functions.powerFunctions.ConstantFunction;
import ru.ssau.tk.itenion.functions.powerFunctions.IdentityFunction;
import ru.ssau.tk.itenion.functions.powerFunctions.PolynomialFunction;
import ru.ssau.tk.itenion.ui.ConnectableItem;

import java.util.Objects;

public class LinearCombinationFunction implements MathFunction {
    private static final long serialVersionUID = -2974833398703873849L;
    MathFunction function;
    double constant;
    double shift;

    public LinearCombinationFunction(MathFunction function, double constant, double shift) {
        this.function = function;
        this.constant = constant;
        this.shift = shift;
    }

    public LinearCombinationFunction(LinearCombinationFunction function, double constant, double shift) {
        this.function = function.function;
        this.constant = function.constant * constant;
        this.shift = function.shift + shift;
    }

    public LinearCombinationFunction(MathFunction function, ConstantFunction constantFunction, boolean isShift) {
        this.function = function;
        if (isShift) {
            this.constant = 1;
            this.shift = constantFunction.getConstant();
        } else {
            this.constant = constantFunction.getConstant();
            this.shift = 0;
        }
    }

    public LinearCombinationFunction(LinearCombinationFunction function, ConstantFunction constantFunction, boolean isShift) {
        this.function = function.function;
        if (isShift) {
            this.constant = function.constant;
            this.shift = function.shift + constantFunction.getConstant();
        } else {
            this.constant = function.constant * constantFunction.getConstant();
            this.shift = function.shift * constantFunction.getConstant();
        }
    }

    public static boolean isValidForSimplePlot(MathFunction function) {
        return function instanceof IdentityFunction
                || function instanceof LinearCombinationFunction && ((LinearCombinationFunction) function).getFunction() instanceof IdentityFunction
                || function instanceof PolynomialFunction && ((PolynomialFunction) function).isValidForPlot()
                || function instanceof LinearCombinationFunction && ((LinearCombinationFunction) function).getFunction() instanceof PolynomialFunction && ((PolynomialFunction) ((LinearCombinationFunction) function).getFunction()).isValidForPlot();
    }

    @Override
    public MathFunction sum(double number) {
        return new LinearCombinationFunction(this, constant, number);
    }

    @Override
    public MathFunction subtract(double number) {
        return new LinearCombinationFunction(this, constant, -number);
    }

    @Override
    public MathFunction multiply(double number) {
        return new LinearCombinationFunction(this.getFunction(), number*constant, shift*number);
    }

    @Override
    public MathFunction negate() {
        return new LinearCombinationFunction(function, -constant, -shift);
    }

    @Override
    public double apply(double x) {
        return constant * function.apply(x) + shift;
    }

    @Override
    public MathFunction differentiate() {
        return new LinearCombinationFunction(function.differentiate(), constant, 0);
    }

    @Override
    public String getName() {
        String name;
        if (function instanceof ConstantFunction) {
            name = (constant * ((ConstantFunction) function).getConstant() + shift) + "";
        } else {
            ConnectableItem item = function.getClass().getDeclaredAnnotation(ConnectableItem.class);
            if (Objects.isNull(item) || !item.isAdjacentConstant()) {
                name = constant + "*(" + function.getName() + ")";
            } else {
                if (constant == 1) {
                    name = function.getName();
                } else if (constant == -1) {
                    name = "-" + function.getName();
                } else {
                    name = constant + function.getName();
                }
            }
        }
        if (shift != 0) {
            String sign = shift > 0 ? " + " : " - ";
            name = name + sign + Math.abs(shift);
        }
        return name;
    }

    @Override
    public String toString() {
        return getName();
    }

    public MathFunction getFunction() {
        return function;
    }

    public double getConstant() {
        return constant;
    }

    public double getShift() {
        return shift;
    }

    public static LinearCombinationFunction getWithNegateShift(LinearCombinationFunction function){
        return new LinearCombinationFunction(function.function, function.constant, -function.shift);
    }

    public static LinearCombinationFunction getWithNegateConstant(LinearCombinationFunction function){
        return new LinearCombinationFunction(function.function, -function.constant, function.shift);
    }
}
