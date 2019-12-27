package ru.ssau.tk.sergunin.lab.ui;

import ru.ssau.tk.sergunin.lab.functions.MathFunction;

import java.util.Map;

public interface MathFunctionAccessible {
    default void connectMathFunctionMap(Map<String, MathFunction> functionMap) {
        setMathFunctionMap(functionMap);
        setMathFunctionNode();
    }

    void setMathFunctionNode();

    void setMathFunctionMap(Map<String, MathFunction> functionMap);
}
