package ru.ssau.tk.sergunin.lab.operations;

import ru.ssau.tk.sergunin.lab.functions.MathFunction;
import ru.ssau.tk.sergunin.lab.functions.TabulatedFunction;
import ru.ssau.tk.sergunin.lab.functions.factory.ArrayTabulatedFunctionFactory;
import ru.ssau.tk.sergunin.lab.functions.factory.TabulatedFunctionFactory;

public class TabulatedDifferentialOperator implements DifferentialOperator<TabulatedFunction> {
    TabulatedFunctionFactory factory;

    public TabulatedDifferentialOperator() {
        this.factory = new ArrayTabulatedFunctionFactory();
    }

    public void setFactory(TabulatedFunctionFactory factory) {
        this.factory = factory;
    }

    public TabulatedFunctionFactory getFactory() {
        return factory;
    }

    @Override
    public double apply(double x) {
        return 0;
    }


    @Override
    public TabulatedFunction derive(TabulatedFunction function) {
        double[] xValues = new double[function.getCount()];
        double[] yValues = new double[function.getCount()];
        TabulatedFunctionOperationService.asPoints(factory);
        return null;
    }
}
