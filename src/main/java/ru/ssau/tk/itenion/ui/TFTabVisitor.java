package ru.ssau.tk.itenion.ui;

public abstract class TFTabVisitor extends TabVisitor{
    abstract void visit(TabController.TFState tfState);

    void visit(TabController.VMFState vmf){}
}
