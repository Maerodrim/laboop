package ru.ssau.tk.itenion.functions.wrapFunctions;

import ru.ssau.tk.itenion.functions.MathFunction;
import ru.ssau.tk.itenion.functions.powerFunctions.ConstantFunction;
import ru.ssau.tk.itenion.functions.trigonometricFunctions.CosFunction;
import ru.ssau.tk.itenion.functions.trigonometricFunctions.SinFunction;

public class MultiplyOnConstantMF implements MathFunction{
    private static final long serialVersionUID = -2974833398703873849L;
    MathFunction function;
    double constant;

    public MultiplyOnConstantMF(MathFunction function, double constant){
        this.function = function;
        this.constant = constant;
    }

    @Override
    public double apply(double x) {
        return constant*function.apply(x);
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
        if (function instanceof SinFunction || function instanceof CosFunction) {
            return constant + function.getName();
        } else {
            return constant + "*(" + function.getName() + ")";
        }
    }
}
