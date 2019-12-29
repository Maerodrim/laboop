package ru.ssau.tk.sergunin.lab.functions;

import ru.ssau.tk.sergunin.lab.ui.ExplicitAccessControllable;

public interface TabulatedFunction extends MathFunction, Iterable<Point>, ExplicitAccessControllable, Insertable, Removable {

    int getCount();

    double getX(int index);

    double getY(int index);

    void setY(int index, double value);

    void setY(TabulatedFunction function);

    int indexOfX(double x);

    int indexOfY(double y);

    double leftBound();

    double rightBound();

    TabulatedFunction copy();

    boolean isStrict();

    boolean isUnmodifiable();

    TabulatedFunction unwrap();

    default boolean isCanBeComposed(TabulatedFunction function) {
        return leftBound() == function.leftBound()
                && rightBound() == function.rightBound()
                && getCount() == function.getCount();
    }

}
