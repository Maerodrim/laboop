package ru.ssau.tk.itenion.ui;

import javafx.stage.Stage;
import ru.ssau.tk.itenion.functions.factory.TabulatedFunctionFactory;

interface OpenableWindow {
    Stage getStage();

    void setStage(Stage stage);
}
