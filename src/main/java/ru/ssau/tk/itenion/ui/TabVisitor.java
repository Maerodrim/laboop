package ru.ssau.tk.itenion.ui;

public abstract class TabVisitor {
    public static TabHolderState state;

    abstract void visit(TabController.TFState tfState);

    abstract void visit(TabController.VMFState vmf);
}
