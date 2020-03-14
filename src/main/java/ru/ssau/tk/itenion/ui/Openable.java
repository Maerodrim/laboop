package ru.ssau.tk.itenion.ui;

import javafx.stage.Stage;
import ru.ssau.tk.itenion.functions.factory.TabulatedFunctionFactory;

interface Openable {
    Stage getStage();

    void setStage(Stage stage);

    //void setFactory(TabulatedFunctionFactory factory);
}
