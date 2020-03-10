package ru.ssau.tk.itenion.functions.wrapFunctions;

import ru.ssau.tk.itenion.functions.MathFunction;
import ru.ssau.tk.itenion.functions.powerFunctions.ConstantFunction;
import ru.ssau.tk.itenion.ui.ConnectableItem;

import java.util.Objects;

public class ForDoubleOperations implements MathFunction {
    private static final long serialVersionUID = -2974833398703873849L;
    MathFunction function;
    double constant;
    double shift;

    public ForDoubleOperations(MathFunction function, double constant, double shift) {
        if (!(function instanceof ForDoubleOperations)) {
            this.function = function;
            this.constant = constant;
        } else {
            this.function = ((ForDoubleOperations) function).function;
            this.constant = ((ForDoubleOperations) function).constant * constant;
            this.shift = shift;
        }
    }

    public ForDoubleOperations(MathFunction function, ConstantFunction constantFunction, boolean isShift) {
        if (!(function instanceof ForDoubleOperations)) {
            this.function = function;
            if (isShift) {
                this.shift = constantFunction.getConstant();
            } else {
                this.constant = constantFunction.getConstant();
            }
        } else {
            this.function = ((ForDoubleOperations) function).function;
            if (isShift) {
                this.constant = ((ForDoubleOperations) function).constant;
                this.shift = ((ForDoubleOperations) function).shift + constantFunction.getConstant();
            } else {
                this.constant = ((ForDoubleOperations) function).constant * constantFunction.getConstant();
                this.shift = ((ForDoubleOperations) function).shift;
            }
        }
    }

    @Override
    public double apply(double x) {
        return constant * function.apply(x) + shift;
    }

    @Override
    public MathFunction differentiate() {
        return new ForDoubleOperations(function.differentiate(), constant, 0);
    }

    @Override
    public String getName() {
        String name;
        if (function instanceof ConstantFunction) {
            name = constant * ((ConstantFunction) function).getConstant() + "";
        }
        ConnectableItem item = function.getClass().getDeclaredAnnotation(ConnectableItem.class);
        if (Objects.isNull(item) || !item.isAdjacentConstant()) {
            name = constant + "*(" + function.getName() + ")";
        } else {
            name = constant + function.getName();
        }
        if (shift != 0) {
            String sign = shift > 0 ? " + " : " - ";
            name = "(" + name + ")" + sign + shift;
        }
        return name;
    }

    public MathFunction getFunction() {
        return function;
    }
}
