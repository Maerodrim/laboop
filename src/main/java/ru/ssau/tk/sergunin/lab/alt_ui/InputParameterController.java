package ru.ssau.tk.sergunin.lab.alt_ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ru.ssau.tk.sergunin.lab.functions.factory.TabulatedFunctionFactory;

public class InputParameterController implements Openable {
    public InputParameterController() {
    }

    private Stage stage;
    private double value;

    @FXML
    Label label;
    @FXML
    TextField textField;
    @FXML
    Button button;

    double getParameter() {
        return value;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void setFactory(TabulatedFunctionFactory factory) { }

    @Override
    public void setParentController(Openable controller) { }

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
