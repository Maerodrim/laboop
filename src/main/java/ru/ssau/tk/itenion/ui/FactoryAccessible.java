package ru.ssau.tk.itenion.ui;

import ru.ssau.tk.itenion.functions.factory.TabulatedFunctionFactory;

public interface FactoryAccessible extends AnyTabVisitor {
    @Override
    default void visit(TabController.AnyTabState anyTabState) {
    }

    default TabulatedFunctionFactory factory() {
        return anyState().getFactory();
    }
}
