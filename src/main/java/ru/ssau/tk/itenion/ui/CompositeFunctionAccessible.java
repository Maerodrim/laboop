package ru.ssau.tk.itenion.ui;

import ru.ssau.tk.itenion.functions.MathFunction;

import java.util.Map;

public interface CompositeFunctionAccessible {

    default void connectCompositeFunctionMap() {
        updateCompositeFunctionMap(((TableController) getParentController()).getCompositeFunctionMap());
        updateCompositeFunctionNode();
    }

    Openable getParentController();

    void updateCompositeFunctionNode();

    void updateCompositeFunctionMap(Map<String, MathFunction> compositeFunctionMap);
}
