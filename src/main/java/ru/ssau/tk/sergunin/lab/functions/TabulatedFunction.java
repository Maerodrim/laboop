package ru.ssau.tk.sergunin.lab.functions;

public interface TabulatedFunction extends MathFunction {
    int getCount();

    double getX(int index) throws RuntimeException;

    double getY(int index) throws RuntimeException;

    void setY(int index, double value) throws RuntimeException;

    int indexOfX(double x);

    int indexOfY(double y);

    double leftBound();

    double rightBound();
}
