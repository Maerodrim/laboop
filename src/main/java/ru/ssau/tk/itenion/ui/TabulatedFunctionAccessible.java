package ru.ssau.tk.itenion.ui;

import javafx.scene.control.Tab;
import ru.ssau.tk.itenion.functions.MathFunction;
import ru.ssau.tk.itenion.functions.tabulatedFunctions.TabulatedFunction;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public interface TabulatedFunctionAccessible {
    default void connectTabulatedFunctionMap(Map<Tab, TabulatedFunction> tabulatedFunctionMap) {
        AtomicBoolean isNotSuitableSet = new AtomicBoolean(true);
        TabVisitor.state.accept(new TabVisitor() {
            @Override
            void visit(TabController.TFState tfState) {
                tabulatedFunctionMap.forEach((tab, function) -> {
                    if (function != tfState.getFunction() &&
                            !function.isStrict() &&
                            tfState.getFunction().isCanBeComposed(function)) {
                        putFunction(tab, function);
                    }
                });
            }

            @Override
            void visit(TabController.VMFState vmfState) {
                tabulatedFunctionMap.forEach(this::putFunction);
            }

            void putFunction(Tab tab, TabulatedFunction function){
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

    Map<String, MathFunction> getTabulatedFunctionMap();

    void updateTabulatedFunctionNode();
}
