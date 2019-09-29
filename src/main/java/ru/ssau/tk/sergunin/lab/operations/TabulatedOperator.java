package ru.ssau.tk.sergunin.lab.operations;

import ru.ssau.tk.sergunin.lab.functions.*;
import ru.ssau.tk.sergunin.lab.functions.factory.ArrayTabulatedFunctionFactory;
import ru.ssau.tk.sergunin.lab.functions.factory.TabulatedFunctionFactory;

public abstract class TabulatedOperator {
    protected TabulatedFunctionFactory factory;
    protected TabulatedFunction shift;

    public TabulatedOperator() {
        this.factory = new ArrayTabulatedFunctionFactory();
    }

    public TabulatedOperator(TabulatedFunctionFactory factory) {
        this.factory = factory;
    }

    public TabulatedFunctionFactory getFactory() {
        return factory;
    }

    public void setFactory(TabulatedFunctionFactory factory) {
        this.factory = factory;
    }

    public TabulatedFunction doOperation(TabulatedFunction function, BiOperation operation) {
        double[] xValues = new double[function.getCount()];
        double[] yValues = new double[function.getCount()];
        Point[] arrayPoint = TabulatedFunctionOperationService.asPoints(function);
        for (int i = 0; i < xValues.length; i++) {
            if (i < (xValues.length - 1)) {
                yValues[i] = operation.apply(arrayPoint[i], arrayPoint[i + 1]);
                xValues[i] = arrayPoint[i].x;
            } else {
                yValues[i] = yValues[i - 1];
                xValues[i] = arrayPoint[i].x;
            }
        }
        return factory.create(xValues, yValues);
    }

    public TabulatedFunction derive(TabulatedFunction function) {
        return doOperation(function, ((u, v) -> (v.y - u.y) / (v.x - u.x)));
    }

    public TabulatedFunction integrate(TabulatedFunction function) {
        return doOperation(function, new BiOperation() {
            double sum = 0;
            boolean isFirst = true;

            @Override
            public double apply(Point u, Point v) {
                if (isFirst) {
                    shift = new ArrayTabulatedFunction(new ZeroFunction(), function.getX(0), function.getX(function.getCount() - 1), function.getCount());
                    isFirst = false;
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