package ru.ssau.tk.sergunin.lab.functions;

import com.fasterxml.jackson.annotation.JsonFormat;

public interface TabulatedFunction extends MathFunction, Iterable<Point>{
//    static TabulatedFunction join(TabulatedFunction firstFunction, TabulatedFunction secondFunction, TabulatedFunctionFactory factory) {
//        if (firstFunction.getCount() == 0) return secondFunction;
//        Point[] firstPoints = TabulatedFunctionOperationService.asPoints(firstFunction);
//        Point[] secondPoints = TabulatedFunctionOperationService.asPoints(secondFunction);
//        List<Point[]> points = new ArrayList<>();
//        points.add(firstPoints);
//        points.add(secondPoints);
//        double[] xValues = new double[firstPoints.length + secondPoints.length];
//        double[] yValues = new double[firstPoints.length + secondPoints.length];
//        int j = 0;
//        int k = 0;
//        boolean isFirst = false;
//        boolean isDoneForFirstFunction = false;
//        boolean isDoneForSecondFunction = false;
//        for (int i = 0; i < xValues.length; i++) {
//            if (!isDoneForFirstFunction && (isDoneForSecondFunction || firstPoints[j].x < secondPoints[k].x)) {
//                xValues[i] = firstPoints[j].x;
//                yValues[i] = firstPoints[j].y;
//                j++;
//            }
//            if (isFirst && !isDoneForFirstFunction && j - (secondPoints[secondPoints.length - 1].x < firstPoints[secondPoints.length - 1].x ? 1 : 0) == firstPoints.length) {
//                isDoneForFirstFunction = true;
//                isFirst = false;
//                continue;
//            }
//            if (!isDoneForSecondFunction && (isDoneForFirstFunction || firstPoints[j].x > secondPoints[k].x)) {
//                xValues[i] = secondPoints[k].x;
//                yValues[i] = secondPoints[k].y;
//                k++;
//            }
//            if (!isDoneForSecondFunction && k - (secondPoints[secondPoints.length - 1].x > firstPoints[secondPoints.length - 1].x ? 1: 0) == secondPoints.length)
//                isDoneForSecondFunction = true;
//        }
//        return factory.create(xValues, yValues);
//    }

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

    void offerStrict(boolean isStrict);

    void offerUnmodifiable(boolean isUnmodifiable);
}
