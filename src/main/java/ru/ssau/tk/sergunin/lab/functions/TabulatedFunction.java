package ru.ssau.tk.sergunin.lab.functions;

import ru.ssau.tk.sergunin.lab.functions.factory.TabulatedFunctionFactory;
import ru.ssau.tk.sergunin.lab.operations.TabulatedFunctionOperationService;

public interface TabulatedFunction extends MathFunction, Iterable<Point> {
    static TabulatedFunction join(TabulatedFunction firstFunction, TabulatedFunction secondFunction, TabulatedFunctionFactory factory) {
        if (firstFunction.getCount() == 0) return secondFunction;
        Point[] firstPoints = TabulatedFunctionOperationService.asPoints(firstFunction);
        Point[] secondPoints = TabulatedFunctionOperationService.asPoints(secondFunction);
        double[] xValues = new double[firstPoints.length + secondPoints.length];
        double[] yValues = new double[firstPoints.length + secondPoints.length];
        int j = 0;
        int k = 0;
        boolean isFirst = true;
        boolean isDoneForFirstFunction = false;
        boolean isDoneForSecondFunction = false;
        for (int i = 0; i < xValues.length; i++) {
            if (!isDoneForFirstFunction && (isDoneForSecondFunction || firstPoints[j].x < secondPoints[k].x)) {
                xValues[i] = firstPoints[j].x;
                yValues[i] = firstPoints[j].y;
                j++;
            }
            if (isFirst && !isDoneForFirstFunction && j - (secondPoints[secondPoints.length - 1].x < firstPoints[secondPoints.length - 1].x ? 1 : 0) == firstPoints.length) {
                isDoneForFirstFunction = true;
                isFirst = false;
                continue;
            }
            if (!isDoneForSecondFunction && (isDoneForFirstFunction || firstPoints[j].x > secondPoints[k].x)) {
                xValues[i] = secondPoints[k].x;
                yValues[i] = secondPoints[k].y;
                k++;
            }
            if (!isDoneForSecondFunction && k - (secondPoints[secondPoints.length - 1].x > firstPoints[secondPoints.length - 1].x ? 1 : 0) == secondPoints.length)
                isDoneForSecondFunction = true;
        }
        return factory.create(xValues, yValues);
    }

    int getCount();

    double getX(int index);

    double getY(int index);

    void setY(int index, double value);

    int indexOfX(double x);

    int indexOfY(double y);

    double leftBound();

    double rightBound();

}
