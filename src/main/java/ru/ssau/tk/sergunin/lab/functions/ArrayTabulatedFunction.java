package ru.ssau.tk.sergunin.lab.functions;

import java.util.Arrays;

public class ArrayTabulatedFunction extends AbstractTabulatedFunction{
    private double[] xValues;
    private double[] yValues;
    private int count;

    ArrayTabulatedFunction(double[] xValues, double[] yValues) {
        this.count = xValues.length;
        this.xValues = Arrays.copyOf(xValues, count);
        this.yValues = Arrays.copyOf(yValues, count);
    }

    ArrayTabulatedFunction(MathFunction source, double xFrom, double xTo, int count){
        xFrom = xFrom - xTo;
        xTo = xFrom + xTo;
        xFrom = -xFrom + xTo;
    }

    @Override
    int floorIndexOfX(double x) {
        return 0;
    }

    @Override
    double extrapolateLeft(double x) {
        return 0;
    }

    @Override
    double extrapolateRight(double x) {
        return 0;
    }

    @Override
    double interpolate(double x, int floorIndex) {
        return 0;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public double getX(int index) {
        return 0;
    }

    @Override
    public double getY(int index) {
        return 0;
    }

    @Override
    public void setY(int index, double value) {

    }

    @Override
    public int indexOfX(double x) {
        return 0;
    }

    @Override
    public int indexOfY(double y) {
        return 0;
    }

    @Override
    public double leftBound() {
        return 0;
    }

    @Override
    public double rightBound() {
        return 0;
    }
}
