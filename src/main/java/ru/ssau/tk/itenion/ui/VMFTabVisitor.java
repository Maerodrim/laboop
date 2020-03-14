package ru.ssau.tk.itenion.ui;

@FunctionalInterface
public interface VMFTabVisitor extends TabVisitor {
    void visit(TabController.VMFState vmfState);

    default void visit(TabController.TFState tfState){}
}
