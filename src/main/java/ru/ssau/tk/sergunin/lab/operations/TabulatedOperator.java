package ru.ssau.tk.sergunin.lab.operations;

import ru.ssau.tk.sergunin.lab.functions.*;
import ru.ssau.tk.sergunin.lab.functions.factory.ArrayTabulatedFunctionFactory;
import ru.ssau.tk.sergunin.lab.functions.factory.TabulatedFunctionFactory;
import ru.ssau.tk.sergunin.lab.functions.powerFunctions.ConstantFunction;
import ru.ssau.tk.sergunin.lab.functions.powerFunctions.ZeroFunction;
import ru.ssau.tk.sergunin.lab.functions.tabulatedFunctions.ArrayTabulatedFunction;
import ru.ssau.tk.sergunin.lab.functions.tabulatedFunctions.TabulatedFunction;

public abstract class TabulatedOperator {
    protected TabulatedFunctionFactory factory;
    protected TabulatedFunction shift;
    private Boolean isFirst = true;

    protected TabulatedOperator() {
        this.factory = new ArrayTabulatedFunctionFactory();
    }

    protected TabulatedOperator(TabulatedFunctionFactory factory) {
        this.factory = factory;
    }

    protected TabulatedFunctionFactory getFactory() {
        return factory;
    }

    protected void setFactory(TabulatedFunctionFactory factory) {
        this.factory = factory;
    }

    private TabulatedFunction doOperation(int from, int to, double[] xValues, double[] yValues, Point[] arrayPoint, BiOperation operation) {
        for (int i = from; i < to - 2; i++) {
            yValues[i + 1 - from] = operation.apply(arrayPoint[i - from], arrayPoint[i + 1 - from], arrayPoint[i + 2 - from]);
            xValues[i + 1 - from] = arrayPoint[i + 1 - from].x;
        }
        xValues[0] = arrayPoint[0].x;
        yValues[0] = yValues[1];
        xValues[xValues.length - 1] = arrayPoint[xValues.length - 1].x;
        yValues[xValues.length - 1] = yValues[xValues.length - 2];
        return factory.create(xValues, yValues);
    }

    protected TabulatedFunction derive(TabulatedFunction function) {
        return doOperation(0, function.getCount(), new double[function.getCount()], new double[function.getCount()], TabulatedFunctionOperationService.asPoints(function), (u, v, z) -> (z.y - u.y) / (z.x - u.x));
    }

    protected TabulatedFunction integrate(TabulatedFunction function, int from, int to, double[] xValues, double[] yValues, Point[] points) {
        return doOperation(from, to, xValues, yValues, points, new BiOperation() {
            double sum = 0;

            @Override
            public double apply(Point u, Point v, Point z) {
                if (isFirst) {
                    sum += v.y * (u.x - v.x);
                    shift = new ArrayTabulatedFunction(new ZeroFunction(), function.getX(0), function.getX(function.getCount() - 1), function.getCount());
                    isFirst = false;
                    return sum;
                }
                if (v.x > 0 && u.x < 0) {
                    shift = new ArrayTabulatedFunction(new ConstantFunction(sum), function.getX(0), function.getX(function.getCount() - 1), function.getCount());
                }
                sum += (v.y + u.y) * (v.x - u.x) / 2;
                return sum;
            }
        });
    }

    @FunctionalInterface
    private interface BiOperation {
        double apply(Point u, Point v, Point z);
    }
}