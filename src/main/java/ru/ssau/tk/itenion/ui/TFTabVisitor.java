package ru.ssau.tk.itenion.ui;

@FunctionalInterface
public interface TFTabVisitor extends TabVisitor {
    void visit(TabController.TFState tfState);

    default void visit(TabController.VMFState vmfState) {
    }
}
