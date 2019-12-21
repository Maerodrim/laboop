package ru.ssau.tk.sergunin.lab.operations;

import ru.ssau.tk.sergunin.lab.alt_ui.SelectableOperator;
import ru.ssau.tk.sergunin.lab.concurrent.SynchronizedTabulatedFunction;
import ru.ssau.tk.sergunin.lab.functions.TabulatedFunction;
import ru.ssau.tk.sergunin.lab.functions.factory.TabulatedFunctionFactory;

@SelectableOperator(name = "")
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
    @SelectableOperator(name = "Derive", priority = 1)
    public TabulatedFunction derive(TabulatedFunction function) {
        return super.derive(function);
    }

    public TabulatedFunction deriveSynchronously(TabulatedFunction function) {
        if (!(function instanceof SynchronizedTabulatedFunction))
            function = new SynchronizedTabulatedFunction(function);
        return ((SynchronizedTabulatedFunction) function).doSynchronously(this::derive);
    }
}
