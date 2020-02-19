package ru.ssau.tk.sergunin.lab.functions.tabulatedFunctions;

import ru.ssau.tk.sergunin.lab.functions.Insertable;
import ru.ssau.tk.sergunin.lab.functions.MathFunction;
import ru.ssau.tk.sergunin.lab.functions.Point;
import ru.ssau.tk.sergunin.lab.functions.Removable;
import ru.ssau.tk.sergunin.lab.operations.TabulatedDifferentialOperator;
import ru.ssau.tk.sergunin.lab.ui.ExplicitAccessControllable;

import java.util.Objects;

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

    @Override
    default String getName() {
        if (!Objects.isNull(getMathFunction())) {
            return getMathFunction().getName();
        } else {
            return "TF(x)";
        }
    }

    MathFunction getMathFunction();

    void setMathFunction(MathFunction math);

    @Override
    default MathFunction differentiate() {
        return new TabulatedDifferentialOperator().derive(this);
    }
}
