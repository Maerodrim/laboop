package ru.ssau.tk.itenion.ui;

import ru.ssau.tk.itenion.functions.AnyTabHolderMathFunction;
import ru.ssau.tk.itenion.enums.State;

public interface TabHolderState {
    void changeState(State state);

    AnyTabHolderMathFunction getFunction();

    void accept(TabVisitor tabVisitor);
}
