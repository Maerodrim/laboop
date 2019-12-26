package ru.ssau.tk.sergunin.lab.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import ru.ssau.tk.sergunin.lab.exceptions.NaNException;
import ru.ssau.tk.sergunin.lab.functions.MathFunction;
import ru.ssau.tk.sergunin.lab.functions.TabulatedFunction;
import ru.ssau.tk.sergunin.lab.functions.factory.TabulatedFunctionFactory;

import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

@ConnectableItem(name = "Create new math function", type = Item.CONTROLLER, pathFXML = "function.fxml")
public class MathFunctionController implements Initializable, Openable, MathFunctionAccessible {
    @FXML
    ComboBox<String> comboBox;
    @FXML
    TextField leftBorder;
    @FXML
    TextField rightBorder;
    @FXML
    TextField numberOfPoints;
    @FXML
    CheckBox isUnmodifiable;
    @FXML
    CheckBox isStrict;
    @FXML
    Button create;
    private Stage stage;
    private InputParameterController inputParameterController = new InputParameterController();
    private Openable parentController;
    private TabulatedFunctionFactory factory;
    private Map<String, MathFunction> functionMap;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        inputParameterController = Functions.initializeModalityWindow(
                Functions.FXML_PATH + "inputParameter.fxml", inputParameterController);
        inputParameterController.getStage().initOwner(stage);
        inputParameterController.getStage().setTitle("Input parameter");
    }

    @FXML
    private void createFunction() {
        if (functionMap.get(comboBox.getValue()).getClass().getDeclaredAnnotation(ConnectableItem.class).parameter()) {
            try {
                functionMap.replace(comboBox.getValue(), functionMap.get(comboBox.getValue()).getClass()
                        .getDeclaredConstructor(Double.TYPE).newInstance(inputParameterController.getParameter()));
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        try {
            TabulatedFunction function = factory.create(functionMap.get(comboBox.getValue()),
                    Double.parseDouble(leftBorder.getText()),
                    Double.parseDouble(rightBorder.getText()),
                    Integer.parseInt(numberOfPoints.getText()),
                    isStrict.isSelected(),
                    isUnmodifiable.isSelected());
            ((TableController) parentController).createTab(function);
            stage.close();
        } catch (NullPointerException | NumberFormatException nfe) {
            AlertWindows.showWarning("Введите корректные значения");
        } catch (IllegalArgumentException e) {
            AlertWindows.showWarning("Укажите положительное > 2 количество точек");
        } catch (NaNException e) {
            AlertWindows.showWarning("Результат не существует");
        }
    }

    @FXML
    private void doOnClickOnComboBox(ActionEvent event) {
        if (functionMap.get(((ComboBox) event.getSource()).getValue().toString()).getClass().getDeclaredAnnotation(ConnectableItem.class).parameter()) {
            inputParameterController.getStage().show();
        }
    }

    public void setFactory(TabulatedFunctionFactory factory) {
        this.factory = factory;
    }

    @Override
    public void setParentController(Openable controller) {
        parentController = controller;
    }

    public ComboBox<String> getComboBox() {
        return comboBox;
    }

    @Override
    public void setFunctionMap(Map<String, MathFunction> functionMap) {
        this.functionMap = functionMap;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
