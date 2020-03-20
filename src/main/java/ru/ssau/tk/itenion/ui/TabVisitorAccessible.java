package ru.ssau.tk.itenion.ui;

public interface TabVisitorAccessible extends TabVisitor {
    @Override
    default void visit(TabController.TFState tfState) {
    }

    @Override
    default void visit(TabController.VMFState vmfState) {
    }
}
