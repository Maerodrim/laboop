package ru.ssau.tk.itenion.ui;

public interface PlotAccessible extends AnyTabVisitor{
    @Override
    default void visit(TabController.AnyTabHolderState anyTabHolderState){}

    default void plot(){
        anyState().plot();
    }
}
