package ru.ssau.tk.sergunin.lab.functions;

public class MockTabulatedFunction extends AbstractTabulatedFunction{

    private final double x0 = 0;
    private final double x1 = 1;
    private double y0 = 5;
    private double y1 = 10;

    @Override
    int floorIndexOfX(double x) {
        if (x < x0) {
            return 0;
        }
        else if (x > x0 && x < x1) {
            return 1;
        }
        else {
            return 2;
        }
    }

    @Override
    double extrapolateLeft(double x) {
        return interpolate(x, x0, x1, y0, y1);
    }

    @Override
    double extrapolateRight(double x) {
        return interpolate(x, x0, x1, y0, y1);
    }

    @Override
    double interpolate(double x, int floorIndex) {
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
        }
        else {
            return x1;
        }
    }

    @Override
    public double getY(int index) {
        if (index == 0) {
            return y0;
        }
        else {
            return y1;
        }
    }

    @Override
    public void setY(int index, double value) {
        if (index == 0) {
            y0 = value;
        }
        else {
            y1 = value;
        }
    }

    @Override
    public int indexOfX(double x) {
        if (x == x0) {
            return 0;
        }
        else if (x == x1) {
            return 1;
        }
        else {
            return -1;
        }
    }

    @Override
    public int indexOfY(double y) {
        if (y == y0) {
            return 0;
        }
        else if (y == y1) {
            return 1;
        }
        else {
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
    public void insert(double x, double y) {

    }
}