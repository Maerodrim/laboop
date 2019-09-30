package ru.ssau.tk.sergunin.lab.operations;

import ru.ssau.tk.sergunin.lab.functions.*;
import ru.ssau.tk.sergunin.lab.functions.factory.ArrayTabulatedFunctionFactory;
import ru.ssau.tk.sergunin.lab.functions.factory.TabulatedFunctionFactory;

public abstract class TabulatedOperator {
    protected TabulatedFunctionFactory factory;
    protected TabulatedFunction shift;

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

    private TabulatedFunction doOperation(TabulatedFunction function, BiOperation operation) {
        double[] xValues = new double[function.getCount()];
        double[] yValues = new double[function.getCount()];
        Point[] arrayPoint = TabulatedFunctionOperationService.asPoints(function);
        for (int i = 0; i < xValues.length; i++) {
            yValues[i] = operation.apply(i != 0 ? arrayPoint[i - 1] : new Point(2 * arrayPoint[i].x - arrayPoint[i + 1].x, function.apply(2 * arrayPoint[i].x - arrayPoint[i + 1].x)), arrayPoint[i]);
            xValues[i] = arrayPoint[i].x;
        }
        return factory.create(xValues, yValues);
    }

    protected TabulatedFunction derive(TabulatedFunction function) {
        return doOperation(function, ((u, v) -> (v.y - u.y) / (v.x - u.x)));
    }

    protected TabulatedFunction integrate(TabulatedFunction function) {
        return doOperation(function, new BiOperation() {
            double sum = 0;
            boolean isFirst = true;

            @Override
            public double apply(Point u, Point v) {
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
        double apply(Point u, Point v);
    }
}