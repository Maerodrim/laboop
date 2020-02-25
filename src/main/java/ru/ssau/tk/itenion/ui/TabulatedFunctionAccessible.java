package ru.ssau.tk.itenion.ui;

import ru.ssau.tk.itenion.functions.MathFunction;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public interface TabulatedFunctionAccessible {
    default void connectTabulatedFunctionMap() {
        AtomicBoolean isNotSuitableSet = new AtomicBoolean(true);
        ((TableController) getParentController()).getTabulatedFunctionMap().forEach((tab, function) -> {
            if (function != ((TableController) getParentController()).getFunction() &&
                    !function.isStrict() &&
                    ((TableController) getParentController()).getFunction().isCanBeComposed(function)) {
                String name = function.isMathFunctionExist()
                        ? function.getName()
                        : tab.getText();
                getTabulatedFunctionMap().put(name, function);
                isNotSuitableSet.set(false);
            }
        });
        if (isNotSuitableSet.get()) {
            getTabulatedFunctionMap().clear();
        }
        updateTabulatedFunctionNode();
    }

    Openable getParentController();

    Map<String, ? super MathFunction> getTabulatedFunctionMap();

    void updateTabulatedFunctionNode();
}
