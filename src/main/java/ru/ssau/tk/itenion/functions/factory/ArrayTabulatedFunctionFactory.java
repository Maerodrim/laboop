package ru.ssau.tk.itenion.functions.factory;

import javafx.collections.ObservableList;
import ru.ssau.tk.itenion.functions.MathFunction;
import ru.ssau.tk.itenion.functions.Point;
import ru.ssau.tk.itenion.functions.tabulatedFunctions.ArrayTabulatedFunction;
import ru.ssau.tk.itenion.functions.tabulatedFunctions.TabulatedFunction;
import ru.ssau.tk.itenion.ui.ConnectableItem;
import ru.ssau.tk.itenion.ui.Item;

@ConnectableItem(name = "Array tabulated function", priority = 1, type = Item.FACTORY)
public class ArrayTabulatedFunctionFactory implements TabulatedFunctionFactory {

    public Class<ArrayTabulatedFunction> getTabulatedFunctionClass() {
        return ArrayTabulatedFunction.class;
    }

    @Override
    public TabulatedFunction create(double[] xValues, double[] yValues) {
        return new ArrayTabulatedFunction(xValues, yValues);
    }

    @Override
    public TabulatedFunction create(ObservableList<Point> observableList) {
        return new ArrayTabulatedFunction(observableList);
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
