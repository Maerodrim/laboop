package ru.ssau.tk.sergunin.lab.ui;

import javafx.scene.control.Tab;
import ru.ssau.tk.sergunin.lab.functions.TabulatedFunction;

import java.util.Map;

public interface TabulatedFunctionAccessible {
    boolean connectMap(Map<Tab, TabulatedFunction> functionMap);
}
