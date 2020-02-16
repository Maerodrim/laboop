package ru.ssau.tk.sergunin.lab.functions.factory;

import ru.ssau.tk.sergunin.lab.functions.MathFunction;
import ru.ssau.tk.sergunin.lab.functions.tabulatedFunctions.StrictTabulatedFunction;
import ru.ssau.tk.sergunin.lab.functions.tabulatedFunctions.TabulatedFunction;
import ru.ssau.tk.sergunin.lab.functions.tabulatedFunctions.UnmodifiableTabulatedFunction;

public interface TabulatedFunctionFactory {
    TabulatedFunction create(double[] xValues, double[] yValues);

    default TabulatedFunction createStrict(double[] xValues, double[] yValues) {
        return new StrictTabulatedFunction(create(xValues, yValues));
    }

    default TabulatedFunction createUnmodifiable(double[] xValues, double[] yValues) {
        return new UnmodifiableTabulatedFunction(create(xValues, yValues));
    }

    default TabulatedFunction createStrictUnmodifiable(double[] xValues, double[] yValues) {
        return new UnmodifiableTabulatedFunction(new StrictTabulatedFunction(create(xValues, yValues)));
    }

    TabulatedFunction create(MathFunction source, double xFrom, double xTo, int count);

    default TabulatedFunction createStrict(MathFunction source, double xFrom, double xTo, int count) {
        return new StrictTabulatedFunction(create(source, xFrom, xTo, count));
    }

    default TabulatedFunction createUnmodifiable(MathFunction source, double xFrom, double xTo, int count) {
        return new UnmodifiableTabulatedFunction(create(source, xFrom, xTo, count));
    }

    default TabulatedFunction createStrictUnmodifiable(MathFunction source, double xFrom, double xTo, int count) {
        return new UnmodifiableTabulatedFunction(new StrictTabulatedFunction(create(source, xFrom, xTo, count)));
    }

    default TabulatedFunction create(MathFunction source, double xFrom, double xTo, int count, boolean isStrict, boolean isUnmodifiable) {
        if (isUnmodifiable && isStrict) {
            return createStrictUnmodifiable(source, xFrom, xTo, count);
        } else if (isUnmodifiable) {
            return createUnmodifiable(source, xFrom, xTo, count);
        } else if (isStrict) {
            return createStrict(source, xFrom, xTo, count);
        } else {
            return create(source, xFrom, xTo, count);
        }
    }

    TabulatedFunction getIdentity();

    Class<?> getTabulatedFunctionClass();
}
