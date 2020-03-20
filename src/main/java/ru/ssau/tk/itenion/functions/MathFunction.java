package ru.ssau.tk.itenion.functions;

import Jama.Matrix;
import com.fasterxml.jackson.annotation.JsonIgnore;
import ru.ssau.tk.itenion.enums.Variable;
import ru.ssau.tk.itenion.functions.multipleVariablesFunctions.vectorArgumentMathFunctions.VAMF;
import ru.ssau.tk.itenion.functions.powerFunctions.ConstantFunction;
import ru.ssau.tk.itenion.functions.powerFunctions.ZeroFunction;
import ru.ssau.tk.itenion.functions.tabulatedFunctions.TabulatedFunction;

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
        MathFunction result;
        if (afterFunction instanceof ConstantFunction) {
            if (this instanceof LinearCombinationFunction) {
                result = new LinearCombinationFunction((LinearCombinationFunction) this, (ConstantFunction) afterFunction, true);
            } else {
                result = new LinearCombinationFunction(this, (ConstantFunction) afterFunction, true);
            }
        } else if (this instanceof ConstantFunction) {
            result = new LinearCombinationFunction(afterFunction, (ConstantFunction) this, true);
        } else {
            result = new MathFunction() {
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
                    return function.getName() + " + " + afterFunction.getName();
                }
            };
        }
        if (result instanceof LinearCombinationFunction && ((LinearCombinationFunction) result).getShift() == 0 && ((LinearCombinationFunction) result).getConstant() == 1) {
            result = ((LinearCombinationFunction) result).getFunction();
        }
        return result;
    }

    default MathFunction subtract(MathFunction afterFunction) {
        MathFunction function = this;
        MathFunction result;
//        if (this instanceof LinearCombinationFunction && ((LinearCombinationFunction) this).getFunction() instanceof PolynomialFunction) {
//            result =
//        } else
        if (afterFunction instanceof ConstantFunction) {
            result = sum(new ConstantFunction(-((ConstantFunction) afterFunction).getConstant()));
        } else if (this instanceof ConstantFunction) {
            result = new LinearCombinationFunction(afterFunction, new ConstantFunction(-((ConstantFunction) this).getConstant()), true);
        } else {
            result = new MathFunction() {
                private static final long serialVersionUID = 1734329896572016553L;

                @Override
                public double apply(double x) {
                    return function.apply(x) - afterFunction.apply(x);
                }

                @Override
                public MathFunction differentiate() {
                    return function.differentiate().subtract(afterFunction.differentiate());
                }

                @Override
                public String getName() {
                    return function.getName() + " - " + afterFunction.getName();
                }
            };
        }
        if (result instanceof LinearCombinationFunction && ((LinearCombinationFunction) result).getShift() == 0 && ((LinearCombinationFunction) result).getConstant() == 1) {
            result = ((LinearCombinationFunction) result).getFunction();
        }
        return result;
    }

    default MathFunction sum(double number) {
        return new LinearCombinationFunction(this, 1, number);
    }

    default MathFunction subtract(double number) {
        return new LinearCombinationFunction(this, 1, -number);
    }

    default MathFunction multiply(double number) {
        return new LinearCombinationFunction(this, number, 0);
    }

    default MathFunction negate() {
        return multiply(-1);
    }

    default MathFunction multiply(MathFunction afterFunction) {
        MathFunction function = this;
        MathFunction result;
        if (afterFunction instanceof ConstantFunction) {
            if (this instanceof LinearCombinationFunction) {
                result = new LinearCombinationFunction((LinearCombinationFunction) this, (ConstantFunction) afterFunction, false);
            } else {
                result = new LinearCombinationFunction(this, (ConstantFunction) afterFunction, false);
            }
        } else if (this instanceof ConstantFunction) {
            result = new LinearCombinationFunction(afterFunction, (ConstantFunction) this, false);
        } else {
            result = new MathFunction() {
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
        if (result instanceof LinearCombinationFunction && ((LinearCombinationFunction) result).getShift() == 0 && ((LinearCombinationFunction) result).getConstant() == 1) {
            result = ((LinearCombinationFunction) result).getFunction();
        }
        return result;
    }

    default MathFunction sqr() {
        return multiply(this);
    }

    default MathFunction divide(MathFunction afterFunction) {
        if (afterFunction instanceof ZeroFunction) {
            throw new IllegalArgumentException();
        }
        MathFunction result;
        if (afterFunction instanceof ConstantFunction) {
            result = this.multiply(new ConstantFunction(1 / ((ConstantFunction) afterFunction).getConstant()));
        } else if (this instanceof ConstantFunction) {
            if (this instanceof ZeroFunction) {
                throw new IllegalArgumentException();
            }
            result = new LinearCombinationFunction(afterFunction, new ConstantFunction(1 / ((ConstantFunction) this).getConstant()), false);
        } else {
            result = multiply(afterFunction.negate());
        }
        if (result instanceof LinearCombinationFunction && ((LinearCombinationFunction) result).getShift() == 0 && ((LinearCombinationFunction) result).getConstant() == 1) {
            result = ((LinearCombinationFunction) result).getFunction();
        }
        return result;
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
        throw new UnsupportedOperationException();
    }

    @Override
    @JsonIgnore
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

    @Override
    default VAMF getMathFunction(Variable variable) {
        return this;
    }

}
