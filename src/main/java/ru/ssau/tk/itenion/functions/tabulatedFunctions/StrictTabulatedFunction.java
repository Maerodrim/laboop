package ru.ssau.tk.itenion.functions.tabulatedFunctions;

import org.jetbrains.annotations.NotNull;
import ru.ssau.tk.itenion.functions.MathFunction;
import ru.ssau.tk.itenion.functions.Point;

import java.util.Iterator;

public class StrictTabulatedFunction implements TabulatedFunction {
    private static final long serialVersionUID = -3410726428353433261L;
    private TabulatedFunction tabulatedFunction;

    public StrictTabulatedFunction(TabulatedFunction tabulatedFunction) {
        this.tabulatedFunction = tabulatedFunction;
    }

    @Override
    public int getCount() {
        return tabulatedFunction.getCount();
    }

    @Override
    public double getX(int index) {
        return tabulatedFunction.getX(index);
    }

    @Override
    public double getY(int index) {
        return tabulatedFunction.getY(index);
    }

    @Override
    public void setY(int index, double value) {
        tabulatedFunction.setY(index, value);
    }

    @Override
    public void setY(TabulatedFunction function) {
        tabulatedFunction.setY(function);
    }

    @Override
    public int indexOfX(double x) {
        return tabulatedFunction.indexOfX(x);
    }

    @Override
    public int indexOfY(double y) {
        return tabulatedFunction.indexOfY(y);
    }

    @Override
    public double leftBound() {
        return tabulatedFunction.leftBound();
    }

    @Override
    public double rightBound() {
        return tabulatedFunction.rightBound();
    }

    @Override
    public TabulatedFunction copy() {
        return tabulatedFunction.copy();
    }

    @Override
    public boolean isStrict() {
        return true;
    }

    @Override
    public boolean isUnmodifiable() {
        return tabulatedFunction.isUnmodifiable();
    }

    @NotNull
    @Override
    public Iterator<Point> iterator() {
        return tabulatedFunction.iterator();
    }

    @Override
    public double apply(double x) throws RuntimeException {
        if (tabulatedFunction.indexOfX(x) == -1) {
            throw new UnsupportedOperationException("Apply don't support");
        } else {
            return tabulatedFunction.apply(x);
        }

    }

    public TabulatedFunction unwrap() {
        return tabulatedFunction;
    }

    @Override
    public int floorIndexOfX(double x) {
        return tabulatedFunction.floorIndexOfX(x);
    }

    @Override
    public MathFunction getMathFunction() {
        return tabulatedFunction.getMathFunction();
    }

    @Override
    public void setMathFunction(MathFunction math) {
        tabulatedFunction.setMathFunction(math);
    }

    @Override
    public void offerStrict(boolean isStrict) {
    }

    @Override
    public void offerUnmodifiable(boolean isUnmodifiable) {
        tabulatedFunction.offerUnmodifiable(isUnmodifiable);
    }

    @Override
    public void insert(double x, double y) {
        tabulatedFunction.insert(x, y);
    }

    @Override
    public void remove(int index) throws IllegalArgumentException, ArrayIndexOutOfBoundsException {
        tabulatedFunction.remove(index);
    }
}
