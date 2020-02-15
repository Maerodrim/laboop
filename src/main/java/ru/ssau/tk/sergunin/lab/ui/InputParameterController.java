package ru.ssau.tk.sergunin.lab.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ru.ssau.tk.sergunin.lab.functions.factory.TabulatedFunctionFactory;

public class InputParameterController implements Openable {

    @FXML
    Label label;
    @FXML
    TextField textField;
    @FXML
    Button button;
    private String value;
    private double doubleValue;
    private Class<?> typeOfParameter;

    private Stage stage;

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void setFactory(TabulatedFunctionFactory factory) {
    }

    @Override
    public void setParentController(Openable controller) {
    }

    @FXML
    public void ok() {
        try {
            value = textField.getText();
            if (typeOfParameter.equals(Double.TYPE)) {
                doubleValue = Double.parseDouble(value);
            }
            stage.close();
        } catch (NullPointerException | NumberFormatException e) {
            textField.clear();
            AlertWindows.showWarning("Введите корректное значение");
        }
    }

    public void setTypeOfParameter(Class<?> clazz) {
        typeOfParameter = clazz;
    }

    public String getParameter() {
        return value;
    }

    public double getDoubleParameter() {
        return doubleValue;
    }
}
