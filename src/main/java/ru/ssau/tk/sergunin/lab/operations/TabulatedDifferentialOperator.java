package ru.ssau.tk.sergunin.lab.operations;

import ru.ssau.tk.sergunin.lab.functions.Point;
import ru.ssau.tk.sergunin.lab.functions.TabulatedFunction;
import ru.ssau.tk.sergunin.lab.functions.factory.ArrayTabulatedFunctionFactory;
import ru.ssau.tk.sergunin.lab.functions.factory.TabulatedFunctionFactory;

public class TabulatedDifferentialOperator implements DifferentialOperator<TabulatedFunction> {
    private TabulatedFunctionFactory factory;

    public TabulatedDifferentialOperator() {
        this.factory = new ArrayTabulatedFunctionFactory();
    }

    public TabulatedDifferentialOperator(TabulatedFunctionFactory factory) {
        this.factory = factory;
    }

    public void setFactory(TabulatedFunctionFactory factory) {
        this.factory = factory;
    }

    public TabulatedFunctionFactory getFactory() {
        return factory;
    }

    @Override
    public TabulatedFunction derive(TabulatedFunction function) {
        double[] xValues = new double[function.getCount()];
        double[] yValues = new double[function.getCount()];
        Point[] arrayPoint = TabulatedFunctionOperationService.asPoints(function);
        for (int i = 0; i < xValues.length; i++) {
            if (i < (xValues.length - 1)) {
                yValues[i] = (arrayPoint[i + 1].y - arrayPoint[i].y) / (arrayPoint[i + 1].x - arrayPoint[i].x);
                xValues[i] = arrayPoint[i].x;
            } else {
                yValues[i] = yValues[i - 1];
                xValues[i] = arrayPoint[i].x;
            }
        }

        return factory.create(xValues, yValues);
    }

    @Override
    public double apply(double x) {
        return 0;
    }
}
