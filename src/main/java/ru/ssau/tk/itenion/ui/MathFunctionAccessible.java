package ru.ssau.tk.itenion.ui;

import ru.ssau.tk.itenion.functions.MathFunction;

import java.util.Map;

public interface MathFunctionAccessible {
    default void connectMathFunctionMap(Map<String, MathFunction> functionMap) {
        setMathFunctionsMap(functionMap);
        setMathFunctionNode();
    }

    void setMathFunctionNode();

    void setMathFunctionsMap(Map<String, MathFunction> functionMap);
}
