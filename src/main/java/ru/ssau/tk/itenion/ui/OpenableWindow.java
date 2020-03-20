package ru.ssau.tk.itenion.ui;

import javafx.stage.Stage;

interface OpenableWindow {
    Stage getStage();

    void setStage(Stage stage);
}
