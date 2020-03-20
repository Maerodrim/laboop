package ru.ssau.tk.itenion.ui;

import ru.ssau.tk.itenion.functions.MathFunction;

import java.util.Map;

public interface CompositeFunctionAccessible {

    default void bindCompositeFunctionMap(Map<String, MathFunction> compositeFunctionMap) {
        updateCompositeFunctionMap(compositeFunctionMap);
        updateCompositeFunctionNode();
    }

    void updateCompositeFunctionNode();

    void updateCompositeFunctionMap(Map<String, MathFunction> compositeFunctionMap);
}
