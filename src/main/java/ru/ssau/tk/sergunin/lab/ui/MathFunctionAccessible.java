package ru.ssau.tk.sergunin.lab.ui;

import javafx.scene.control.ComboBox;
import ru.ssau.tk.sergunin.lab.functions.MathFunction;

import java.util.Map;

public interface MathFunctionAccessible {
    default void connectMap(Map<String, MathFunction> functionMap) {
        setFunctionMap(functionMap);
        getComboBox().getItems().addAll(functionMap.keySet());
        getComboBox().setValue(getComboBox().getItems().get(0));
    }

    ComboBox<String> getComboBox();

    void setFunctionMap(Map<String, MathFunction> functionMap);
}
