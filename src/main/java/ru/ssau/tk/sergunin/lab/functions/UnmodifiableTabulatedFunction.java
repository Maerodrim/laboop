package ru.ssau.tk.sergunin.lab.functions;


import java.util.Iterator;

public class UnmodifiableTabulatedFunction implements TabulatedFunction {
    private TabulatedFunction tabulatedFunction;

    public UnmodifiableTabulatedFunction(TabulatedFunction tabulatedFunction) {
        this.tabulatedFunction = tabulatedFunction;
    }

    @Override
    public int getCount() {
        return tabulatedFunction.getCount();
    }

    @Override
    public double getX(int index) throws RuntimeException {
        return tabulatedFunction.getX(index);
    }

    @Override
    public double getY(int index) throws RuntimeException {
        return tabulatedFunction.getY(index);
    }

    @Override
    public void setY(int index, double value) throws RuntimeException {
        throw new UnsupportedOperationException("Change values of Y don't support");
    }

    @Override
    public void setY(TabulatedFunction function) {
        throw new UnsupportedOperationException("Change values of Y don't support");
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

    public TabulatedFunction unwrapTabulatedFunction() {
        return tabulatedFunction;
    }

    @Override
    public boolean isStrict() {
        return tabulatedFunction.isStrict();
    }

    @Override
    public boolean isUnmodifiable() {
        return true;
    }

    @Override
    public TabulatedFunction unwrap() {
        return tabulatedFunction;
    }

    @Override
    public void offerStrict(boolean isStrict) {
    }

    @Override
    public void offerUnmodifiable(boolean isUnmodifiable) {
    }

    @Override
    public Iterator<Point> iterator() {
        return tabulatedFunction.iterator();
    }

    @Override
    public double apply(double x) {
        return tabulatedFunction.apply(x);
    }

    @Override
    public void insert(double x, double y) {
        throw new UnsupportedOperationException("Insert don't support");
    }

    @Override
    public void remove(int index) throws IllegalArgumentException, ArrayIndexOutOfBoundsException {
        throw new UnsupportedOperationException("Remove don't support");
    }
}
