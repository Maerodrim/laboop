package ru.ssau.tk.itenion.ui;

public interface AnyTabHolderState {
    void accept(AnyTabVisitor tabVisitor);
}