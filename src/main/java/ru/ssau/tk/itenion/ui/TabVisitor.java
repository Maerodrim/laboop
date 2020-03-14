package ru.ssau.tk.itenion.ui;

public interface TabVisitor {

    void visit(TabController.TFState tfState);

    void visit(TabController.VMFState vmfState);

    default AnyTabHolderState anyState(){
        return TabController.anyTabState;
    }

    default TabHolderState state(){
        return TabController.state;
    }
}
