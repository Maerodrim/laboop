package ru.ssau.tk.itenion.ui;

public interface TabVisitorSimple extends TabVisitor {
    @Override
    default void visit(TabController.TFState tfState){}

    @Override
    default void visit(TabController.VMFState vmfState){}
}
