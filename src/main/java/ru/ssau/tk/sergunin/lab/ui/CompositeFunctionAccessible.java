package ru.ssau.tk.sergunin.lab.ui;

import ru.ssau.tk.sergunin.lab.functions.MathFunction;

import java.util.Map;

public interface CompositeFunctionAccessible {

    default void connectCompositeFunctionMap() {
        updateCompositeFunctionMap(((TableController) getParentController()).getCompositeFunctionMap());
        updateCompositeFunctionNode();
    }

    Openable getParentController();

    void updateCompositeFunctionNode();

    void updateCompositeFunctionMap(Map<String, MathFunction> functionMap);
}
