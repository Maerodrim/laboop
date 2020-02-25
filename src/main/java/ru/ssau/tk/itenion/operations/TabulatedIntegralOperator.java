package ru.ssau.tk.itenion.operations;

import ru.ssau.tk.itenion.functions.factory.ArrayTabulatedFunctionFactory;
import ru.ssau.tk.itenion.functions.factory.TabulatedFunctionFactory;
import ru.ssau.tk.itenion.functions.tabulatedFunctions.TabulatedFunction;
import ru.ssau.tk.itenion.ui.ConnectableItem;
import ru.ssau.tk.itenion.ui.Item;

@ConnectableItem(name = "Integrate", priority = 10, type = Item.OPERATOR)
public class TabulatedIntegralOperator extends TabulatedOperator implements IntegralOperator<TabulatedFunction> {
    public TabulatedIntegralOperator() {
        super();
    }

    public TabulatedIntegralOperator(TabulatedFunctionFactory factory) {
        super(factory);
    }

    protected TabulatedFunctionFactory getFactory() {
        return super.getFactory();
    }

    protected void setFactory(TabulatedFunctionFactory factory) {
        super.setFactory(factory);
    }

    @Override
    @ConnectableItem(name = "Integrate", priority = 11, type = Item.OPERATOR)
    public TabulatedFunction integrate(TabulatedFunction function) {
        return new TabulatedFunctionOperationService(new ArrayTabulatedFunctionFactory())
                .subtract(super.integrate(function, 0, function.getCount(), new double[function.getCount()],
                        new double[function.getCount()], TabulatedFunctionOperationService.asPoints(function)), shift);
    }

}
