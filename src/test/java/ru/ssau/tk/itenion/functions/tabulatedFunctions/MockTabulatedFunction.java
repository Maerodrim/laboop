package ru.ssau.tk.itenion.functions.tabulatedFunctions;

import org.jetbrains.annotations.NotNull;
import ru.ssau.tk.itenion.functions.MathFunction;
import ru.ssau.tk.itenion.functions.Point;

import java.util.Iterator;

public class MockTabulatedFunction extends AbstractTabulatedFunction {

    private static final long serialVersionUID = 8807641477090582045L;
    private final double x0 = 0;
    private final double x1 = 1;
    private double y0 = 5;
    private double y1 = 10;

    @Override
    public int floorIndexOfX(double x) {
        if (x < x0) {
            return 0;
        } else if (x > x0 && x < x1) {
            return 1;
        } else {
            return 2;
        }
    }

    @Override
    protected double extrapolateLeft(double x) {
        return interpolate(x, x0, x1, y0, y1);
    }

    @Override
    protected double extrapolateRight(double x) {
        return interpolate(x, x0, x1, y0, y1);
    }

    @Override
    protected double interpolate(double x, int floorIndex) {
        return interpolate(x, x0, x1, y0, y1);
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public double getX(int index) {
        if (index == 0) {
            return x0;
        } else {
            return x1;
        }
    }

    @Override
    public double getY(int index) {
        if (index == 0) {
            return y0;
        } else {
            return y1;
        }
    }

    @Override
    public void setY(int index, double value) {
        if (index == 0) {
            y0 = value;
        } else {
            y1 = value;
        }
    }

    @Override
    public void setY(TabulatedFunction function) {
    }

    @Override
    public int indexOfX(double x) {
        if (x == x0) {
            return 0;
        } else if (x == x1) {
            return 1;
        } else {
            return -1;
        }
    }

    @Override
    public int indexOfY(double y) {
        if (y == y0) {
            return 0;
        } else if (y == y1) {
            return 1;
        } else {
            return -1;
        }
    }

    @Override
    public double leftBound() {
        return x0;
    }

    @Override
    public double rightBound() {
        return x1;
    }

    @Override
    public TabulatedFunction copy() {
        return null;
    }

    @Override
    public boolean isStrict() {
        return false;
    }

    @Override
    public boolean isUnmodifiable() {
        return false;
    }

    @Override
    public TabulatedFunction unwrap() {
        return this;
    }

    @Override
    public MathFunction getMathFunction() {
        return null;
    }

    @Override
    public void setMathFunction(MathFunction math) {

    }

    @Override
    public void offerStrict(boolean isStrict) {
    }

    @Override
    public void offerUnmodifiable(boolean isUnmodifiable) {
    }

    @NotNull
    @Override
    public Iterator<Point> iterator() {
        return null;
    }

    @Override
    public void insert(double x, double y) {
        insert(x, y);
    }

    @Override
    public void remove(int index) throws IllegalArgumentException, ArrayIndexOutOfBoundsException {
        remove(index);
    }
}