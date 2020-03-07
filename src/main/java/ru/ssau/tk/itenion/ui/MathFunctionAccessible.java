package ru.ssau.tk.itenion.ui;

import javafx.concurrent.Task;
import ru.ssau.tk.itenion.functions.MathFunction;

import java.util.Map;

public interface MathFunctionAccessible {
    default void connectMathFunctionMap(Map<String, MathFunction> functionMap) {
        setMathFunctionMap(functionMap);
        setMathFunctionNode();
    }

    void setMathFunctionNode();

    void setMathFunctionMap(Map<String, MathFunction> functionMap);
}
