package ru.ssau.tk.sergunin.lab.functions.factory;

import org.jetbrains.annotations.NotNull;
import ru.ssau.tk.sergunin.lab.functions.*;
import java.util.Iterator;

public interface TabulatedFunctionFactory {
    TabulatedFunction create(double[] xValues, double[] yValues);

    default  TabulatedFunction createStrict(double[] xValues, double[] yValues) {
        return new StrictTabulatedFunction(TabulatedFunctionFactory.this.create(xValues,yValues));
    }
}
