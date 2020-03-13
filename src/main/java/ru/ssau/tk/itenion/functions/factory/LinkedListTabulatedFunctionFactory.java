package ru.ssau.tk.itenion.functions.factory;

import javafx.collections.ObservableList;
import ru.ssau.tk.itenion.functions.MathFunction;
import ru.ssau.tk.itenion.functions.Point;
import ru.ssau.tk.itenion.functions.tabulatedFunctions.LinkedListTabulatedFunction;
import ru.ssau.tk.itenion.functions.tabulatedFunctions.TabulatedFunction;
import ru.ssau.tk.itenion.ui.ConnectableItem;
import ru.ssau.tk.itenion.ui.Item;

@ConnectableItem(name = "Linked list tabulated function", priority = 2, type = Item.FACTORY)
public class LinkedListTabulatedFunctionFactory implements TabulatedFunctionFactory {

    @Override
    public TabulatedFunction create(double[] xValues, double[] yValues) {
        return new LinkedListTabulatedFunction(xValues, yValues);
    }

    @Override
    public TabulatedFunction create(ObservableList<Point> observableList) {
        return new LinkedListTabulatedFunction(observableList);
    }

    @Override
    public TabulatedFunction create(MathFunction source, double xFrom, double xTo, int count) {
        return new LinkedListTabulatedFunction(source, xFrom, xTo, count);
    }

    @Override
    public TabulatedFunction getIdentity() {
        return LinkedListTabulatedFunction.getIdentity();
    }

    @Override
    public Class<?> getTabulatedFunctionClass() {
        return LinkedListTabulatedFunction.class;
    }
}
