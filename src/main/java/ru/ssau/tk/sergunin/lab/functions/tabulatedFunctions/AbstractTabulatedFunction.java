package ru.ssau.tk.sergunin.lab.functions.tabulatedFunctions;

import ru.ssau.tk.sergunin.lab.exceptions.ArrayIsNotSortedException;
import ru.ssau.tk.sergunin.lab.exceptions.DifferentLengthOfArraysException;
import ru.ssau.tk.sergunin.lab.functions.Point;

public abstract class AbstractTabulatedFunction implements TabulatedFunction {

    private static final long serialVersionUID = 4087048866240456298L;

    protected static void checkLengthIsTheSame(double[] xValues, double[] yValues) {
        if (xValues.length != yValues.length) {
            throw new DifferentLengthOfArraysException();
        }
    }

    protected static void checkSorted(double[] xValues) {
        for (int i = 1; i < xValues.length; i++) {
            if (xValues[i] < xValues[i - 1]) {
                throw new ArrayIsNotSortedException();
            }
        }
    }

    protected abstract int floorIndexOfX(double x) throws IllegalArgumentException;

    protected abstract double extrapolateLeft(double x);

    protected abstract double extrapolateRight(double x);

    protected abstract double interpolate(double x, int floorIndex);

    protected double interpolate(double x, double leftX, double rightX, double leftY, double rightY) {
        return leftY + (rightY - leftY) * (x - leftX) / (rightX - leftX);
    }

    @Override
    public double apply(double x) {
        if (x < leftBound()) {
            return extrapolateLeft(x);
        } else if (x > rightBound()) {
            return extrapolateRight(x);
        } else if (indexOfX(x) != -1) {
            return getY(indexOfX(x));
        } else {
            return interpolate(x, floorIndexOfX(x));
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(getClass().getSimpleName()).append(" size = ").append(getCount()).append("\n");
        for (Point point : this) {
            builder
                    .append("[")
                    .append(point.x)
                    .append("; ")
                    .append(point.y)
                    .append("]\n");
        }
        builder.deleteCharAt(builder.length() - 1);
        return builder.toString();
    }


}
