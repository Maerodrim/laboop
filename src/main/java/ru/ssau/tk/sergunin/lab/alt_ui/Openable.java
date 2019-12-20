package ru.ssau.tk.sergunin.lab.alt_ui;

import javafx.stage.Stage;
import ru.ssau.tk.sergunin.lab.functions.factory.TabulatedFunctionFactory;

public interface Openable {
    Stage getStage();
    void setStage(Stage stage);
    void setFactory(TabulatedFunctionFactory factory);
    void setParentController(Openable controller);
}
