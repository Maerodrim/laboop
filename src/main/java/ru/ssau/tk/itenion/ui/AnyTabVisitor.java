package ru.ssau.tk.itenion.ui;

public interface AnyTabVisitor{
    default TabController.AnyTabState anyState(){
        return TabController.anyTabState;
    }

    void visit(TabController.AnyTabState anyTabState);
}

