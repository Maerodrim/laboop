package ru.ssau.tk.itenion.functions;

import Jama.Matrix;
import ru.ssau.tk.itenion.functions.multipleVariablesFunctions.vectorArgumentMathFunctions.VAMF;
import ru.ssau.tk.itenion.functions.powerFunctions.ZeroFunction;
import ru.ssau.tk.itenion.functions.wrapFunctions.MultiplyOnConstantMF;

import java.io.Serializable;
import java.util.ArrayList;

public interface MathFunction extends Serializable, Nameable, VAMF, Differentiable {

    double apply(double x);

    default CompositeFunction andThen(MathFunction afterFunction) {
        return new CompositeFunction(this, afterFunction);
    }

    default CompositeFunction compose(MathFunction beforeFunction) {
        return new CompositeFunction(beforeFunction, this);
    }

    default MathFunction sum(MathFunction afterFunction) {
        MathFunction function = this;
        return new MathFunction() {
            private static final long serialVersionUID = -4986154588819604160L;

            @Override
            public double apply(double x) {
                return function.apply(x) + afterFunction.apply(x);
            }

            @Override
            public MathFunction differentiate() {
                return function.differentiate().sum(afterFunction.differentiate());
            }

            @Override
            public String getName() {
                return "(" + function.getName() + ") + (" + afterFunction.getName() + ")";
            }
        };
    }

    default MathFunction subtract(MathFunction afterFunction) {
        return sum(afterFunction.negate());
    }

    default MathFunction multiply(double number) {
        /*MathFunction function = this;
        return new MathFunction() {
            private static final long serialVersionUID = 4507943343697267559L;

            @Override
            public double apply(double x) {
                return number * function.apply(x);
            }

            @Override
            public MathFunction differentiate() {
                return function.differentiate().multiply(number);
            }

            @Override
            public String getName() {
                return number + "*(" + function.getName() + ")";
            }
        };*/
        return new MultiplyOnConstantMF(this, number);
    }

    default MathFunction negate() {
        return multiply(-1);
    }

    default MathFunction multiply(MathFunction afterFunction) {
        MathFunction function = this;
        return new MathFunction() {
            private static final long serialVersionUID = 4507943343697267559L;

            @Override
            public double apply(double x) {
                return function.apply(x) * afterFunction.apply(x);
            }

            @Override
            public MathFunction differentiate() {
                return function.differentiate().multiply(afterFunction).sum(afterFunction.differentiate().multiply(function));
            }

            @Override
            public String getName() {
                return "(" + function.getName() + ") * (" + afterFunction.getName() + ")";
            }
        };

    }

    default MathFunction sqr() {
        return multiply(this);
    }

    default MathFunction divide(MathFunction afterFunction) {
        if (afterFunction instanceof ZeroFunction) {
            throw new IllegalArgumentException();
        }
        return multiply(afterFunction.inverse());
    }

    default MathFunction inverse() {
        if (this instanceof ZeroFunction) {
            throw new IllegalArgumentException();
        }
        MathFunction function = this;
        return new MathFunction() {
            private static final long serialVersionUID = -1712670267695331417L;

            @Override
            public double apply(double x) {
                if (x == 0) {
                    throw new IllegalArgumentException();
                }
                return 1. / x;
            }

            @Override
            public MathFunction differentiate() {
                return function.sqr().inverse().negate();
            }

            @Override
            public String getName() {
                return "1/(" + function.getName() + ")";
            }
        };
    }

    default VAMF differentiate(Variable variable) {
        return differentiate();
    }

    @Override
    default MathFunction get(Variable variable) {
        return this;
    }

    @Override
    default void put(Variable variable, MathFunction mathFunction) {
    }

    @Override
    default int getDimension() {
        return 1;
    }

    default String getName(Variable variable) {
        if (!variable.equals(Variable.x)) {
            return getName().replaceAll("x", variable.toString());
        } else {
            return getName();
        }
    }

    default double apply(ArrayList<Double> x) {
        throw new UnsupportedOperationException();
    }

    default double apply(Matrix x) {
        throw new UnsupportedOperationException();
    }
}
