package ru.ssau.tk.sergunin.lab.operations;

import ru.ssau.tk.sergunin.lab.functions.TabulatedFunction;
import ru.ssau.tk.sergunin.lab.functions.factory.ArrayTabulatedFunctionFactory;
import ru.ssau.tk.sergunin.lab.functions.factory.TabulatedFunctionFactory;

public class TabulatedDifferentialOperator implements DifferentialOperator {
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
    public <TabulatedFunction> TabulatedFunction derive(TabulatedFunction function) {
        double[] xValues = new double[TabulatedFunctionOperationService.getCount];
        double[] yValues = new double[TabulatedFunctionOperationService.getCount];
        TabulatedFunctionOperationService.asPoints(factory);
        return null;
    }


    @Override
    public double apply(double x) {
        return 0;
    }
}
