package ru.ssau.tk.itenion.ui;

public interface AnyTabVisitor{
    default TabController.AnyTabHolderState anyState(){
        return TabController.anyTabHolderState;
    }

    void visit(TabController.AnyTabHolderState anyTabHolderState);
}

