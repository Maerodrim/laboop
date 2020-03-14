package ru.ssau.tk.itenion.ui;

public interface AnyTabVisitor{
    default AnyTabHolderState anyState(){
        return TabController.anyTabState;
    }

    void visit(TabController.AnyTabState anyTabState);
}

