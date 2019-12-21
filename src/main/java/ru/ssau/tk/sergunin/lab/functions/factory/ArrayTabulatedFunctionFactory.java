package ru.ssau.tk.sergunin.lab.functions.factory;

import ru.ssau.tk.sergunin.lab.functions.ArrayTabulatedFunction;
import ru.ssau.tk.sergunin.lab.functions.MathFunction;
import ru.ssau.tk.sergunin.lab.functions.TabulatedFunction;
import ru.ssau.tk.sergunin.lab.ui.Item;
import ru.ssau.tk.sergunin.lab.ui.SelectableItem;

@SelectableItem(name = "Array tabulated function", priority = 1, type = Item.FACTORY)
public class ArrayTabulatedFunctionFactory implements TabulatedFunctionFactory {

    public Class<ArrayTabulatedFunction> getTabulatedFunctionClass() {
        return ArrayTabulatedFunction.class;
    }

    @Override
    public TabulatedFunction create(double[] xValues, double[] yValues) {
        return new ArrayTabulatedFunction(xValues, yValues);
    }

    @Override
    public TabulatedFunction create(MathFunction source, double xFrom, double xTo, int count) {
        return new ArrayTabulatedFunction(source, xFrom, xTo, count);
    }

    @Override
    public TabulatedFunction getIdentity() {
        return ArrayTabulatedFunction.getIdentity();
    }
}
