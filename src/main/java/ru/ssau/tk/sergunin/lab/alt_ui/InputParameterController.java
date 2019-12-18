package ru.ssau.tk.sergunin.lab.alt_ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class InputParameterController {
    Stage stage;

    @FXML
    Label label;
    @FXML
    TextField textField;
    @FXML
    Button button;

    public String getTextFieldValue() {
        return textField.getText();
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void apply() {
        stage.close();
    }

}
