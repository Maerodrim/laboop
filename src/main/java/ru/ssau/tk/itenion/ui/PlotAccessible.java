package ru.ssau.tk.itenion.ui;

public interface PlotAccessible extends AnyTabVisitor{
    @Override
    default void visit(TabController.AnyTabState anyTabState){}

    default void plot(){
        anyState().plot();
    }
}
