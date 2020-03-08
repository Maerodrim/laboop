package ru.ssau.tk.itenion.functions.wrapFunctions;

import ru.ssau.tk.itenion.functions.MathFunction;
import ru.ssau.tk.itenion.functions.powerFunctions.ConstantFunction;
import ru.ssau.tk.itenion.ui.ConnectableItem;

import java.util.Objects;

public class MultiplyOnConstantMF implements MathFunction {
    private static final long serialVersionUID = -2974833398703873849L;
    MathFunction function;
    double constant;

    public MultiplyOnConstantMF(MathFunction function, double constant) {
        if (!(function instanceof MultiplyOnConstantMF)) {
            this.function = function;
            this.constant = constant;
        } else {
            this.function = ((MultiplyOnConstantMF) function).function;
            this.constant = ((MultiplyOnConstantMF) function).constant * constant;
        }
    }

    public MultiplyOnConstantMF(MathFunction function, ConstantFunction constantFunction) {
        this(function, constantFunction.getConstant());
    }

    @Override
    public double apply(double x) {
        return constant * function.apply(x);
    }

    @Override
    public MathFunction differentiate() {
        return new MultiplyOnConstantMF(function.differentiate(), constant);
    }

    @Override
    public String getName() {
        if (function instanceof ConstantFunction) {
            return constant * ((ConstantFunction) function).getConstant() + "";
        }
        ConnectableItem item = function.getClass().getDeclaredAnnotation(ConnectableItem.class);
        if (Objects.isNull(item) || !item.isAdjacentConstant()) {
            return constant + "*(" + function.getName() + ")";
        } else {
            return constant + function.getName();
        }
    }
}
