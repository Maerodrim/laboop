package ru.ssau.tk.itenion.ui;

import javafx.scene.control.Tab;
import ru.ssau.tk.itenion.functions.MathFunction;
import ru.ssau.tk.itenion.functions.tabulatedFunctions.TabulatedFunction;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public interface TabulatedFunctionAccessible {
    default void connectTabulatedFunctionMap(Map<Tab, TabulatedFunction> tabulatedFunctionMap) {
        AtomicBoolean isNotSuitableSet = new AtomicBoolean(true);
        TabController.state.accept(new TabVisitor() {
            @Override
            public void visit(TabController.TFState tfState) {
                tabulatedFunctionMap.forEach((tab, function) -> {
                    if (function != tfState.getFunction() &&
                            !function.isStrict() &&
                            tfState.getFunction().isCanBeComposed(function)) {
                        putFunction(tab, function);
                    }
                });
            }

            @Override
            public void visit(TabController.VMFState vmfState) {
                tabulatedFunctionMap.forEach(this::putFunction);
            }

            void putFunction(Tab tab, TabulatedFunction function){
                String name = function.isMathFunctionExist()
                        ? function.getName()
                        : tab.getText();
                getFittingTabulatedFunctionsMap().put(name, function);
                isNotSuitableSet.set(false);
            }
        });
        if (isNotSuitableSet.get()) {
            getFittingTabulatedFunctionsMap().clear();
        }
        updateTabulatedFunctionNode();
    }

    Map<String, MathFunction> getFittingTabulatedFunctionsMap();

    void updateTabulatedFunctionNode();
}
