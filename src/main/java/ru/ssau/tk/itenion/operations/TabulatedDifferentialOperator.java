package ru.ssau.tk.itenion.operations;

import ru.ssau.tk.itenion.concurrent.SynchronizedTabulatedFunction;
import ru.ssau.tk.itenion.functions.factory.TabulatedFunctionFactory;
import ru.ssau.tk.itenion.functions.tabulatedFunctions.TabulatedFunction;
import ru.ssau.tk.itenion.ui.ConnectableItem;
import ru.ssau.tk.itenion.ui.Item;

@ConnectableItem(name = "", type = Item.OPERATOR)
public class TabulatedDifferentialOperator extends TabulatedOperator implements DifferentialOperator<TabulatedFunction> {

    public TabulatedDifferentialOperator() {
        super();
    }

    public TabulatedDifferentialOperator(TabulatedFunctionFactory factory) {
        super(factory);
    }

    protected TabulatedFunctionFactory getFactory() {
        return super.getFactory();
    }

    protected void setFactory(TabulatedFunctionFactory factory) {
        super.setFactory(factory);
    }

    @Override
    @ConnectableItem(name = "Numerical derive", priority = 1, type = Item.OPERATOR)
    public TabulatedFunction derive(TabulatedFunction function) {
        return super.derive(function);
    }

    public TabulatedFunction deriveSynchronously(TabulatedFunction function) {
        if (!(function instanceof SynchronizedTabulatedFunction))
            function = new SynchronizedTabulatedFunction(function);
        return ((SynchronizedTabulatedFunction) function).doSynchronously(this::derive);
    }
}
