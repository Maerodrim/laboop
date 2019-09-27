package ru.ssau.tk.sergunin.lab.functions;


import java.util.Iterator;

public class UnmodifiableTabulatedFunction implements TabulatedFunction {
    TabulatedFunction tabulatedFunction;

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
        throw new UnsupportedOperationException();
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
    public Iterator<Point> iterator() {
        return tabulatedFunction.iterator();
    }

    protected int floorIndexOfX(double x) throws IllegalArgumentException {
        return ((AbstractTabulatedFunction) tabulatedFunction).floorIndexOfX(x);
    }

    protected double extrapolateLeft(double x) {
        return ((AbstractTabulatedFunction) tabulatedFunction).extrapolateLeft(x);
    }

    protected double extrapolateRight(double x) {
        return ((AbstractTabulatedFunction) tabulatedFunction).extrapolateRight(x);
    }

    protected double interpolate(double x, int floorIndex) {
        return ((AbstractTabulatedFunction) tabulatedFunction).interpolate(x, floorIndex);
    }

    @Override
    public double apply(double x) {
        return tabulatedFunction.apply(x);
    }
}
