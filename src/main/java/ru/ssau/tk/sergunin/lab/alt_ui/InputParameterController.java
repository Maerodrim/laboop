package ru.ssau.tk.sergunin.lab.alt_ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class InputParameterController {
    Stage stage;
    double value;

    @FXML
    Label label;
    @FXML
    TextField textField;
    @FXML
    Button button;

    public double getParameter() {
        return value;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void apply() {
        try {
            value = Double.parseDouble(textField.getText());
            stage.close();
        } catch (NullPointerException | NumberFormatException nfe) {
            textField.clear();
            AlertWindows.showWarning("Введите корректное значение");
        }
    }

}
