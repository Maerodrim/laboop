package ru.ssau.tk.itenion.ui;

import javafx.collections.ObservableList;
import javafx.scene.layout.AnchorPane;
import ru.ssau.tk.itenion.functions.Function;
import ru.ssau.tk.itenion.functions.Point;
import ru.ssau.tk.itenion.functions.multipleVariablesFunctions.vectorFunctions.VMF;
import ru.ssau.tk.itenion.functions.tabulatedFunctions.TabulatedFunction;

public interface TabHolder {
    void createTab(ObservableList<Point> list);

    void createTab(TabulatedFunction function);

    void createTab(AnchorPane vmfPane, VMF vmf);

    Function getFunction();
}
