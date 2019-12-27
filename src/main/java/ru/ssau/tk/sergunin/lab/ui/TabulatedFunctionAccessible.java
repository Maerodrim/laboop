package ru.ssau.tk.sergunin.lab.ui;

import ru.ssau.tk.sergunin.lab.functions.MathFunction;

import java.util.Map;

public interface TabulatedFunctionAccessible {
    default void connectTabulatedFunctionMap() {
        ((TableController) getParentController()).getTabulatedFunctionMap().forEach((tab, function) -> {
            if (function != ((TableController) getParentController()).getFunction() &&
                    !function.isStrict() &&
                    ((TableController) getParentController()).getFunction().isCanBeComposed(function)) {
                getTabulatedFunctionMap().put(tab.getText(), function);
            }
        });
        if (!getTabulatedFunctionMap().isEmpty()) {
            updateTabulatedFunctionNode();
        }
    }

    Openable getParentController();

    Map<String, ? super MathFunction> getTabulatedFunctionMap();

    void updateTabulatedFunctionNode();
}
